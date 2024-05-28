package pl.skowron.uhc.scoreboard.provider;

import org.bukkit.entity.Player;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.configuration.ScoreboardConfig;
import pl.skowron.uhc.scoreboard.SidebarEntry;
import pl.skowron.uhc.scoreboard.SidebarProvider;
import pl.skowron.uhc.game.GameEngine;
import pl.skowron.uhc.game.GameState;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class WaitingScoreboard implements SidebarProvider {

    private final String SCOREBOARD_SPACER = ChatUtil.fixColor("&7&m-------");

    public List<SidebarEntry> getLines(Player player) {

        List<SidebarEntry> lines = new ArrayList<>();
        int players = UserEngine.getInstance().getGamePlayers();
        int border = (int) GameConfig.INITIAL_BORDER;
        String time;
        GameEngine gameEngine = GameEngine.getInstance();
        GameState gameState = gameEngine.getGameState();

        if(gameState.equals(GameState.COUNTDOWN)) time = gameEngine.getCurrentCountdown().getTime() + " sekund";
        else if(gameState.equals(GameState.SCATTERING))time = "Teleportacja...";
        else time = "Oczekiwanie...";

        int x = 1;

        for(String string : ScoreboardConfig.WAITING){
            string = string.replace("%time", time).replace("%players", String.valueOf(players)).replace("%border", String.valueOf(border));

            if(string.length() > 16){
                int i = string.length()/3;

                String splitted1 = string.substring(0, i);
                String splitted2 = string.substring(i, 2*i);
                String splitted3 = string.substring(2*i);

                lines.add(new SidebarEntry(ChatUtil.fixColor(splitted1) ,ChatUtil.fixColor(splitted2),ChatUtil.fixColor(splitted3)));
            } else {
                if(string.trim().equalsIgnoreCase("")){
                    lines.add(new SidebarEntry("", " ".repeat(Math.max(0, x)), ""));
                    x++;
                } else {
                    lines.add(new SidebarEntry("", ChatUtil.fixColor(string), ""));
                }
            }
        }

        return lines;
    }

}