package com.liaoliao.flighthelmet.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class PigHatArmorModel extends ModelBiped {
    public static final PigHatArmorModel INSTANCE = new PigHatArmorModel();

    private PigHatArmorModel() { super(0.0F); }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float scale) {
        if (!bipedHead.showModel || bipedHead.isHidden) return;
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        GlStateManager.pushMatrix();
        if (isChild) {
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
        } else if (isSneak) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
        bipedHead.postRender(scale);
        GlStateManager.scale(scale * 16.0F, scale * 16.0F, scale * 16.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        for (float[] quad : PigHatGeometry.QUADS) {
            for (int vertex = 0; vertex < 4; vertex++) {
                int offset = 3 + vertex * 5;
                buffer.pos(quad[offset], quad[offset + 1], quad[offset + 2])
                        .tex(quad[offset + 3], quad[offset + 4])
                        .normal(quad[0], quad[1], quad[2]).endVertex();
            }
        }
        tessellator.draw();
        GlStateManager.popMatrix();
    }
}
