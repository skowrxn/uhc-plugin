package pl.skowron.uhc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.world.WorldManager;

public class CountdownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)){
            sender.sendMessage("Komenda dostepna dla graczy");
            return false;
        }

        UHCPlayer uhcPlayer = UserEngine.getInstance().getUser(player);

        if(!uhcPlayer.isStaff()){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie masz uprawnien do uzycia komendy"));
            return false;
        }

        GameEngine gameEngine = GameEngine.getInstance();

        if(!gameEngine.getGameState().equals(GameState.WAITING)){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cOdliczanie juz sie rozpoczelo lub zakonczylo"));
            return false;
        }

        if(!WorldManager.getInstance().isFinishedLoading()){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cMapa jest w trakcie lub nie zostala jeszcze zaladowana (/load)"));
            return false;
        }

        gameEngine.setForced(true);
        gameEngine.startCountdown();

        return true;
    }
}
