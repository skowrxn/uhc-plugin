package pl.skowron.uhc.time.runnables;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.TitleAPI;
import pl.skowron.uhc.game.GameEngine;

import java.util.Arrays;

public class BorderTask extends BukkitRunnable {

    private final int borderShrinkStartTime;
    private final int borderShrinkEndTime;
    private final int borderShrinkEndSize;

    private final World world;

    private int timer = 0;

    public BorderTask(World world){
        this.borderShrinkStartTime = GameConfig.BORDER_SHRINK_START;
        this.borderShrinkEndTime = GameConfig.BORDER_SHRINK_END;
        this.borderShrinkEndSize = 50;

        this.world = world;
    }

    @Override
    public void run() {
        int timeToDeathmatch = this.borderShrinkEndTime - this.timer;
        int timeToShrinkStart = this.borderShrinkStartTime - this.timer;

        if (Arrays.asList(1, 2, 3, 4, 5, 30, 60, 300).contains(timeToShrinkStart)) {
            Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.BORDER_COUNTDOWN_MESSAGE));
            TitleAPI.sendActionBarToAllPlayers(Messages.BORDER_COUNTDOWN_ACTIONBAR);
        } else if (timeToShrinkStart == 0) {
            Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.BORDER_START_MESSAGE));
            TitleAPI.sendActionBarToAllPlayers(Messages.BORDER_START_ACTIONBAR);

            world.getWorldBorder().setSize(borderShrinkEndSize, this.borderShrinkEndSize - this.borderShrinkStartTime);
        }
        if (Arrays.asList(1, 2, 3, 4, 5, 30, 60, 300).contains(timeToDeathmatch)) {
            Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.DEATHMATCH_COUNTDOWN_MESSAGE));
            TitleAPI.sendActionBarToAllPlayers(Messages.DEATHMATCH_COUNTDOWN_ACTIONBAR);
        } else if (timeToDeathmatch == 0) {
            GameEngine.getInstance().deathmatch();
            cancel();
        }


        timer++;

    }



}
