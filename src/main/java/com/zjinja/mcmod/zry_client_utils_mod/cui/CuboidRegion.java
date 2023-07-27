package com.zjinja.mcmod.zry_client_utils_mod.cui;

import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class CuboidRegion extends EmptyRegion {
    private Optional<Vec3> point1;
    private Optional<Vec3> point2;

    public CuboidRegion(){
        point1 = Optional.empty();
        point2 = Optional.empty();
    }

    @Override
    public String describe() {
        String p1,p2;
        p1 = point1.map(Vec3::toString).orElse("null");
        p2 = point2.map(Vec3::toString).orElse("null");
        return String.format("cuboid %s - %s", p1, p2);
    }

    @Override
    public CUIRegionManager.RegionType getType() {
        return CUIRegionManager.RegionType.CUBOID;
    }

    @Override
    public void updatePoint(int id, double x, double y, double z) {
        switch (id){
            case 0 -> {
                point1 = Optional.of(new Vec3(x, y, z));
            }
            case 1 -> {
                point2 = Optional.of(new Vec3(x, y, z));
            }
            default -> {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "wecui/cuboid-region/update-point", "id {} out of range.", id
                );
            }
        }
    }
}
