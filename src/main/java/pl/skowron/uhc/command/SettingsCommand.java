package pl.skowron.uhc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.gui.handler.GuiHandler;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;

import java.util.Arrays;

public class SettingsCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("Komenda dostepna dla graczy");
            return false;
        }

        Player player = (Player)sender;
        UHCPlayer uhcPlayer = UserEngine.getInstance().getUser(player);

        if(!uhcPlayer.isStaff()){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie masz permisji"));
            return false;
        }

        if(!Arrays.asList(GameState.COUNTDOWN, GameState.WAITING).contains(GameEngine.getInstance().getGameState())){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cGra juz wystartowala"));
            return false;
        }

        GuiHandler.getSettingsGui().open(player);

        return true;
    }
}
