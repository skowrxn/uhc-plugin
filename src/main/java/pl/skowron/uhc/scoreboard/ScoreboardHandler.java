package pl.skowron.uhc.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pl.skowron.uhc.UHCPlugin;
import pl.skowron.uhc.scoreboard.provider.ProviderResolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardHandler implements Listener {

	private static ScoreboardHandler instance;

	private final Map<UUID, PlayerBoard> playerBoards = new HashMap<>();
	private final ProviderResolver timerSidebarProvider;

	public ScoreboardHandler() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(this, UHCPlugin.getInstance());
		this.timerSidebarProvider = new ProviderResolver();

		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

		for (Player player : players) {
			this.applyBoard(player).addUpdates(players);
		}
	}

	public PlayerBoard getPlayerBoard(UUID uuid) {
		return this.playerBoards.get(uuid);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.playerBoards.remove(event.getPlayer().getUniqueId()).remove();
	}

	public PlayerBoard applyBoard(Player player) {
		PlayerBoard board = new PlayerBoard(player);
		PlayerBoard previous = this.playerBoards.put(player.getUniqueId(), board);

		if (previous != null && previous != board) {
			previous.remove();
		}

		board.setSidebarVisible();
		board.setDefaultSidebar(this.timerSidebarProvider);

		return board;
	}

	public void handleJoin(Player player){
		this.applyBoard(player);

		for (PlayerBoard board : getPlayerBoards().values()) {
			board.addUpdate(player);
		}

		new BukkitRunnable() {
			public void run() {
				getPlayerBoard(player.getUniqueId()).addUpdates(Bukkit.getOnlinePlayers());
			}
		}.runTaskLaterAsynchronously(UHCPlugin.getInstance(), 4L);
	}

	public static ScoreboardHandler getInstance() { return instance; }
	public Map<UUID, PlayerBoard> getPlayerBoards() { return this.playerBoards; }

}