package pl.skowron.uhc.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.scoreboard.ScoreboardHandler;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.LocationUtil;
import pl.skowron.uhc.util.PlayerUtil;
import pl.skowron.uhc.util.TitleAPI;
import pl.skowron.uhc.world.WorldManager;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        event.setJoinMessage("");

        ScoreboardHandler.getInstance().handleJoin(player);

        UserEngine userEngine = UserEngine.getInstance();
        UHCPlayer uhcPlayer = userEngine.getUser(player.getUniqueId());

        if(uhcPlayer == null){
            userEngine.createUser(player);
        }

        GameState gameState = GameEngine.getInstance().getGameState();

        switch (gameState){

            case WAITING:
            case COUNTDOWN:
                player.teleport(LocationUtil.getSpawnLocation());

                PlayerUtil.reset(player);
                PlayerUtil.unfreezePlayer(player);

                if(!WorldManager.getInstance().isLoading())
                    TitleAPI.sendActionBarToAllPlayers(Messages.PLAYER_JOIN_ACTIONBAR.replace("%player", player.getName()).replace("%total", String.valueOf(userEngine.getGamePlayers())));
                Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.PLAYER_JOIN_MESSAGE.replace("%player", player.getName()).replace("%total", String.valueOf(userEngine.getGamePlayers()))));

                GameEngine.getInstance().checkRequiredPlayers();
                break;

            case SCATTERING:
                //TODO set spectator and notificate
                break;

            case PLAYING:
                //TODO set spectator
                break;
        }

    }

}
