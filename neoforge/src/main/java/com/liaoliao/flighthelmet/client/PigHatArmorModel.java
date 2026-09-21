package com.liaoliao.flighthelmet.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;

/** Uses the armor layer's pose, lighting, overlay and enchantment glint. */
final class PigHatArmorModel extends Model {
    private final HumanoidModel<?> original;

    PigHatArmorModel(HumanoidModel<?> original) {
        super(RenderType::armorCutoutNoCull);
        this.original = original;
    }

    @Override
    public void renderToBuffer(PoseStack poses, VertexConsumer vertices, int light, int overlay, int color) {
        if (!original.head.visible) return;
        poses.pushPose();
        if (original.young) {
            poses.scale(0.75F, 0.75F, 0.75F);
            poses.translate(0.0F, 1.0F, 0.0F);
        }
        original.head.translateAndRotate(poses);
        PoseStack.Pose pose = poses.last();
        for (float[] quad : PigHatGeometry.QUADS) {
            for (int vertex = 0; vertex < 4; vertex++) {
                int offset = 3 + vertex * 5;
                vertices.addVertex(pose.pose(), quad[offset], quad[offset + 1], quad[offset + 2])
                        .setColor(color).setUv(quad[offset + 3], quad[offset + 4])
                        .setOverlay(overlay).setLight(light)
                        .setNormal(pose, quad[0], quad[1], quad[2]);
            }
        }
        poses.popPose();
    }
}
