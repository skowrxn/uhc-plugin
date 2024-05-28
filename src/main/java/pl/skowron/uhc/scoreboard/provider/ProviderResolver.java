package pl.skowron.uhc.scoreboard.provider;


import org.bukkit.entity.Player;
import pl.skowron.uhc.scoreboard.SidebarEntry;
import pl.skowron.uhc.scoreboard.SidebarProvider;
import pl.skowron.uhc.user.PlayerState;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderResolver implements SidebarProvider {

	private Map<PlayerState, SidebarProvider> providers = new HashMap<>();

	public ProviderResolver() {
		this.providers.put(PlayerState.INGAME, new WaitingScoreboard());
		this.providers.put(PlayerState.SPECTATING, new WaitingScoreboard()); //TODO more scoreboards
		this.providers.put(PlayerState.STAFFMODE, new WaitingScoreboard());
		this.providers.put(PlayerState.WAITING, new WaitingScoreboard());
	}

	@Override
	public List<SidebarEntry> getLines(Player player) {
		UHCPlayer profile = UserEngine.getInstance().getUser(player);

		if (this.providers.containsKey(profile.getPlayerState())) {
			return this.providers.get(profile.getPlayerState()).getLines(player);
		}
		else {
			return Collections.emptyList();
		}
	}

}