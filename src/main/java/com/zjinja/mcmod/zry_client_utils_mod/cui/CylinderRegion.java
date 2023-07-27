package com.zjinja.mcmod.zry_client_utils_mod.cui;

import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class CylinderRegion extends EmptyRegion {
    private Optional<Vec3> center;
    private double rX, rZ, minY, maxY;

    public CylinderRegion(){
        center = Optional.empty();
        rX = 0;
        rZ = 0;
        minY = 0;
        maxY = 0;
    }

    @Override
    public String describe() {
        String p1;
        p1 = center.map(Vec3::toString).orElse("null");
        return String.format("cylinder c=%s rx=%f rz=%f, min-y=%f, max-y=%f", p1, rX, rZ, minY, maxY);
    }

    @Override
    public CUIRegionManager.RegionType getType() {
        return CUIRegionManager.RegionType.CYLINDER;
    }

    @Override
    public void updateCylinder(int x, int y, int z, double rx, double rz){
        this.center = Optional.of(new Vec3(x, y, z));
        this.rX = rx;
        this.rZ = rz;
    }

    @Override
    public void updateMinMax(int min, int max) {
        minY = min;
        maxY = max;
    }
}
