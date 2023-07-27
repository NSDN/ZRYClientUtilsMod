package com.zjinja.mcmod.zry_client_utils_mod.cui;

public class PolygonRegion extends EmptyRegion {
    @Override
    public CUIRegionManager.RegionType getType() {
        return CUIRegionManager.RegionType.POLYGON;
    }

    @Override
    public String describe() {
        return "polygon <can not describe it yet>";
    }
}
