package pl.skowron.uhc.time;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Countdown {

    private int time;

    protected BukkitTask task;
    protected final Plugin plugin;

    public Countdown(int time, Plugin plugin) {
        this.time = time;
        this.plugin = plugin;
    }

    public abstract void count(int current);

    public final void start() {
        task = new BukkitRunnable() {

            @Override
            public void run() {
                count(time);
                if (time-- <= 0) cancel();
            }

        }.runTaskTimer(plugin, 0L, 20L);
    }

    public final void cancel(){ Bukkit.getScheduler().cancelTask(this.task.getTaskId()); }
    public int getTime() { return this.time; }

}