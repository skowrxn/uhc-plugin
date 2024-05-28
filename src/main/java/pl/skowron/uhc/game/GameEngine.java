package pl.skowron.uhc.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.skowron.uhc.UHCPlugin;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.time.Countdown;
import pl.skowron.uhc.time.TimeManager;
import pl.skowron.uhc.time.runnables.BorderTask;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.LocationUtil;
import pl.skowron.uhc.util.PlayerUtil;
import pl.skowron.uhc.util.TitleAPI;
import pl.skowron.uhc.world.WorldManager;

import java.util.Arrays;
import java.util.List;

public class GameEngine {

    private GameState gameState;
    private Countdown currentCountdown;
    private boolean wasForced;
    private final WorldManager worldManager;

    private static GameEngine instance;

    public GameEngine(){
        instance = this;
        this.gameState = GameState.WAITING;
        this.wasForced = false;
        this.worldManager = WorldManager.getInstance();
    }

    public void checkRequiredPlayers(){
        if(wasForced)return;
        if(!Arrays.asList(GameState.WAITING, GameState.COUNTDOWN).contains(this.gameState))return;

        int requiredPlayers = GameConfig.REQUIRED_PLAYERS;
        int totalPlayers = UserEngine.getInstance().getGamePlayers();

        if (totalPlayers >= requiredPlayers){
            this.startCountdown();
        } else {
            this.stopCountdown();
        }
    }

    public void startCountdown(){
        if(!this.getGameState().equals(GameState.WAITING))return;
        setGameState(GameState.COUNTDOWN);

        this.currentCountdown = new Countdown(15, UHCPlugin.getInstance()) {

            @Override
            public void count(int current) {
                if(Arrays.asList(15, 10).contains(current) || ( current <= 5 && current > 0 ) ){

                    TitleAPI.sendTitleToAllPlayers(Messages.TITLE_MAIN, Messages.GAME_COUNTDOWN_SUBTITLE.trim().equals("") ? null
                            : Messages.GAME_COUNTDOWN_SUBTITLE.replace("%time", current + " sekund"));
                    Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.GAME_COUNTDOWN_MESSAGE.replace("%time", current + " sekund")));

                } else if (current == 0){

                    gameState = GameState.SCATTERING;
                    scatter();
                    cancel();

                }

            }
        };

        this.currentCountdown.start();
    }

    public void enableEnvironmentalDamage(){
        Bukkit.broadcastMessage("Enabled damages");
    }

    public void enablePVP(){
        Bukkit.broadcastMessage("Enabled pvp");
    }

    public void startBorderShrink() {
        Bukkit.broadcastMessage("Started border shrinking");
    }

    public void stopCountdown(){
        if(!this.getGameState().equals(GameState.COUNTDOWN))return;

        currentCountdown.cancel();
        this.currentCountdown = null;

        setGameState(GameState.WAITING);

        Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.GAME_COUNTDOWN_CANCELLED_MESSAGE));
        TitleAPI.sendTitleToAllPlayers(Messages.TITLE_MAIN, Messages.GAME_COUNTDOWN_CANCELLED_SUBTITLE.trim().equals("") ? null : Messages.GAME_COUNTDOWN_SUBTITLE);
    }

    public void startGame(){
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(UHCPlugin.getInstance(), new BorderTask(worldManager.getWorldGenerator().getWorld()), 0, 20);
        TimeManager.getInstance().startTask();

        this.getGamePlayers().forEach(uhcPlayer -> {
            if(uhcPlayer.isInStaffMode())return;
            Player player = uhcPlayer.getPlayer();

            PlayerUtil.unfreezePlayer(player);
            PlayerUtil.reset(player);

            player.sendMessage(ChatUtil.fixColor(Messages.GAME_START_MESSAGE));
            TitleAPI.sendActionBar(player, Messages.GAME_START_ACTIONBAR);
            TitleAPI.sendTitle(player, Messages.TITLE_MAIN, Messages.GAME_START_SUBTITLE.trim().equals("") ? null : Messages.GAME_START_SUBTITLE);

        });
    }


    private void finalGameCountdown(){
        Countdown countdown = new Countdown(60, UHCPlugin.getInstance()) {
            @Override
            public void count(int current) {
                if(Arrays.asList(60, 30, 15, 10, 5, 4, 3, 2, 1).contains(current)) {
                    Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.GAME_COUNTDOWN_MESSAGE.replace("%time", current + " sekund")));
                    TitleAPI.sendTitleToAllPlayers(Messages.TITLE_MAIN, Messages.GAME_COUNTDOWN_SUBTITLE.trim().equals("") ? null :
                            Messages.GAME_COUNTDOWN_SUBTITLE.replace("%time", current + " sekund"));
                } else if (current == 0) {
                    startGame();
                    cancel();
                }
            }
        };

        countdown.start();


        Bukkit.broadcastMessage("Gra zostala wystartowana");
    }

    private void scatter(){
        int gamePlayers = getGamePlayersSize();

        UserEngine userEngine = UserEngine.getInstance();

        Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.TELEPORTATION_MESSAGE));
        TitleAPI.sendActionBarToAllPlayers(Messages.TELEPORTATION_ACTIONBAR.replace("%teleported", "0").replace("%total", String.valueOf(gamePlayers)));
        TitleAPI.sendPermTitleToAllPlayers(Messages.TITLE_MAIN, Messages.TELEPORTATION_SUBTITLE.trim().equals("") ? null : Messages.TELEPORTATION_SUBTITLE);

        this.setGameState(GameState.SCATTERING);

        Countdown countdown = new Countdown(userEngine.getGamePlayers() * 2 - 2, UHCPlugin.getInstance()) {

            @Override
            public void count(int current) {
                if(current % 2 != 0)return;

                int i = current / 2;

                UHCPlayer uhcPlayer = userEngine.getUsers().get(i);
                Player player = uhcPlayer.getPlayer();

                if(GameConfig.INITIAL_STAFF_MODE.contains(uhcPlayer))return;

                Location randomLocation = LocationUtil.getRandomLocation(worldManager.getWorldGenerator().getWorld(), (int) GameConfig.INITIAL_BORDER);
                player.teleport(randomLocation);

                PlayerUtil.reset(player);
                PlayerUtil.freezePlayer(player);

                TitleAPI.sendActionBarToAllPlayers(Messages.TELEPORTATION_ACTIONBAR.replace("%teleported", String.valueOf(i)).replace("%total", String.valueOf(gamePlayers)));
                Bukkit.broadcastMessage("current: " + current);

                if(current == 0){
                    finalGameCountdown();
                    cancel();
                }

            }
        };

        countdown.start();

    }

    public void deathmatch(){
        Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.DEATHMATCH_START_MESSAGE));
        TitleAPI.sendTitleToAllPlayers(Messages.TITLE_MAIN, Messages.DEATHMATCH_START_SUBTITLE);
        TitleAPI.sendActionBarToAllPlayers(Messages.DEATHMATCH_START_ACTIONBAR);
    }

    private int getGamePlayersSize() { return UserEngine.getInstance().getGamePlayers(); }
    private List<UHCPlayer> getGamePlayers() { return UserEngine.getInstance().getUsers(); }
    public void setGameState(GameState gameState){ this.gameState = gameState; }
    public GameState getGameState() { return this.gameState; }
    public Countdown getCurrentCountdown() { return this.currentCountdown; }
    public void setForced(boolean forced) { this.wasForced = forced; }
    public boolean wasForced() { return this.wasForced; }
    public static GameEngine getInstance() { return instance; }
}
