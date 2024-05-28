package pl.skowron.uhc.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.skowron.uhc.UHCPlugin;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.user.UHCPlayer;
import pl.skowron.uhc.user.UserEngine;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.ItemBuilder;
import pl.skowron.uhc.world.WorldManager;


public class SettingsGui implements Listener {

    private final Inventory inventory;
    private final String title = "&eUstawienia gry";

    public SettingsGui(){
        this.inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatUtil.fixColor(this.title));
        Bukkit.getPluginManager().registerEvents(this, UHCPlugin.getInstance());
    }


    public void open(Player player){
        UHCPlayer uhcUser = null; //TODO uhcusers

        ItemStack protection = new ItemBuilder(Material.DIAMOND_SWORD).name("&eOchrona PVP")
                .lore("&7- Koniec ochrony PVP:&e" + (" " + GameConfig.PVP_PROTECTION_END + "min").replace(" 0min", " Brak ochrony")).build();
        ItemStack border = new ItemBuilder(Material.IRON_FENCE).name("&eGranica")
                .lore("&7- Poczatkowy promien: &e" + (int)GameConfig.INITIAL_BORDER + " &7&o(LPM)").lore(
                        "&7- Rozpoczecie skracania: &e" + GameConfig.BORDER_SHRINK_START + "min &7&o(PPM)").lore(
                        "&7- Koniec skracania (50x50): &e" + GameConfig.BORDER_SHRINK_END + "min &7&o(SCROLL)").build();
        ItemStack players = new ItemBuilder(Material.getMaterial(397)).data(3).name("&eMinimum graczy")
                .lore("&7- Wymagana ilosc graczy do startu: &e" + GameConfig.REQUIRED_PLAYERS).lore("  ")
                  .lore("&7&o(Kliknij LPM/PPM aby zwiekszyc/zmniejszyc)").build();
        ItemStack staffmode = new ItemBuilder(Material.getMaterial(347)).data(3).name("&eTryb administratora")
                .lore("&7- Tryb administratora po rozpoczeciu: " + (GameConfig.INITIAL_STAFF_MODE.contains(uhcUser) ? "&aTAK" : "&cNIE") ).build();
        ItemStack cutclean = new ItemBuilder(Material.IRON_INGOT).data(3).name("&eAutomatyczne przepalanie rud")
                .lore("&7- Tryb: " + (GameConfig.CUTCLEAN_ENABLED ? "&aWlaczone" : "&cWylaczone")).build();

        this.inventory.setItem(0, protection);
        this.inventory.setItem(1, border);
        this.inventory.setItem(2, players);
        this.inventory.setItem(3, staffmode);
        this.inventory.setItem(4, cutclean);

        player.openInventory(this.inventory);
    }


    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player)e.getWhoClicked();
        UHCPlayer uhcUser = UserEngine.getInstance().getUser(player);
        Inventory inventory = e.getClickedInventory();

        if(e.getClickedInventory() == null) return;
        if(!inventory.getTitle().equalsIgnoreCase(ChatUtil.fixColor(this.title))) return;

        e.setCancelled(true);

        switch (e.getSlot()) {
            case 0 -> {
                GameConfig.increasePVPProtection();
                refreshInventory(player);
            }
            case 1 -> {
                switch (e.getClick()) {
                    case LEFT -> {
                        WorldManager worldManager = WorldManager.getInstance();
                        if (worldManager.isLoading() || worldManager.isFinishedLoading()) {
                            player.sendMessage(ChatUtil.fixColor("&4Blad: &cNie juz zmienic rozmiaru granicy"));
                            break;
                        }
                        GameConfig.increaseInitialBorder();
                        refreshInventory(player);
                    }
                    case RIGHT -> {
                        GameConfig.increaseBorderShrinkStart();
                        refreshInventory(player);
                    }
                    case MIDDLE -> {
                        GameConfig.increaseBorderShrinkEnd();
                        refreshInventory(player);
                    }
                }
            }
            case 2 -> {
                if (e.getClick() == ClickType.LEFT) GameConfig.increaseRequiredPlayers();
                else GameConfig.decreaseRequiredPlayers();
                refreshInventory(player);
            }
            case 3 -> {
                if (GameConfig.INITIAL_STAFF_MODE.contains(uhcUser)) GameConfig.INITIAL_STAFF_MODE.remove(uhcUser);
                else GameConfig.INITIAL_STAFF_MODE.add(uhcUser);
                refreshInventory(player);
            }
            case 4 -> {
                GameConfig.CUTCLEAN_ENABLED = !GameConfig.CUTCLEAN_ENABLED;
                refreshInventory(player);
            }
        }
    }


    private void refreshInventory(Player player){
        this.open(player);
    }

}
