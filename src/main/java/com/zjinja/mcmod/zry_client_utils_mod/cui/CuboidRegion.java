package com.zjinja.mcmod.zry_client_utils_mod.cui;

import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RGBA;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RenderContext;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RenderUtils;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.world.phys.AABB;
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

    @Override
    public void render(RenderContext rctx, boolean mainSelection) {
        if(this.point1.isEmpty() || this.point2.isEmpty()) {
            return;
        }
        Vec3 p1 = this.point1.get();
        Vec3 p2 = this.point2.get();
        double aX = Math.min(p1.x, p2.x);
        double bX = Math.max(p1.x, p2.x) + 1;
        double aY = Math.min(p1.y, p2.y);
        double bY = Math.max(p1.y, p2.y) + 1;
        double aZ = Math.min(p1.z, p2.z);
        double bZ = Math.max(p1.z, p2.z) + 1;
        AABB aabbMain = new AABB(aX, aY, aZ, bX, bY, bZ);
        AABB aabbFP = new AABB(p1.x, p1.y, p1.z, p1.x + 1, p1.y + 1, p1.z + 1);
        AABB aabbSP = new AABB(p2.x, p2.y, p2.z, p2.x + 1, p2.y + 1, p2.z + 1);
        RenderUtils.drawOutlineBox(rctx, aabbMain, new RGBA(0.0F, 0.0F, 1.0F, 1.0F));
        RenderUtils.drawOutlineBox(rctx, aabbFP, new RGBA(1.0F, 0.0F, 0.0F, 1.0F));
        RenderUtils.drawOutlineBox(rctx, aabbSP, new RGBA(0.0F, 1.0F, 0.0F, 1.0F));
    }
}
