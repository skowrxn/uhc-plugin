package pl.skowron.uhc.user;

import org.bukkit.entity.Player;
import pl.skowron.uhc.game.GameEngine;

import java.util.UUID;


public class UHCPlayer {

    private Player player;
    private UUID uuid;
    private String name;
    private int kills;
    private PlayerState playerState;

    public UHCPlayer(Player player){
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.kills = 0;
        switch (GameEngine.getInstance().getGameState()) {
            case WAITING, COUNTDOWN -> this.playerState = PlayerState.WAITING;
            default -> this.playerState = PlayerState.SPECTATING;
        }
    }

    public boolean isStaff(){
        return player.hasPermission("uhc.staff");
    }

    public boolean isInStaffMode() { return false; }
    public Player getPlayer() { return this.player; }
    public UUID getUuid() { return this.uuid; }
    public String getName() { return this.name; }
    public int getKills() { return this.kills; }
    public PlayerState getPlayerState(){ return this.playerState; }
    public void setPlayer(Player player) { this.player = player; }
    public void setUuid(UUID uuid){ this.uuid = uuid; }
    public void setName(String name) { this.name = name; }
    public void setKills(int kills) { this.kills = kills; }
    public void setPlayerState(PlayerState playerState) { this.playerState = playerState; }

}
