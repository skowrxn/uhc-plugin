package pl.skowron.uhc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.world.WorldManager;

public class LoadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player))return false;

        UHCPlayer uhcPlayer = UserEngine.getInstance().getUser(player);
        GameState gameState = GameEngine.getInstance().getGameState();

        if(!uhcPlayer.isStaff()){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie masz permisji"));
            return false;
        }

        if(!gameState.equals(GameState.WAITING)){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie mozesz juz uzyc tej komendy"));
            return false;
        }

        WorldManager worldManager = WorldManager.getInstance();

        if(worldManager.isLoading() || worldManager.isFinishedLoading()){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie mozesz ponownie zaladowac swiata"));
            return false;
        }

        worldManager.loadWorld(GameConfig.INITIAL_BORDER);

        return true;
    }
}
