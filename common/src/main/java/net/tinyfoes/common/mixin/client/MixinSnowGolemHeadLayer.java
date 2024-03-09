package net.tinyfoes.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.world.entity.animal.SnowGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SnowGolemHeadLayer.class)
public abstract class MixinSnowGolemHeadLayer extends RenderLayer<SnowGolem, SnowGolemModel<SnowGolem>> {
	public MixinSnowGolemHeadLayer(RenderLayerParent<SnowGolem, SnowGolemModel<SnowGolem>> renderLayerParent) {
		super(renderLayerParent);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/SnowGolem;FFFFFF)V",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/model/geom/ModelPart;translateAndRotate(Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
	public void translateAndRotate(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, SnowGolem snowGolem, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (this.getParentModel().young) {
			poseStack.translate(0.0f, 0.75f, 0.0f);
			poseStack.scale(0.75f, 0.75f, 0.75f);
		}
	}
	//	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/SnowGolem;FFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 1))
	//	public void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, SnowGolem snowGolem, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
	//		if(this.getParentModel().young){
	//			poseStack.scale(2.0f, 2.0f,2.0f);
	//		}
	//	}
}
