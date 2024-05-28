package pl.skowron.uhc.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;

import java.util.Arrays;

public class HungerChangeListener implements Listener {

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event){
        if(!(event.getEntity() instanceof Player))return;

        if(Arrays.asList(GameState.WAITING, GameState.COUNTDOWN).contains(GameEngine.getInstance().getGameState())){
            event.setCancelled(true);
        }

    }

}
