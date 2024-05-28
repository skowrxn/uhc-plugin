package pl.skowron.uhc.configuration;

import pl.skowron.uhc.user.UHCPlayer;

import java.util.ArrayList;

public class GameConfig {

    public static int PVP_PROTECTION_END;
    public static double INITIAL_BORDER;
    public static int BORDER_SHRINK_START;
    public static int BORDER_SHRINK_END;
    public static int REQUIRED_PLAYERS;
    public static boolean CUTCLEAN_ENABLED;
    public static ArrayList<UHCPlayer> INITIAL_STAFF_MODE;

    public GameConfig(){
        PVP_PROTECTION_END = 10;
        INITIAL_BORDER = 1000;
        BORDER_SHRINK_START = 10;
        BORDER_SHRINK_END = 45;
        REQUIRED_PLAYERS = 16;
        CUTCLEAN_ENABLED = true;
        INITIAL_STAFF_MODE = new ArrayList<>();
    }

    public static void increasePVPProtection(){
        if(PVP_PROTECTION_END < 30) PVP_PROTECTION_END += 5;
        else PVP_PROTECTION_END = 0;
    }

    public static void increaseInitialBorder(){
        if(INITIAL_BORDER < 3000) INITIAL_BORDER += 250;
        else INITIAL_BORDER = 250;
    }

    public static void increaseBorderShrinkStart(){
        if(BORDER_SHRINK_START < 30) BORDER_SHRINK_START += 5;
        else BORDER_SHRINK_START = 0;
    }

    public static void increaseBorderShrinkEnd(){
        if(BORDER_SHRINK_END < 90) BORDER_SHRINK_END += 5;
        else BORDER_SHRINK_END = 30;
    }

    public static void increaseRequiredPlayers(){
        if(REQUIRED_PLAYERS < 256) REQUIRED_PLAYERS += 4;
        else REQUIRED_PLAYERS = 4;
    }

    public static void decreaseRequiredPlayers(){
        if(REQUIRED_PLAYERS > 7) REQUIRED_PLAYERS -= 4;
        else REQUIRED_PLAYERS = 4;
    }






}
