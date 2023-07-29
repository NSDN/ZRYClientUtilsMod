package com.zjinja.mcmod.zry_client_utils_mod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.lwjgl.opengl.GL11;

public class RenderUtils {
    public static void renderVoxelShape(PoseStack ps, VertexConsumer vc, VoxelShape vox,
                                         double refX, double refY, double refZ, float cR, float cG, float cB, float cA) {
        PoseStack.Pose last = ps.last();
        vox.forAllEdges((e1, e2, e3, e4, e5, e6) -> {
            float f = (float)(e4 - e1);
            float f1 = (float)(e5 - e2);
            float f2 = (float)(e6 - e3);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f = f / f3;
            f1 = f1 / f3;
            f2 = f2 / f3;
            vc.vertex(
                    last.pose(), (float)(e1 + refX), (float)(e2 + refY),
                    (float)(e3 + refZ)).color(cR, cG, cB, cA).normal(last.normal(),
                    f, f1, f2
            ).endVertex();
            vc.vertex(
                    last.pose(), (float)(e4 + refX), (float)(e5 + refY),
                    (float)(e6 + refZ)).color(cR, cG, cB, cA).normal(last.normal(),
                    f, f1, f2
            ).endVertex();
        });
    }

    public static void drawOutlineBox(RenderContext rctx, AABB aabb, RGBA color) {
        if(!rctx.getFrustum().isVisible(aabb)) return;
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(RenderType.lines());
        VoxelShape vsp = Shapes.create(aabb);
        Vec3 cam = rctx.getCameraPos();
        double camX = cam.x, camY = cam.y, camZ = cam.z;
        rctx.pushPose();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        renderVoxelShape(
                rctx.getPoseStack(), builder, vsp,
                -camX, -camY, -camZ,
                color.R, color.G, color.B, color.A
        );
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        rctx.popPose();
    }
}
