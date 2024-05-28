package pl.skowron.uhc.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.TitleAPI;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserEngine {

    private final List<UHCPlayer> users;
    private static UserEngine instance;

    public UserEngine(){
        instance = this;

        this.users = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
             this.createUser(player);
        }
    }

    public UHCPlayer getUser(Player player){
        return this.users.stream().filter(user -> user.getUuid().equals(player.getUniqueId())).findFirst().orElse(null);
    }

    public UHCPlayer getUser(UUID uuid){
        return this.users.stream().filter(user -> user.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public UHCPlayer getUser(String name){
        return this.users.stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void deleteUser(UUID uuid){
        UHCPlayer uhcUser = getUser(uuid);
        if(uhcUser == null)return;
        this.users.remove(uhcUser);
    }

    public void deleteUser(String name){
        UHCPlayer uhcUser = getUser(name);
        if(uhcUser == null)return;
        this.users.remove(uhcUser);
    }

    public void createUser(Player player){
        UHCPlayer uhcUser = new UHCPlayer(player);
        this.users.add(uhcUser);
    }

    public void handlePlayerQuit(Player player){
        UHCPlayer uhcPlayer = this.getUser(player);
        GameState gameState = GameEngine.getInstance().getGameState();

        switch (gameState){
            case WAITING:
            case COUNTDOWN:
                TitleAPI.sendActionBarToAllPlayers(Messages.PLAYER_LEAVE_ACTIONBAR.replace("%player", player.getName()).replace("%total", String.valueOf(this.getGamePlayers())));
                Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.PLAYER_LEAVE_MESSAGE.replace("%player", player.getName()).replace("%total", String.valueOf(this.getGamePlayers()))));
                GameEngine.getInstance().checkRequiredPlayers();
                break;

            case SCATTERING:
                break;

            case PLAYING:
        }
    }

    public int getGamePlayers(){
        return this.users.size() - GameConfig.INITIAL_STAFF_MODE.size();
    }

    public List<UHCPlayer> getUsers() { return this.users; }
    public static UserEngine getInstance() { return instance; }

}
