package com.zjinja.mcmod.zry_client_utils_mod.cui;

public interface IRegion {
    public String describe();
    public CUIRegionManager.RegionType getType();
    public void updatePoint(int id, double x, double y, double z);
    public void updatePolygonPoint(int id, int x, int z);
    public void updateEllipsoidCenter(int x, int y, int z);
    public void updateEllipsoidRadius(double x, double y, double z);
    public void updateCylinder(int x, int y, int z, double rx, double rz);
    public void updateMinMax(int min, int max);
    public void updatePolygon(int[] vid);
}
