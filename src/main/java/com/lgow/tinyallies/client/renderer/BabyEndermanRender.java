package com.lgow.tinyallies.client.renderer;

import com.lgow.tinyallies.client.layer.BabyCarriedBlockLayer;
import com.lgow.tinyallies.client.layer.GlowingEyesLayer;
import com.lgow.tinyallies.client.model.BabyEndermanModel;
import com.lgow.tinyallies.entity.BabyEnderman;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BabyEndermanRender extends MobRenderer<BabyEnderman, BabyEndermanModel<BabyEnderman>> {
	private static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");
	private static final ResourceLocation ENDERMAN_EYES_LOCATION = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
	private final RandomSource random = RandomSource.create();

	public BabyEndermanRender(EntityRendererProvider.Context p_173992_) {
		super(p_173992_, new BabyEndermanModel<>(p_173992_.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
		this.addLayer(new GlowingEyesLayer<>(this, ENDERMAN_EYES_LOCATION));
		this.addLayer(new BabyCarriedBlockLayer(this, p_173992_.getBlockRenderDispatcher()));
	}

	public void render(BabyEnderman pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		BlockState blockstate = pEntity.getCarriedBlock();
		BabyEndermanModel<BabyEnderman> endermanmodel = this.getModel();
		endermanmodel.carrying = blockstate != null;
		endermanmodel.creepy = pEntity.isCreepy();
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}

	public Vec3 getRenderOffset(BabyEnderman pEntity, float pPartialTicks) {
		if (pEntity.isCreepy()) {
			double d0 = 0.02D;
			return new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
		} else {
			return super.getRenderOffset(pEntity, pPartialTicks);
		}
	}

	public ResourceLocation getTextureLocation(BabyEnderman pEntity) {
		return ENDERMAN_LOCATION;
	}
}