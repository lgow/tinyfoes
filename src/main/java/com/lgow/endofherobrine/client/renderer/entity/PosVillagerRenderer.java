package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.client.model.PosVillagerModel;
import com.lgow.endofherobrine.client.model.modellayer.ModModelLayers;
import com.lgow.endofherobrine.entity.possessed.animal.PosVillager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosVillagerRenderer extends MobRenderer<PosVillager, PosVillagerModel<PosVillager>> {

    public PosVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new PosVillagerModel<>(context.bakeLayer(ModModelLayers.POS_VILLAGER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new WhiteEyesLayer<>(this,"pos_villager_eyes.png"));
        this.addLayer(new VillagerProfessionLayer<>(this, context.getResourceManager(), "pos_villager"));
        this.addLayer(new CrossedArmsItemLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(PosVillager posVillager) {
        return new ResourceLocation("textures/entity/pos_villager/villager.png");
    }

    @Override
    protected void scale(PosVillager pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        float f = 0.9375F;
        if (pLivingEntity.isBaby()) {
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }

        pMatrixStack.scale(f, f, f);
    }
}
