package pl.skowron.uhc.configuration;


import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.skowron.uhc.UHCPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ScoreboardConfig {

    public static String TITLE = "&6&lUHC";
    public static List<String> WAITING = Arrays.asList("", "&7Start:", "&e%time", "", "&7Graczy: &e%players", "&7Granica: &e%border", "");
    public static List<String> FINISHED = Arrays.asList("", "&7Zwyciezca: &e%winner", "", "&7Czas gry: &e%time:", "&7Zabojstwa: &e%kills", "");

    private static FileConfiguration scoreboardConfig;
    private static boolean firstLaunch = false;

    public ScoreboardConfig() throws IOException, IllegalAccessException {
        File file = new File(UHCPlugin.getInstance().getDataFolder(), "scoreboard.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            firstLaunch = true;
        }

        scoreboardConfig = new YamlConfiguration();

        try {
            scoreboardConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace(); //TODO exception caught
        }

        for(Field field : this.getClass().getFields()){

            String name = field.getName();
            Object defaultValue = field.get("");

            if(name.equalsIgnoreCase("TITLE")){
                if(scoreboardConfig.getString(name) == null || (firstLaunch && scoreboardConfig.getString(name).isEmpty()) ){
                    scoreboardConfig.set(name, defaultValue);
                } else {
                    field.set("", scoreboardConfig.getString(name));
                }
                continue;
            }

            if(scoreboardConfig.getStringList(name) == null || (firstLaunch && scoreboardConfig.getStringList(name).isEmpty()) ){
                scoreboardConfig.set(name, defaultValue);
            } else {
                field.set("", scoreboardConfig.getStringList(name));
            }
        }

        try {
            scoreboardConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace(); //TODO exception caught
        }

    }

    public static boolean reload(){

        File file = new File(UHCPlugin.getInstance().getDataFolder(), "messages.yml");
        scoreboardConfig = new YamlConfiguration();

        try {
            scoreboardConfig.load(file);
            for(Field field : Messages.class.getFields()){

                String name = field.getName();
                List<String> defaultValue = (List<String>)field.get("");

                if(scoreboardConfig.getStringList(name) == null){
                    scoreboardConfig.set(name, defaultValue);
                } else {
                    field.set("", scoreboardConfig.getStringList(name));
                }
            }
            scoreboardConfig.save(file);
            return true;
        } catch (IOException | InvalidConfigurationException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }


}
