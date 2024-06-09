package net.tinyapi.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckInBodyLayer.class)
public abstract class MixinStuckInModelLayer <T extends LivingEntity, M extends PlayerModel<T>> extends RenderLayer<T, M> {
	public MixinStuckInModelLayer(RenderLayerParent<T, M> renderLayerParent) {
		super(renderLayerParent);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/model/geom/ModelPart;translateAndRotate(Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
	public void translateAndRotate(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (this.getParentModel().young) {
			poseStack.translate(0.0f, 0.75f, 0.0f);
			poseStack.scale(0.5f, 0.5f, 0.5f);
		}
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/StuckInBodyLayer;renderStuckItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFF)V"))
	public void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (this.getParentModel().young) {
			poseStack.scale(2.0f, 2.0f, 2.0f);
		}
	}
}
