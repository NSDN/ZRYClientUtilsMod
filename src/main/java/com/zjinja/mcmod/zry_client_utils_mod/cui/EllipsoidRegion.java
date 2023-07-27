package com.zjinja.mcmod.zry_client_utils_mod.cui;

import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class EllipsoidRegion extends EmptyRegion{
    private Optional<Vec3> center;
    private Optional<Vec3> radius;

    public EllipsoidRegion() {
        center = Optional.empty();
        radius = Optional.empty();
    }

    @Override
    public String describe() {
        String p1,p2;
        p1 = center.map(Vec3::toString).orElse("null");
        p2 = radius.map(Vec3::toString).orElse("null");
        return String.format("ellipsoid c=%s rad=%s", p1, p2);
    }

    @Override
    public CUIRegionManager.RegionType getType() {
        return CUIRegionManager.RegionType.ELLIPSOID;
    }

    @Override
    public void updateEllipsoidCenter(int x, int y, int z) {
        center = Optional.of(new Vec3(x, y, z));
    }

    @Override
    public void updateEllipsoidRadius(double x, double y, double z) {
        radius = Optional.of(new Vec3(x, y, z));
    }
}
