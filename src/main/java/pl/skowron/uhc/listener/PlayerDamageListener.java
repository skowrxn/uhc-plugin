package pl.skowron.uhc.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.user.PlayerState;
import pl.skowron.uhc.user.UserEngine;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player))return;

        PlayerState playerState = UserEngine.getInstance().getUser(player).getPlayerState();
        if(!playerState.equals(PlayerState.INGAME)) event.setCancelled(true);

        GameState gameState = GameEngine.getInstance().getGameState();

        if(!gameState.equals(GameState.PLAYING)) event.setCancelled(true);

    }

}
