package pl.skowron.uhc.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import pl.skowron.uhc.UHCPlugin;
import pl.skowron.uhc.configuration.ConfigurationFile;

import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {

    public static Location getSpawnLocation() {
        String worldName = ConfigurationFile.SPAWN_WORLD;
        World world = Bukkit.getWorld(worldName) == null ? Bukkit.getWorlds().get(0) : Bukkit.getWorld(worldName);

        double x = ConfigurationFile.SPAWN_X;
        double y = ConfigurationFile.SPAWN_Y;
        double z = ConfigurationFile.SPAWN_Z;

        double yaw = ConfigurationFile.SPAWN_YAW;
        double pitch = ConfigurationFile.SPAWN_PITCH;

        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public static void updateSpawnLocation(Location location){
        String world = location.getWorld().getName();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        float pitch = location.getPitch();
        float yaw = location.getYaw();

        ConfigurationFile.SPAWN_WORLD = world;
        ConfigurationFile.SPAWN_X = x;
        ConfigurationFile.SPAWN_Y = y;
        ConfigurationFile.SPAWN_Z = z;
        ConfigurationFile.SPAWN_PITCH = pitch;
        ConfigurationFile.SPAWN_YAW = yaw;

        Bukkit.getScheduler().runTaskAsynchronously(UHCPlugin.getInstance(), ConfigurationFile::saveLocationToConfig);
    }

    public static Location getRandomLocation(World world, int radius){
        int x = randomCoordinate(radius);
        int z = randomCoordinate(radius);

        Bukkit.getLogger().info("RADIUS: " + radius);

        return new Location(world, x, world.getHighestBlockYAt(x, z), z);
    }

    static int randomCoordinate(int radius){
        return ThreadLocalRandom.current().nextInt(-radius, radius + 1);
    }






}
