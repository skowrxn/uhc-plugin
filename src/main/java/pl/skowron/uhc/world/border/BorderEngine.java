package pl.skowron.uhc.world.border;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import pl.skowron.uhc.UHCPlugin;

public class BorderEngine {

    public void scheduleBorderShrink(World world, int minutesToGo, int deathmatchBorder){
        WorldBorder worldBorder = world.getWorldBorder();

        Bukkit.getScheduler().runTaskLater(UHCPlugin.getInstance(), () -> {

        }, 20L * minutesToGo);
    }


}
