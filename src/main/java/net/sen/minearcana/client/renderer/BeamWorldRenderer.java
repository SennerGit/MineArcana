package net.sen.minearcana.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import net.sen.minearcana.client.utils.lightbeam.BeamRenderCache;
import net.sen.minearcana.client.utils.lightbeam.BeamSegment;

public class BeamWorldRenderer {

    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;

        PoseStack poseStack = event.getPoseStack();
        Camera camera       = mc.gameRenderer.getMainCamera();
        Vec3 camPos         = camera.getPosition();

        // Buffer builder
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        // Get segments from the render cache (client only)
        var segments = BeamRenderCache.getSegments(mc.level);
        if (segments == null || segments.isEmpty())
            return;

        // Render each segment
        for (BeamSegment seg : segments) {
            renderSegment(seg, poseStack, buffer, camPos);
        }

        buffer.endBatch();
    }

    private static void renderSegment(
            BeamSegment seg,
            PoseStack poseStack,
            MultiBufferSource buffer,
            Vec3 camPos
    ) {
        Vec3 start = seg.start().subtract(camPos);
        Vec3 end   = seg.end().subtract(camPos);

        float width = 0.06f;

        Vec3 dir  = end.subtract(start).normalize();
        Vec3 perp = dir.cross(new Vec3(0, 1, 0)).normalize().scale(width);

        Vec3 p1 = start.add(perp);
        Vec3 p2 = start.subtract(perp);
        Vec3 p3 = end.subtract(perp);
        Vec3 p4 = end.add(perp);

        int r = seg.colour().getTextureDiffuseColor() >> 16 & 255;
        int g = seg.colour().getTextureDiffuseColor() >> 8  & 255;
        int b = seg.colour().getTextureDiffuseColor()       & 255;
        int a = 255;

        poseStack.pushPose();
        var pose = poseStack.last();

        var consumer = buffer.getBuffer(RenderType.lightning());

        consumer.addVertex(pose, (float)p1.x, (float)p1.y, (float)p1.z).setColor(r,g,b,a);
        consumer.addVertex(pose, (float)p2.x, (float)p2.y, (float)p2.z).setColor(r,g,b,a);
        consumer.addVertex(pose, (float)p3.x, (float)p3.y, (float)p3.z).setColor(r,g,b,a);
        consumer.addVertex(pose, (float)p4.x, (float)p4.y, (float)p4.z).setColor(r,g,b,a);

        poseStack.popPose();
    }
}
