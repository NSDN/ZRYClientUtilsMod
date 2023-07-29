package com.zjinja.mcmod.zry_client_utils_mod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.Vec3;

public class RenderContext {
    private final PoseStack poseStack;
    private final Vec3 camPos;
    private final Frustum frustum;

    public RenderContext(PoseStack ps, Frustum fr) {
        this.poseStack = ps;
        this.camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        this.frustum = fr;
    }

    public void pushPose() {
        poseStack.pushPose();
    }

    public void popPose() {
        poseStack.popPose();
    }

    public Vec3 getCameraPos() {
        return camPos;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public Frustum getFrustum() {
        return this.frustum;
    }
}
