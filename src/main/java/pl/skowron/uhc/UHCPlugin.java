package pl.skowron.uhc;


import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pl.skowron.uhc.command.*;
import pl.skowron.uhc.configuration.ConfigurationFile;
import pl.skowron.uhc.listener.*;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.configuration.ScoreboardConfig;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.gui.handler.GuiHandler;
import pl.skowron.uhc.time.TimeManager;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.scoreboard.ScoreboardHandler;
import pl.skowron.uhc.world.WorldManager;

import java.io.IOException;

public final class UHCPlugin extends JavaPlugin {

    private static UHCPlugin instance;

    @Override
    public void onEnable() {

        instance = this;

        Bukkit.getLogger().info("Trwa ladowanie pluginu");

        this.loadCommands();
        this.loadConfiguration();
        this.loadListeners();

        new WorldManager();
        new GameEngine();
        new UserEngine();
        new GuiHandler();
        new ScoreboardHandler();
        new TimeManager();

    }

    private void loadConfiguration() {
        new Messages();
        new ConfigurationFile();
        try {
            new ScoreboardConfig();
        } catch (IOException | IllegalAccessException e) {
            Bukkit.getLogger().info("Wystapil blad podczas ladowania scoreboard'u");
            Bukkit.getLogger().info(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        new GameConfig();
    }

    private void loadCommands(){
        getCommand("ustawienia").setExecutor(new SettingsCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("odliczanie").setExecutor(new CountdownCommand());
        getCommand("configreload").setExecutor(new ConfigReloadCommand());
        getCommand("load").setExecutor(new LoadCommand());
        getCommand("debug").setExecutor(new DebugCommand());
    }

    private void loadListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerDamageListener(), this);
        pluginManager.registerEvents(new BlockBreakListener(), this);
        pluginManager.registerEvents(new HungerChangeListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Wylaczanie pluginu...");
    }


    public static UHCPlugin getInstance() { return instance; }

}
