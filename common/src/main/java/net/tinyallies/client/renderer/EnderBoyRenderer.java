package net.tinyallies.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tinyallies.client.layer.EnderBoyCarriedBlockLayer;
import net.tinyallies.client.layer.GlowingEyesLayer;
import net.tinyallies.client.model.EnderBoyModel;
import net.tinyallies.entity.EnderBoy;

public class EnderBoyRenderer extends MobRenderer<EnderBoy, EnderBoyModel<EnderBoy>> {
	private static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation(
			"textures/entity/enderman/enderman.png");
	private static final ResourceLocation ENDERMAN_EYES_LOCATION = new ResourceLocation(
			"textures/entity/enderman/enderman_eyes.png");
	private final RandomSource random = RandomSource.create();

	public EnderBoyRenderer(EntityRendererProvider.Context p_173992_) {
		super(p_173992_, new EnderBoyModel<>(p_173992_.bakeLayer(ModelLayers.ENDERMAN)), 0.25F);
		this.addLayer(new GlowingEyesLayer<>(this, ENDERMAN_EYES_LOCATION));
		this.addLayer(new EnderBoyCarriedBlockLayer(this, p_173992_.getBlockRenderDispatcher()));
	}

	public void render(EnderBoy pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		BlockState blockstate = pEntity.getCarriedBlock();
		EnderBoyModel<EnderBoy> endermanmodel = this.getModel();
		endermanmodel.carrying = blockstate != null;
		endermanmodel.creepy = pEntity.isCreepy();
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}

	public Vec3 getRenderOffset(EnderBoy pEntity, float pPartialTicks) {
		if (pEntity.isCreepy()) {
			double d0 = 0.02D;
			return new Vec3(this.random.nextGaussian() * d0, 0.0D, this.random.nextGaussian() * d0);
		}
		else {
			return pEntity.isInSittingPose() ? new Vec3(0, -0.81, 0) : super.getRenderOffset(pEntity, pPartialTicks);
		}
	}

	public ResourceLocation getTextureLocation(EnderBoy pEntity) {
		return ENDERMAN_LOCATION;
	}
}