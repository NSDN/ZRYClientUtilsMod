package com.zjinja.mcmod.zry_client_utils_mod.marker;

import com.zjinja.mcmod.zry_client_utils_mod.cui.CUIRegionManager;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RGBA;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RenderContext;
import com.zjinja.mcmod.zry_client_utils_mod.renderer.RenderUtils;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

public class MarkerMgr {
    public final static MarkerMgr INSTANCE = new MarkerMgr();


    public static MarkerMgr getInstance() {
        return INSTANCE;
    }

    private final ArrayList<AABB> markerList = new ArrayList<>();

    public MarkerMgr() {

    }

    public void clearMarkerList() {
        this.markerList.clear();
    }

    public void addPointMarker(int x, int y, int z){
        AABB marker = new AABB(x, y, z, x+1, y+1, z+1);
        this.markerList.add(marker);
    }

    public void addRangeMarker(int x1, int y1, int z1, int x2, int y2, int z2){
        AABB marker = new AABB(x1, y1, z1, x2, y2, z2);
        this.markerList.add(marker);
    }

    public boolean addRangeFromWE() {
        var crm = CUIRegionManager.getInstance();
        if(crm == null) {
            return false;
        }
        var aabb = crm.getSelectionAABB();
        if(aabb.isEmpty()) {
            return false;
        }
        this.markerList.add(aabb.get());
        return true;
    }

    public static void render(RenderContext rctx){
        var inst = getInstance();
        if (inst != null) {
            for(AABB i: inst.markerList) {
                RenderUtils.drawOutlineBox(rctx, i, new RGBA(0.8476F, 0.6367F, 0.2617F, 1.0F));
            }
        }
    }

}
