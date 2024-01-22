package net.tinyallies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CarriedBlockLayer.class)
public abstract class MixinCarriedBlockLayer extends RenderLayer<EnderMan, EndermanModel<EnderMan>> {
	@Shadow @Final private BlockRenderDispatcher blockRenderer;

	public MixinCarriedBlockLayer(RenderLayerParent<EnderMan, EndermanModel<EnderMan>> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, EnderMan entity, float f, float g, float h, float j, float k, float l) {
		BlockState blockstate = entity.getCarriedBlock();
		if (blockstate != null) {
			if (this.getParentModel().young) {
				poseStack.pushPose();
				poseStack.translate(-.78, 1.0, -0.45);
				poseStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
				poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
				poseStack.translate(0.8, 0.1875, 0.8);
				float m = 0.5F;
				poseStack.scale(-0.5F, -0.5F, 0.5F);
				poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
				this.blockRenderer.renderSingleBlock(blockstate, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
				poseStack.popPose();
			}
			else {
				poseStack.pushPose();
				poseStack.translate(0.0, 0.6875, -0.75);
				poseStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
				poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
				poseStack.translate(0.25, 0.1875, 0.25);
				float m = 0.5F;
				poseStack.scale(-0.5F, -0.5F, 0.5F);
				poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
				this.blockRenderer.renderSingleBlock(blockstate, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
				poseStack.popPose();
			}
		}
	}
}
