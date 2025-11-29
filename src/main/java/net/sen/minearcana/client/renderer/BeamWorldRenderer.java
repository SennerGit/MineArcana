package net.sen.minearcana.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.sen.minearcana.client.utils.lightbeam.BeamRenderCache;
import net.sen.minearcana.client.utils.lightbeam.BeamSegment;

import java.util.List;

public class BeamWorldRenderer {

    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;

        List<BeamSegment> segments = BeamRenderCache.getSegments(mc.level);
        if (segments.isEmpty())
            return;

        PoseStack poseStack = event.getPoseStack();
        Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();

        // ⭐ Correct buffer source for NeoForge 1.21
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buffers.getBuffer(RenderType.lightning());

        poseStack.pushPose();

        // Convert world → camera space
        poseStack.translate(
                -camera.x(),
                -camera.y(),
                -camera.z()
        );

        for (BeamSegment seg : segments) {
            renderSegment(seg, poseStack, consumer);
        }

        poseStack.popPose();

        // Required to flush the draw
        buffers.endBatch(RenderType.lightning());
    }

    private static void renderSegment(BeamSegment seg, PoseStack poseStack, VertexConsumer consumer) {
        Vec3 start = seg.start();
        Vec3 end = seg.end();

        if (start == null || end == null)
            return;

        float width = 0.05f;

        // Compute quad
        Vec3 dir = end.subtract(start).normalize();
        Vec3 perp = dir.cross(new Vec3(0, 1, 0));
        if (perp.lengthSqr() < 0.0001)
            perp = dir.cross(new Vec3(1, 0, 0));

        perp = perp.normalize().scale(width);

        Vec3 p1 = start.add(perp);
        Vec3 p2 = start.subtract(perp);
        Vec3 p3 = end.subtract(perp);
        Vec3 p4 = end.add(perp);

        int col = seg.colour().getTextureDiffuseColor();
        int r = (col >> 16) & 255;
        int g = (col >> 8) & 255;
        int b = (col) & 255;
        int a = 255;

        PoseStack.Pose pose = poseStack.last();

        consumer.addVertex(pose.pose(), (float) p1.x, (float) p1.y, (float) p1.z).setColor(r, g, b, a);
        consumer.addVertex(pose.pose(), (float) p2.x, (float) p2.y, (float) p2.z).setColor(r, g, b, a);
        consumer.addVertex(pose.pose(), (float) p3.x, (float) p3.y, (float) p3.z).setColor(r, g, b, a);
        consumer.addVertex(pose.pose(), (float) p4.x, (float) p4.y, (float) p4.z).setColor(r, g, b, a);
    }
}
