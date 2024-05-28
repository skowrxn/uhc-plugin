package pl.skowron.uhc.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.TitleAPI;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage("");

        UserEngine userEngine = UserEngine.getInstance();
        UHCPlayer uhcPlayer = userEngine.getUser(player);
        GameState gameState = GameEngine.getInstance().getGameState();

        userEngine.deleteUser(player.getUniqueId());

        switch (gameState){
            case WAITING:
            case COUNTDOWN:
                TitleAPI.sendActionBarToAllPlayers(Messages.PLAYER_LEAVE_ACTIONBAR.replace("%player", player.getName()).replace("%total", String.valueOf(userEngine.getGamePlayers())));
                Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.PLAYER_LEAVE_MESSAGE.replace("%player", player.getName()).replace("%total", String.valueOf(userEngine.getGamePlayers()))));
                GameEngine.getInstance().checkRequiredPlayers();
                break;

            case SCATTERING: //TODO LEAVING WHILE SCATTTERRING
                break;

            case PLAYING:
                break;
        }




    }


}
