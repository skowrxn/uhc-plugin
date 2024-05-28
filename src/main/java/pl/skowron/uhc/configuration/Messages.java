package pl.skowron.uhc.configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.skowron.uhc.UHCPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;


public class Messages {

    public static String
    TITLE_MAIN = "&6UHC",
    PLAYER_KILL_MESSAGE = "&7Gracz &e%killer &7zabil gracza &e%killed",
    PLAYER_JOIN_MESSAGE = "&6&lUHC &8>> &7Gracz &e%player &7dolaczyl do gry &e(%total/64)",
    PLAYER_JOIN_ACTIONBAR = "&6&lUHC &8>> &7Gracz &e%player &7dolaczyl do gry &e(%total/64)",
    PLAYER_LEAVE_MESSAGE = "&6&lUHC &8>> &7Gracz &e%player &7opuscil gre &e(%total/64)",
    PLAYER_LEAVE_ACTIONBAR = "&6&lUHC &8>> &7Gracz &e%player &7opuscil gre &e(%total/64)",
    WORLD_CREATING_MESSAGE = "&6&lUHC &8>> &7Trwa tworzenie swiata...",
    WORLD_LOADING_ACTIONBAR = "&6&lUHC &8>> &7Trwa ladowanie swiata... &e(%progress)",
    WORLD_LOADING_FINISHED_MESSAGE = "&6&lUHC &8>> &7Pomyslnie zaladowano swiat",
    GAME_COUNTDOWN_SUBTITLE = "&7Gra rozpocznie sie za &e%time",
    GAME_COUNTDOWN_MESSAGE = "&6&lUHC &8>> &7Gra ropocznie sie za &e%time",
    GAME_COUNTDOWN_CANCELLED_MESSAGE = "&6&lUHC &8>> &7Odliczanie zostalo przerwane!",
    GAME_COUNTDOWN_CANCELLED_SUBTITLE = "&7Odliczanie zostalo przerwane",
    TELEPORTATION_MESSAGE = "&6&lUHC &8>> &7Trwa teleportacja graczy...",
    TELEPORTATION_ACTIONBAR = "&6&lUHC &8>> &7Trwa teleportacja graczy... &e(%teleported/%total)",
    TELEPORTATION_SUBTITLE = "&7Trwa teleportacja graczy...",
    TELEPORTATION_FINISHED_SUBTITLE = "&7Pomyslnie przeteleportowano wszystkich graczy",
    TELEPORTATION_FINISHED_MESSAGE = "&6&lUHC &8>> &7Pomyslnie przeteleportowano wszystkich graczy",
    GAME_START_MESSAGE = "&6&lUHC &8>> &7Gra zostala rozpoczeta. Powodzenia!",
    GAME_START_SUBTITLE = "&7Gra zostala rozpoczeta",
    GAME_START_ACTIONBAR = "&6&lUHC &8>> &7Gra zostala rozpoczeta. Powodzenia!",
    PVP_COUNTDOWN_MESSAGE = "&6&lUHC &8>> &7PvP zostanie wlaczone za &e%time",
    PVP_COUNTDOWN_SUBTITLE = "&7PvP zostanie wlaczone za &e%time",
    PVP_COUNTDOWN_ACTIONBAR = "&6&lUHC &8>> &7PvP zostanie wlaczone za &e%time",
    PVP_START_MESSAGE = "&6&lUHC &8>> &7PvP zostalo &ewlaczone",
    PVP_START_SUBTITLE = "&7PvP zostalo &ewlaczone",
    BORDER_COUNTDOWN_MESSAGE = "&6&lUHC &8>> &7Granica zacznie sie zmniejszac za &e%time",
    BORDER_COUNTDOWN_ACTIONBAR = "&6&lUHC &8>> &7Granica zacznie sie zmniejszac za &e%time",
    BORDER_START_MESSAGE = "&6&lUHC &8>> &7Granica zaczela sie &ezmniejszac",
    BORDER_START_ACTIONBAR = "&6&lUHC &8>> &7Granica zaczela sie &ezmniejszac",
    DEATHMATCH_COUNTDOWN_MESSAGE = "&6&lUHC &8>> &7Deathmatch rozpocznie sie za &e%time",
    DEATHMATCH_COUNTDOWN_ACTIONBAR = "&6&lUHC &8>> &7Deathmatch rozpocznie sie za &e%time",
    DEATHMATCH_START_SUBTITLE = "&7Deathmatch zostal &erozpoczety",
    DEATHMATCH_START_MESSAGE = "&6&lUHC &8>> &7Deathmatch zostal &erozpoczety",
    DEATHMATCH_START_ACTIONBAR = "&6&lUHC &8>> &7Deathmatch zostal &erozpoczety",
    WINNER_MESSAGE = "&6&lUHC &8>> &7Gracz &e%player &7wygral gre!",
    WINNER_SUBTITLE = "&7Gracz &e%player &7wygral gre!",
    WINNER_ACTIONBAR = "&6&lUHC &8>> &7Gracz &e%player &7wygral gre!";


    private static FileConfiguration messagesConfig;
    private static boolean firstLaunch = false;

    public Messages(){
        File file = new File(UHCPlugin.getInstance().getDataFolder(), "messages.yml");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                firstLaunch = true;
            }
            messagesConfig = new YamlConfiguration();
            messagesConfig.load(file);
            for(Field field : this.getClass().getFields()){
                String name = field.getName();
                String defaultValue = (String)field.get("");

                if(messagesConfig.getString(name) == null || (firstLaunch && messagesConfig.getString(name).equals("")) ){
                    messagesConfig.set(name, defaultValue);
                } else {
                    field.set("", messagesConfig.getString(name));
                }
            }
            messagesConfig.save(file);
        } catch (IOException | IllegalAccessException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static boolean reload(){
        File file = new File(UHCPlugin.getInstance().getDataFolder(), "messages.yml");
        messagesConfig = new YamlConfiguration();

        try {
            messagesConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            for(Field field : Messages.class.getFields()){
                String name = field.getName();
                String defaultValue = (String)field.get("");

                if(messagesConfig.getString(name) == null){
                    messagesConfig.set(name, defaultValue);
                } else {
                    field.set("", messagesConfig.getString(name));
                }
            }
            messagesConfig.save(file);
            return true;
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace(); //TODO exception caught!!!
            return false;
        }
    }


}
