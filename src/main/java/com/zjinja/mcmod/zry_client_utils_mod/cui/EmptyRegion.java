package com.zjinja.mcmod.zry_client_utils_mod.cui;

import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RenderContext;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;

public class EmptyRegion implements IRegion {
    @Override
    public String describe() {
        return "empty";
    }

    @Override
    public CUIRegionManager.RegionType getType() {
        return CUIRegionManager.RegionType.EMPTY;
    }

    @Override
    public void updatePoint(int id, double x, double y, double z) {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                "wecui/update", "updatePoint not supported for '{}'", this.describe()
        );
    }

    @Override
    public void updatePolygonPoint(int id, int x, int z) {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                "wecui/update", "updatePolygonPoint not supported for '{}'", this.describe()
        );
    }

    @Override
    public void updateEllipsoidCenter(int x, int y, int z) {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                "wecui/update", "updateEllipsoidCenter not supported for '{}'", this.describe()
        );
    }

    @Override
    public void updateEllipsoidRadius(double x, double y, double z) {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                "wecui/update", "updateEllipsoidRadius not supported for '{}'", this.describe()
        );
    }

    @Override
    public void updateCylinder(int x, int y, int z, double rx, double rz) {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                "wecui/update", "updateCylinder not supported for '{}'", this.describe()
        );
    }

    @Override
    public void updateMinMax(int min, int max) {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                "wecui/update", "updateMinMax not supported for '{}'", this.describe()
        );
    }

    @Override
    public void updatePolygon(int[] vid) {
        ZLogUtil.log(
                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                "wecui/update", "updatePolygon not supported for '{}'", this.describe()
        );
    }

    @Override
    public void render(RenderContext rctx, boolean mainSelection) {
    }
}
