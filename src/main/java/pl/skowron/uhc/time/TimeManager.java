package pl.skowron.uhc.time;

import org.bukkit.Bukkit;
import pl.skowron.uhc.UHCPlugin;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.util.TitleAPI;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameEvent;
import pl.skowron.uhc.time.runnables.GameTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TimeManager {

    private final HashMap<Long, GameEvent> timestamps;
    private static TimeManager instance;
    private long timeElapsed = 0;

    public TimeManager(){
        instance = this;
        this.timestamps = new HashMap<>();
        this.timestamps.put(10L, GameEvent.DAMAGE_ENABLE);
        this.timestamps.put(TimeUnit.MINUTES.toSeconds(GameConfig.PVP_PROTECTION_END), GameEvent.PVP_ENABLE);
        this.timestamps.put(TimeUnit.MINUTES.toSeconds(GameConfig.BORDER_SHRINK_START), GameEvent.BORDER_SHRINK);
    }

    public void startTask(){
        Bukkit.getScheduler().runTaskTimer(UHCPlugin.getInstance(), new GameTask(), 20, 20);
    }

    public void checkTimestamp(){
        if(this.timestamps.containsKey(this.timeElapsed)) {
            Bukkit.getScheduler().runTask(UHCPlugin.getInstance(), this.timestamps.get(this.timeElapsed).getRunnable());
            this.timestamps.remove(this.timeElapsed);
            return;
        }

        int timeToNextEvent = this.getTimeToNextEvent();

        if(Arrays.asList(1,2,3,15,30,60).contains(timeToNextEvent)){
            GameEngine gameEngine = GameEngine.getInstance();
            switch (this.getNextEvent()) {
                case DAMAGE_ENABLE ->
                        TitleAPI.sendTitleToAllPlayers("", "&7Obrazenia zostana wlaczone za &e" + timeToNextEvent + " sekund");
                case PVP_ENABLE ->
                        TitleAPI.sendTitleToAllPlayers("", "&7PVP zostanie wlaczone za &e" + timeToNextEvent + " sekund");
                case BORDER_SHRINK ->
                        TitleAPI.sendTitleToAllPlayers("", "&7Granica zacznie sie zmiejszac za &e" + timeToNextEvent + " sekund");
            }
        }
    }

    public GameEvent getNextEvent(){
        return this.timestamps.get(0L);
    }

    public int getTimeToNextEvent(){
        return (int) ((long)this.timestamps.keySet().toArray()[0] - this.timeElapsed);
    }

    public void secondElapsed() { this.timeElapsed++; }

    public static TimeManager getInstance() { return instance; }
    public long getTimeElapsed() { return this.timeElapsed; }




}
