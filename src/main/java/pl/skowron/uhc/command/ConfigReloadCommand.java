package pl.skowron.uhc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.skowron.uhc.configuration.ConfigurationFile;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.configuration.ScoreboardConfig;
import pl.skowron.uhc.util.ChatUtil;

public class ConfigReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender.hasPermission("uhc.reload") || sender.hasPermission("uhc.*"))){
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &cNie masz permisji"));
            return false;
        }

        boolean config = ConfigurationFile.reload();
        boolean messages = Messages.reload();
        boolean scoreboard = ScoreboardConfig.reload();


        if(messages && scoreboard && config)sender.sendMessage(ChatUtil.fixColor("&aPrzeladowano pliki konfiguracyjne pomyslnie"));
        else sender.sendMessage(ChatUtil.fixColor("&cWystapil blad podczas przeladowywania plikow konfiguracyjnych! Sprawdz konsole"));


        return true;
    }
}
