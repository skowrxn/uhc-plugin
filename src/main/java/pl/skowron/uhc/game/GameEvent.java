package pl.skowron.uhc.game;



public enum GameEvent {

    DAMAGE_ENABLE("Obrazenia", () -> GameEngine.getInstance().enableEnvironmentalDamage()),
    PVP_ENABLE("PVP", () -> GameEngine.getInstance().enablePVP()),
    BORDER_SHRINK("Granica", () -> GameEngine.getInstance().startBorderShrink());

    private final String name;
    private final Runnable runnable;

    GameEvent(String name, Runnable runnable){
        this.name = name;
        this.runnable = runnable;
    }

    public String getName() { return this.name; }
    public Runnable getRunnable() { return this.runnable; }

}
