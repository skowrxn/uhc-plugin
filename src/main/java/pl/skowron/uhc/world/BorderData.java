package pl.skowron.uhc.world;

import org.bukkit.Location;

public class BorderData {

  private double x = 0.0D;
  private double z = 0.0D;
  
  private int radiusX = 0;
  private int radiusZ = 0;

  private Boolean shapeRound = null;
  private boolean wrapping = false;
  
  private double maxX;
  private double minX;
  private double maxZ;
  private double minZ;
  
  private double radiusXSquared;
  private double radiusZSquared;
  
  private double DefiniteRectangleX;
  private double DefiniteRectangleZ;

  private double radiusSquaredQuotient;

  public BorderData(double x, double z, int radiusX, int radiusZ, Boolean shapeRound, boolean wrap) {
    setData(x, z, radiusX, radiusZ, shapeRound, wrap);
  }

  public BorderData(double x, double z, int radiusX, int radiusZ) {
    setData(x, z, radiusX, radiusZ, null);
  }

  public BorderData(double x, double z, int radiusX, int radiusZ, Boolean shapeRound) {
      setData(x, z, radiusX, radiusZ, shapeRound);
  }

  public BorderData(double x, double z, int radius) {
    setData(x, z, radius, null);
  }

  public BorderData(double x, double z, int radius, Boolean shapeRound) {
    setData(x, z, radius, shapeRound);
  }

  public final void setData(double x, double z, int radiusX, int radiusZ, Boolean shapeRound, boolean wrap) {
    
      this.x = x;
      this.z = z;
      
      this.shapeRound = shapeRound;
      this.wrapping = wrap;
      
      setRadiusX(radiusX);
      setRadiusZ(radiusZ);
      
  }

  public final void setData(double x, double z, int radiusX, int radiusZ, Boolean shapeRound) {
      setData(x, z, radiusX, radiusZ, shapeRound, false);
  }

  public final void setData(double x, double z, int radius, Boolean shapeRound) {
      setData(x, z, radius, radius, shapeRound, false);
  }

  public BorderData copy() {
      return new BorderData(this.x, this.z, this.radiusX, this.radiusZ, this.shapeRound, this.wrapping);
  }

  public double getX() {
    return this.x;
  }

  public void setX(double x) {
      this.x = x;
      this.maxX = x + this.radiusX;
      this.minX = x - this.radiusX;
  }

  public double getZ() {
    return this.z;
  }

  public void setZ(double z) {
      this.z = z;
      this.maxZ = z + this.radiusZ;
      this.minZ = z - this.radiusZ;
  }

  public int getRadiusX() {
    return this.radiusX;
  }

  public int getRadiusZ() {
    return this.radiusZ;
  }

  public void setRadiusX(int radiusX) {
      this.radiusX = radiusX;
      this.maxX = this.x + radiusX;
      this.minX = this.x - radiusX;
      this.radiusXSquared = radiusX * radiusX;
      this.radiusSquaredQuotient = this.radiusXSquared / this.radiusZSquared;
      this.DefiniteRectangleX = Math.sqrt(0.5D * this.radiusXSquared);
  }

  public void setRadiusZ(int radiusZ) {
      this.radiusZ = radiusZ;
      this.maxZ = this.z + radiusZ;
      this.minZ = this.z - radiusZ;
      this.radiusZSquared = radiusZ * radiusZ;
      this.radiusSquaredQuotient = this.radiusXSquared / this.radiusZSquared;
      this.DefiniteRectangleZ = Math.sqrt(0.5D * this.radiusZSquared);
  }

  public int getRadius() {
    return (this.radiusX + this.radiusZ) / 2;
  }

  public void setRadius(int radius) {
      setRadiusX(radius);
      setRadiusZ(radius);
  }

  public boolean insideBorder(double xLoc, double zLoc, boolean round) {
    
      if (this.shapeRound != null) round = this.shapeRound;
      if (!round) return (xLoc >= this.minX && xLoc <= this.maxX && zLoc >= this.minZ && zLoc <= this.maxZ);
      
      double X = Math.abs(this.x - xLoc);
      double Z = Math.abs(this.z - zLoc);

      if (X < this.DefiniteRectangleX && Z < this.DefiniteRectangleZ) return true;
      if (X >= this.radiusX || Z >= this.radiusZ) return false;

      return X * X + Z * Z * this.radiusSquaredQuotient < this.radiusXSquared;

  }

  public boolean insideBorder(double xLoc, double zLoc) {
    return insideBorder(xLoc, zLoc, true);
  }

  public boolean insideBorder(Location loc) {
    return insideBorder(loc.getX(), loc.getZ(), true);
  }

  public boolean insideBorder(CoordXZ coord, boolean round) {
    return insideBorder(coord.x, coord.z, round);
  }

  public boolean insideBorder(CoordXZ coord) {
    return insideBorder(coord.x, coord.z, true);
  }


}
