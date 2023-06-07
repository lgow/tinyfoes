package com.lgow.tinyallies.client.renderer.projectiles;

import com.lgow.tinyallies.Main;
import com.lgow.tinyallies.entity.projectile.BabyfierBlob;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlobRenderer extends EntityRenderer<BabyfierBlob> {

   public BlobRenderer(EntityRendererProvider.Context pContext) {
      super(pContext);
   }

   @Override
   public ResourceLocation getTextureLocation(BabyfierBlob pEntity) {
      return new ResourceLocation(Main.MODID, "textures/entity/blob/blob.png");
   }

   @Override
   public void render(BabyfierBlob pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
      super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
//      GlSt.pushMatrix();
//      GlStateManager.translate(x, y, z);
//      GlStateManager.scale(0.5, 0.5, 0.5);
//      GlStateManager.color(1.0f, 1.0f, 1.0f);
//      RenderHelper.renderCube();
//      GlStateManager.popMatrix();
   }
}