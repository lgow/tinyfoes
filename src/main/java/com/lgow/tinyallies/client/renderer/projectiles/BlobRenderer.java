package com.lgow.tinyallies.client.renderer.projectiles;

import com.lgow.tinyallies.Main;
import com.lgow.tinyallies.entity.projectile.BabyfierBlob;
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
}