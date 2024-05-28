package pl.skowron.uhc.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.user.UserEngine;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){

        GameState gameState = GameEngine.getInstance().getGameState();
        if(!gameState.equals(GameState.PLAYING))return;

        Block block = event.getBlock();
        Player player = event.getPlayer();

        if(UserEngine.getInstance().getUser(player).isInStaffMode()) event.setCancelled(true);

        if(!GameConfig.CUTCLEAN_ENABLED)return;

        if(block.getType().equals(Material.IRON_ORE)){
            event.setCancelled(true);
            block.setType(Material.AIR);
            block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_INGOT));
        } else if (block.getType().equals(Material.GOLD_ORE)){
            event.setCancelled(true);
            block.setType(Material.AIR);
            block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
        }




    }

}
