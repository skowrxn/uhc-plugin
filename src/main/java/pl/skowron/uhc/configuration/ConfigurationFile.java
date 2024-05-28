package pl.skowron.uhc.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.skowron.uhc.UHCPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class ConfigurationFile {

    public static String
            SPAWN_WORLD = "world";

    public static double
            SPAWN_X = 0,
            SPAWN_Y = 51,
            SPAWN_Z = 0,
            SPAWN_PITCH = 0,
            SPAWN_YAW = 0;



    private static FileConfiguration config;
    private static boolean firstLaunch = false;

    public ConfigurationFile(){
        File file = new File(UHCPlugin.getInstance().getDataFolder(), "configuration.yml");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                firstLaunch = true;
            }
            config = new YamlConfiguration();
            config.load(file);

            for(Field field : this.getClass().getFields()){

                String name = field.getName().replace("_", ".");
                Object defaultValue = field.get(field.getType());

                if (config.get(name) == null || firstLaunch) {
                    config.set(name, defaultValue);
                } else {
                    field.set(field.getType(), config.get(name));
                }
            }

            config.save(file);
        } catch (IOException | IllegalAccessException | InvalidConfigurationException e) {
            Bukkit.broadcastMessage("Blad podczas zapisaywania pliku konfiguracyjego!");
            Bukkit.broadcastMessage(e.getMessage());
        }

    }

    public static boolean reload(){
        File file = new File(UHCPlugin.getInstance().getDataFolder(), "configuration.yml");
        config = new YamlConfiguration();

        try {
            config.load(file);
            for(Field field : ConfigurationFile.class.getFields()){

                String name = field.getName().replace("_", ".");
                Object defaultValue = field.get(field.getType());

                if (config.get(name) == null || firstLaunch) {
                    config.set(name, defaultValue);
                } else {
                    field.set(field.getType(), config.get(name));
                }
            }
            config.save(file);
            return true;
        } catch (IOException | IllegalAccessException | InvalidConfigurationException e) {
            Bukkit.broadcastMessage("Blad podczas przeladowywania pliku konfiguracyjego!");
            Bukkit.broadcastMessage(e.getMessage());
            return false;
        }

    }

    public static void saveLocationToConfig(){
        config.set("SPAWN.WORLD", SPAWN_WORLD);
        config.set("SPAWN.X", SPAWN_X);
        config.set("SPAWN.Y", SPAWN_Y);
        config.set("SPAWN.Z", SPAWN_Z);
        config.set("SPAWN.PITCH", SPAWN_YAW);
        config.set("SPAWN.YAW", SPAWN_PITCH);

        File file = new File(UHCPlugin.getInstance().getDataFolder(), "configuration.yml");

        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.broadcastMessage("Blad podczas zapisywania lokalizacji do pliku konfiguracyjego!");
            Bukkit.broadcastMessage(e.getMessage());
        }

    }



}
