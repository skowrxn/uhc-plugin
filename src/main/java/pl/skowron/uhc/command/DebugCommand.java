package pl.skowron.uhc.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.skowron.uhc.world.WorldManager;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player)sender;

        if(!player.isOp())return false;

        if(args.length == 1){
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            Bukkit.broadcastMessage("Ropoczeto usuwanie swiata...");
            WorldManager.getInstance().deleteWorld();
            Bukkit.broadcastMessage("Usunieto swiat");
            return true;
        } else if (args.length == 2){
            Bukkit.broadcastMessage(String.valueOf(Bukkit.getWorld("uhc").getLoadedChunks().length));
            return true;
        } else {
            player.setFlySpeed(0.2F);
            player.teleport(WorldManager.getInstance().getWorldGenerator().getWorld().getSpawnLocation());
            return true;
        }
    }
}
