package pl.skowron.uhc.scoreboard;

import com.google.common.collect.Iterables;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import pl.skowron.uhc.UHCPlugin;
import pl.skowron.uhc.configuration.ScoreboardConfig;
import pl.skowron.uhc.util.ChatUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerBoard {

	private final AtomicBoolean removed = new AtomicBoolean(false);
	private final Team members;
	private final BufferedObjective bufferedObjective;
	private final Scoreboard scoreboard;
	private final Player player;
	private SidebarProvider defaultProvider;
	private BukkitRunnable runnable;

	public PlayerBoard(Player player) {
		this.player = player;

		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.bufferedObjective = new BufferedObjective(scoreboard);

		this.members = scoreboard.registerNewTeam("members");

		player.setScoreboard(scoreboard);
	}

	public void remove() {
		if (!this.removed.getAndSet(true) && scoreboard != null) {
			for (Team team : scoreboard.getTeams()) {
				team.unregister();
			}

			for (Objective objective : scoreboard.getObjectives()) {
				objective.unregister();
			}
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}

	public void setSidebarVisible() {
		this.bufferedObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public void setDefaultSidebar(SidebarProvider provider) {
		if (provider != this.defaultProvider) {
			this.defaultProvider = provider;

			if (this.runnable != null) {
				this.runnable.cancel();
			}

			if (provider == null) {
				this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
				return;
			}

			(this.runnable = new BukkitRunnable() {
				@Override
				public void run() {
					if (removed.get()) {
						cancel();
						return;
					}

					if (provider == defaultProvider) {
						updateObjective();
					}
				}
			}).runTaskTimerAsynchronously(UHCPlugin.getInstance(), 20L, 20L);
		}
	}

	private void updateObjective() {
		if (this.removed.get()) {
			throw new IllegalStateException("Cannot update whilst board is removed");
		}

		SidebarProvider provider = this.defaultProvider;

		if (provider == null) {
			this.bufferedObjective.setVisible(false);
		}
		else {
			this.bufferedObjective.setTitle(ChatUtil.fixColor(ScoreboardConfig.TITLE));
			this.bufferedObjective.setAllLines(provider.getLines(player));
			this.bufferedObjective.flip();
		}
	}

	public void addUpdate(Player target) {
		this.addUpdates(Collections.singleton(target));
	}

	public void addUpdates(Iterable<? extends Player> updates) {
		if (Iterables.size(updates) == 0) {
			return;
		}

		if (this.removed.get()) {
			throw new IllegalStateException("Cannot update whilst board is removed.");
		}


		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player update : updates) {
					if (update == null || !update.isOnline()) {
						continue;
					}

					List<Team> removeFrom = new ArrayList<>();

					for (Team team : scoreboard.getTeams()) {
						if (team.hasPlayer(update)) {
							removeFrom.add(team);
						}
					}

					for (Team team : removeFrom) {
						team.removePlayer(update);
					}

					if (player.equals(update)) {
						if (!members.hasPlayer(update)) {
							members.addPlayer(update);
						}
					}

				}
			}
		}.runTaskAsynchronously(UHCPlugin.getInstance());
	}

}