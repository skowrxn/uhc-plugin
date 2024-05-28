package pl.skowron.uhc.world;



import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import pl.skowron.uhc.UHCPlugin;
import pl.skowron.uhc.configuration.GameConfig;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.util.ChatUtil;
import pl.skowron.uhc.util.TitleAPI;

import java.io.File;
import java.util.Arrays;

public class WorldManager {

    private WorldGenerator worldGenerator;
    private boolean loading = false;
    private boolean finishedLoading = false;
    private int taskId;
    private int chunks;

    private static WorldManager instance;

    public WorldManager() { instance = this; }


    public void deleteWorld(){
        File deleteFolder = this.getWorldGenerator().getWorld().getWorldFolder();
        Bukkit.unloadWorld(this.getWorldGenerator().getWorld().getName(), false);
    }

    public void loadWorld(double border){
        Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.WORLD_CREATING_MESSAGE));

        this.loading = true;
        this.worldGenerator = new WorldGenerator(GameConfig.INITIAL_BORDER);

        TitleAPI.sendActionBarToAllPlayers(Messages.WORLD_LOADING_ACTIONBAR.replace("%progress",  "0%" ));

        WorldLoaderTask worldLoader = new WorldLoaderTask(this.getWorldGenerator().getWorld(), border, 1, 1);
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(UHCPlugin.getInstance(), worldLoader,1, 1);
        worldLoader.setTaskID(taskId);

    }

    public void handleWorldLoad(){

        this.finishedLoading = true;
        this.loading = false;

        Bukkit.broadcastMessage(ChatUtil.fixColor(Messages.WORLD_LOADING_FINISHED_MESSAGE));
        TitleAPI.sendActionBarToAllPlayers(Messages.WORLD_LOADING_ACTIONBAR.replace("%progress",  "100%" ));
        Bukkit.getScheduler().cancelTask(this.taskId);

        this.replaceTreesNearCentre(this.getWorldGenerator().getWorld());

    }

    public void replaceTrees(Chunk chunk){

        for (int x = 0; x < 16; x++){
            for (int y = 0; y < 256; y++){
                for(int z = 0; z <16; z++){
                    Block block = chunk.getBlock(x, y, z);
                    if (Arrays.asList(Material.getMaterial(17), Material.getMaterial(162)).contains(block.getType())){
                        block.setType(Material.AIR);
                        if(block.getRelative(BlockFace.DOWN).getType().equals(Material.DIRT)){
                            block.getRelative(BlockFace.DOWN).setType(Material.GRASS);
                        }
                    } else if (block.getType().equals(Material.getMaterial(18)) || block.getType().equals(Material.getMaterial(161))){
                        block.setType(Material.AIR);
                    }

                }
            }
        }

    }

    public void replaceTreesNearCentre(World world){
        for(int z = -150; z < 150; z += 16){
            for(int x = -150; x < 150; x += 16){
                Chunk chunk = world.getChunkAt(new Location(world, x, 1, z));
                replaceTrees(chunk);
            }
        }
    }

    public static int getAvailableMemory() {
        return (int)((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1048576L);
    }

    public static boolean isTooLowMemory() {
        return getAvailableMemory() < 250;
    }

    public WorldGenerator getWorldGenerator() {
        return worldGenerator;
    }

    public boolean isLoading() { return this.loading; }
    public boolean isFinishedLoading() { return this.finishedLoading; }
    public int getTaskId() { return this.taskId; }
    public void setLoading(boolean loading) { this.loading = loading; }
    public void setFinishedLoading(boolean finishedLoading) { this.finishedLoading = finishedLoading; }
    public static WorldManager getInstance() { return instance; }

}
