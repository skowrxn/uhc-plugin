package pl.skowron.uhc.world;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import pl.skowron.uhc.configuration.Messages;
import pl.skowron.uhc.util.TitleAPI;

public class WorldLoaderTask implements Runnable {

    private final transient World world;
    private final transient BorderData border;
    private transient boolean readyToGo;
    private transient boolean pausedForMemory = false;
    private transient int taskID = -1;
    private final transient int chunksPerRun;
    private transient boolean continueNotice = false;

    private transient int lastLegX = 0;
    private transient int lastLegZ = 0;
    private transient int lastLegTotal = 0;

    private transient int x;
    private transient int z;
    private transient boolean isZLeg = false;
    private transient boolean isNeg = false;
    private transient int length = -1;
    private transient int current = 0;
    private transient boolean insideBorder = true;

    private final List<CoordXZ> storedChunks = new LinkedList<>();
    private final Set<CoordXZ> originalChunks = new HashSet<>();

    private final transient CoordXZ lastChunk = new CoordXZ(0, 0);

    private transient long lastReport = System.currentTimeMillis();
    private final transient int chunksTotal;
    private transient int chunksCompleted = 0;
    private transient int chunksCompletedFixed = 0;
    private transient int completedLast = 0;

    public WorldLoaderTask(World world, double radius,  int chunksPerRun, int tickFrequency) {

        this.chunksPerRun = chunksPerRun;
        this.world = world;
        this.border = new BorderData(0, 0, (int) radius);

        this.border.setRadiusX((int)radius + 50);
        this.border.setRadiusZ((int)radius + 50);

        this.x = CoordXZ.blockToChunk((int)this.border.getX());
        this.z = CoordXZ.blockToChunk((int)this.border.getZ());

        int chunkWidthX = (int)Math.ceil(((this.border.getRadiusX() + 16) * 2) / 16.0D);
        int chunkWidthZ = (int)Math.ceil(((this.border.getRadiusZ() + 16) * 2) / 16.0D);
        int biggerWidth = Math.max(chunkWidthX, chunkWidthZ);

        this.chunksTotal = (int) (0.745 * (biggerWidth * biggerWidth + biggerWidth + 1));

        Chunk[] originals = this.world.getLoadedChunks();
        for (Chunk original : originals) this.originalChunks.add(new CoordXZ(original.getX(), original.getZ()));

        this.readyToGo = true;

    }

    public void setTaskID(int ID) {
        this.taskID = ID;
    }

    @Override
    public void run() {

        if (this.continueNotice) { this.continueNotice = false; }

        if (this.pausedForMemory) {
            if (WorldManager.isTooLowMemory())
               return;
            this.pausedForMemory = false;
            this.readyToGo = true;
            Bukkit.getLogger().info("Wyczyszczono pamiec ram, trwa kontynuacja ladowania swiata...");
        }

        if (!this.readyToGo) return;

        this.readyToGo = false;
        long loopStartTime = System.currentTimeMillis();

        for (int loop = 0; loop < this.chunksPerRun; loop++) {

          if (this.pausedForMemory) return;

          long now = System.currentTimeMillis();

          if (now > this.lastReport + 2000L) reportProgress();

          if (now > loopStartTime + 45L) {
              this.readyToGo = true;
              return;
          }

          while (!this.border.insideBorder((CoordXZ.chunkToBlock(this.x) + 8), (CoordXZ.chunkToBlock(this.z) + 8))) {
              if (!moveToNext()) return;
          }

          this.insideBorder = true;
          this.world.loadChunk(this.x, this.z, true);

          int popX = !this.isZLeg ? this.x : (this.x + (this.isNeg ? -1 : 1));
          int popZ = this.isZLeg ? this.z : (this.z + (!this.isNeg ? -1 : 1));

          this.world.loadChunk(popX, popZ, false);

          if (!this.storedChunks.contains(this.lastChunk) && !this.originalChunks.contains(this.lastChunk)) {
              this.chunksCompletedFixed++;
              this.world.loadChunk(this.lastChunk.x, this.lastChunk.z, false);
              this.storedChunks.add(new CoordXZ(this.lastChunk.x, this.lastChunk.z));
          }

          this.storedChunks.add(new CoordXZ(popX, popZ));
          this.storedChunks.add(new CoordXZ(this.x, this.z));

          while (this.storedChunks.size() > 8) {
              CoordXZ coord = this.storedChunks.remove(0);
              if (!this.originalChunks.contains(coord))
                this.world.unloadChunkRequest(coord.x, coord.z);
          }

          if (!moveToNext())
              return;

        }

        this.readyToGo = true;

    }

    public boolean moveToNext() {
      if (this.pausedForMemory) return false;

      this.completedLast++;
      if (!this.isNeg && this.current == 0 && this.length > 3) {
          if (!this.isZLeg) {
            this.lastLegX = this.x;
            this.lastLegZ = this.z;
            this.lastLegTotal = this.chunksCompleted + this.completedLast;
          } else {
              int refX = this.lastLegX;
              int refZ = this.lastLegZ;
              int refTotal = this.lastLegTotal;
              int refLength = this.length - 1;
          }
      }

      if (this.current < this.length) {
          this.current++;
        } else {
          this.current = 0;
          this.isZLeg ^= true;
          if (this.isZLeg) {
            this.isNeg ^= true;
            this.length++;
          }
      }

      this.lastChunk.x = this.x;
      this.lastChunk.z = this.z;

      if (this.isZLeg) {
         this.z += this.isNeg ? -1 : 1;
      } else {
         this.x += this.isNeg ? -1 : 1;
      }

      if (this.isZLeg && this.isNeg && this.current == 0) {
        if (!this.insideBorder) {
          finish();
          return false;
        }
        this.insideBorder = false;
      }

      return true;


    }

    public void finish() {
        this.pausedForMemory = true;
        reportProgress();
        this.world.save();
        WorldManager.getInstance().handleWorldLoad();
        stop();
    }

    private void stop() {

        this.readyToGo = false;

        if (this.taskID != -1) Bukkit.getScheduler().cancelTask(this.taskID);

        while (!this.storedChunks.isEmpty()) {
          CoordXZ coord = this.storedChunks.remove(0);
          if (!this.originalChunks.contains(coord))
            this.world.unloadChunkRequest(coord.x, coord.z);
        }

    }

    public void pause() {

        if (this.pausedForMemory) {
          pause(false);
        }

    }

    public void pause(boolean pause) {

        if (this.pausedForMemory && !pause) {
          this.pausedForMemory = false;
        }

    }

    public boolean isPaused() {
       return this.pausedForMemory;
    }

    private void reportProgress() {

        this.lastReport = System.currentTimeMillis();

        double perc = getPercentageCompleted();
        if (perc > 100.0D) perc = 100.0D;

        Bukkit.getLogger().info(this.completedLast + "  (" + (this.chunksCompleted + this.completedLast) + ", ~" + new DecimalFormat("0.0").format(perc) + "%" + ")");
        TitleAPI.sendActionBarToAllPlayers(Messages.WORLD_LOADING_ACTIONBAR.replace("%progress",  new DecimalFormat("0.0").format(perc) + "%" ));

        this.chunksCompleted += this.completedLast;
        this.completedLast = 0;

    }


    public void continueProgress(int x, int z, int length, int totalDone) {
        this.x = x;
        this.z = z;
        this.length = length;
        this.chunksCompleted = totalDone;
        this.continueNotice = true;
    }

    public String refWorld() {
       return this.world.getName();
    }

    public double getPercentageCompleted() {
        return ((double)this.chunksCompletedFixed) / this.chunksTotal * 100.0D;
    }

}
