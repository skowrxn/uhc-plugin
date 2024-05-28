package pl.skowron.uhc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.LocationUtil;

import java.util.Arrays;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))return false;

        Player player = (Player)sender;

        if(!(player.hasPermission("uhc.*") || player.hasPermission("uhc.setspawn"))){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie masz permisji"));
            return false;
        }

        GameState gameState = GameEngine.getInstance().getGameState();

        if(!Arrays.asList(GameState.COUNTDOWN, GameState.WAITING).contains(gameState)){
            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie mozesz tego zrobic, poniewaz gra juz wystartowala"));
            return false;
        }

        LocationUtil.updateSpawnLocation(player.getLocation());
        player.sendMessage(ChatUtil.fixColor("&aPomyslnie ustawiono punkt spawnu"));

        return true;
    }
}
