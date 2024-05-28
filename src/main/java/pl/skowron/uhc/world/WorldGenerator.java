package pl.skowron.uhc.world;

import com.pieterdebot.biomemapping.Biome;
import com.pieterdebot.biomemapping.BiomeMappingAPI;
import org.bukkit.*;

public class WorldGenerator {

    private World world;
    private final int chunks = 1;

    public WorldGenerator(double border){
        this.replaceOceans();
        this.createWorld();
        this.applySettings(border);
    }

    private void createWorld(){
        WorldCreator wc = new WorldCreator("uhc");

        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);

        this.world = wc.createWorld();
    }

    private void replaceOceans(){

        BiomeMappingAPI biomeMapping = new BiomeMappingAPI();
        Biome replacementBiome = Biome.PLAINS;

        for (Biome biome : Biome.values()){
            if ((biome.isOcean() || biome.name().toLowerCase().contains("mountain") || biome.name().toLowerCase().contains("plateau")
                    || biome.name().toLowerCase().contains("hills")) && biomeMapping.biomeSupported(biome)){
                try {
                    biomeMapping.replaceBiomes(biome, replacementBiome);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                replacementBiome = replacementBiome == Biome.PLAINS ? Biome.FOREST : Biome.PLAINS;
            }
        }

    }

    public void applySettings(double border){
        world.setDifficulty(Difficulty.NORMAL);
        world.setPVP(false);
        world.setTime(1000);
        world.setStorm(false);
    }

    public World getWorld() { return this.world; }




}
