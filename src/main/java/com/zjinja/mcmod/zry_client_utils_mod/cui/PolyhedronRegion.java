package com.zjinja.mcmod.zry_client_utils_mod.cui;

public class PolyhedronRegion extends EmptyRegion{
    @Override
    public CUIRegionManager.RegionType getType() {
        return CUIRegionManager.RegionType.POLYHEDRON;
    }

    @Override
    public String describe() {
        return "polyhedron <can not describe it yet>";
    }
}
