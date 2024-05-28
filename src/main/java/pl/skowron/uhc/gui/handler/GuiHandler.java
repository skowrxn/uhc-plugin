package pl.skowron.uhc.gui.handler;

import pl.skowron.uhc.gui.SettingsGui;

public class GuiHandler {

    private static SettingsGui settingsGui;

    public GuiHandler(){
        settingsGui = new SettingsGui();
    }

    public static SettingsGui getSettingsGui() { return settingsGui; }

}
