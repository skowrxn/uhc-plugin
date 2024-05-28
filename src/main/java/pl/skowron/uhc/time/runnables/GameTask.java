package pl.skowron.uhc.time.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import pl.skowron.uhc.time.TimeManager;

public class GameTask extends BukkitRunnable {

    TimeManager timeManager = TimeManager.getInstance();

    @Override
    public void run() {
        timeManager.secondElapsed();
        timeManager.checkTimestamp();
    }
}
