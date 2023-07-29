package com.zjinja.mcmod.zry_client_utils_mod.cui;

import com.mojang.brigadier.Command;
import com.mojang.logging.LogUtils;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RenderContext;
import com.zjinja.mcmod.zry_client_utils_mod.utils.ZLogUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.UUID;

public class CUIRegionManager {
    private static final CUIRegionManager INSTANCE = new CUIRegionManager();

    private IRegion selRegion = new EmptyRegion();
    private HashMap<UUID, IRegion> regions = new HashMap<>();
    private IRegion activeRegion = new EmptyRegion();

    public enum RegionType {
        EMPTY,
        CUBOID,
        POLYGON,
        ELLIPSOID,
        CYLINDER,
        POLYHEDRON,
    }

    public static CUIRegionManager getInstance() {
        return CUIRegionManager.INSTANCE;
    }

    public CUIRegionManager() {
    }

    public void clearSelection(){
        this.selRegion = new EmptyRegion();
    }

    public void clearRegions()
    {
        this.activeRegion = new EmptyRegion();
        this.regions.clear();
    }

    public void setRegion(UUID id, IRegion region)
    {
        if (id == null)
        {
            this.selRegion = region;
            return;
        }

        if (region == null)
        {
            this.regions.remove(id);
            this.activeRegion = new EmptyRegion();
            return;
        }

        this.regions.put(id, region);
        this.activeRegion = region;
    }

    public void update(boolean multi, String type, String[] param) {
        switch (type) {
            default -> {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "wecui/process-pkg", "unknown msg type '{}'", type
                );
            }
            case "s" -> {
                if(!checkArgsCount(param, 1, 2, type)) return;
                IRegion r = createRegionByTypeName(param[0]);
                UUID id = null;
                if (multi)
                {
                    if (r == null && param.length < 2)
                    {
                        ZLogUtil.log(
                                LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                                "wecui/process-pkg", "clear selection event."
                        );
                        this.clearRegions();
                        return;
                    }
                    if (param.length < 2) {
                        ZLogUtil.log(
                                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                                "wecui/process-pkg", "invalid args for msg type '{}'.", type
                        );
                        return;
                    }
                    id = UUID.fromString(param[1]);
                }
                this.setRegion(id, r);
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                        "wecui/process-pkg", "new region event: id={}", id
                );
            }
            case "p" -> {
                if(!checkArgsCount(param, 5, 6, type)) return;
                IRegion r = this.getSelection(multi);
                if (r == null)
                {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                            "wecui/process-pkg", "for event '{}', no selection exists now.", type
                    );
                    return;
                }
                int id = Integer.parseUnsignedInt(param[0]);
                if (
                        multi &&
                                param[1].equals("~") &&
                                param[2].equals("~") &&
                                param[3].equals("~")

                ){
                    var player = Minecraft.getInstance().player;
                    if (player != null){
                        player.sendSystemMessage(Component.translatable("chat.tip.not_support_this_wecui_type"));
                        player.sendSystemMessage(Component.translatable("chat.tip.not_support_l2"));
                    }else {
                        ZLogUtil.log(
                                LogUtils.getLogger(), ZLogUtil.Level.WARN,
                                "wecui/process-pkg", "player is null"
                        );
                    }
                    this.clearRegions();
                    this.clearSelection();
                    return;
                }
                double x = Double.parseDouble(param[1]);
                double y = Double.parseDouble(param[2]);
                double z = Double.parseDouble(param[3]);
                r.updatePoint(id, x, y, z);
            }
            case "p2" -> {
                if(!checkArgsCount(param, 4, 5, type)) return;
                IRegion r = this.getSelection(multi);
                if (r == null)
                {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                            "wecui/process-pkg", "for event '{}', no selection exists now.", type
                    );
                    return;
                }
                int id = Integer.parseUnsignedInt(param[0]);
                int x = Integer.parseInt(param[1]);
                int z = Integer.parseInt(param[2]);
                r.updatePolygonPoint(id, x,  z);
            }
            case "e" -> {
                if(!checkArgsCount(param, 4, 4, type)) return;
                IRegion r = this.getSelection(multi);
                if (r == null)
                {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                            "wecui/process-pkg", "for event '{}', no selection exists now.", type
                    );
                    return;
                }
                int id = Integer.parseUnsignedInt(param[0]);
                switch (id) {
                    case 0 -> {
                        int x = Integer.parseInt(param[1]);
                        int y = Integer.parseInt(param[2]);
                        int z = Integer.parseInt(param[3]);
                        r.updateEllipsoidCenter(x, y, z);
                    }
                    case 1 -> {
                        double x = Double.parseDouble(param[1]);
                        double y = Double.parseDouble(param[2]);
                        double z = Double.parseDouble(param[3]);
                        r.updateEllipsoidRadius(x, y, z);
                    }
                    default -> {
                        ZLogUtil.log(
                                LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                                "wecui/process-pkg", "for event '{}', invalid id {}.", type, id
                        );
                    }
                }
            }

            case "cyl" -> {
                if(!checkArgsCount(param, 5, 5, type)) return;
                IRegion r = this.getSelection(multi);
                if (r == null)
                {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                            "wecui/process-pkg", "for event '{}', no selection exists now.", type
                    );
                    return;
                }
                int x = Integer.parseInt(param[0]);
                int y = Integer.parseInt(param[1]);
                int z = Integer.parseInt(param[2]);
                double rx = Double.parseDouble(param[3]);
                double rz = Double.parseDouble(param[4]);
                r.updateCylinder(x, y, z, rx, rz);
            }
            case "mm" -> {
                if(!checkArgsCount(param, 2, 2, type)) return;
                IRegion r = this.getSelection(multi);
                if (r == null)
                {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                            "wecui/process-pkg", "for event '{}', no selection exists now.", type
                    );
                    return;
                }
                int min = Integer.parseInt(param[0]);
                int max = Integer.parseInt(param[1]);
                r.updateMinMax(min, max);
            }
            case "u" -> {
                if(!checkArgsCount(param, 1, 1, type)) return;
            }
            case "poly" -> {
                if(!checkArgsCount(param, 3, 99, type)) return;
                IRegion r = this.getSelection(multi);
                if (r == null)
                {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                            "wecui/process-pkg", "for event '{}', no selection exists now.", type
                    );
                    return;
                }
                final int[] vertexIds = new int[param.length];
                for (int i = 0; i < param.length; ++i)
                {
                    vertexIds[i] = Integer.parseInt(param[i]);
                }
                r.updatePolygon(vertexIds);
            }
            case "col" -> {
                if(!checkArgsCount(param, 4, 4, type)) return;
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                        "wecui/process-pkg", "event '{}' not supported yet.", type
                );
            }
            case "grid" -> {
                if(!checkArgsCount(param, 1, 2, type)) return;
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.DEBUG,
                        "wecui/process-pkg", "event '{}' not supported yet.", type
                );
            }
        }
    }

    private IRegion createRegionByTypeName(String typename) {
        switch (typename) {
            default -> {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "wecui/process-pkg", "unknown region type '{}'", typename
                );
                return null;
            }
            case "clear" -> {
                return null;
            }
            case "cuboid" -> {
                return new CuboidRegion();
            }
            case "ellipsoid" -> {
                return new EllipsoidRegion();
            }
            case "cylinder" -> {
                return new CylinderRegion();
            }
            case "polygon2d" -> {
                var player = Minecraft.getInstance().player;
                if (player != null){
                    player.sendSystemMessage(Component.translatable("chat.tip.not_support_polygon_yet"));
                    player.sendSystemMessage(Component.translatable("chat.tip.not_support_l2"));
                }else {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.WARN,
                            "wecui/process-pkg", "player is null"
                    );
                }
                return new PolygonRegion();
            }
            case "polyhedron" -> {
                var player = Minecraft.getInstance().player;
                if (player != null){
                    player.sendSystemMessage(Component.translatable("chat.tip.not_support_polyhedron_yet"));
                    player.sendSystemMessage(Component.translatable("chat.tip.not_support_l2"));
                }else {
                    ZLogUtil.log(
                            LogUtils.getLogger(), ZLogUtil.Level.WARN,
                            "wecui/process-pkg", "player is null"
                    );
                }
                return new PolygonRegion();
            }
        }
    }

    public IRegion getSelection(boolean multi)
    {
        return multi ? this.activeRegion : this.selRegion;
    }

    public String describeSelection() {
        if(this.selRegion == null) {
            return "empty";
        }
        return this.selRegion.describe();
    }

    private boolean checkArgsCount(String[] params, int min, int max, String mtype) {
        if (max == min)
        {
            if (params.length != max)
            {
                ZLogUtil.log(
                        LogUtils.getLogger(), ZLogUtil.Level.WARN,
                        "wecui/process-pkg", "invalid args count {} of msg type '{}'",
                        params.length, mtype
                );
                return false;
            }
        }
        if (params.length > max || params.length < min)
        {
            ZLogUtil.log(
                    LogUtils.getLogger(), ZLogUtil.Level.WARN,
                    "wecui/process-pkg", "invalid args count {} of msg type '{}'",
                    params.length, mtype
            );
            return false;
        }else{
            return true;
        }
    }

    public static void render(RenderContext rctx) {
        var inst = getInstance();
        if (inst != null) {
            if (inst.regions != null) {
                for (IRegion ir : inst.regions.values()) {
                    ir.render(rctx, false);
                }
            }
            if(inst.selRegion != null) {
                inst.selRegion.render(rctx, true);
            }
        }
    }
}
