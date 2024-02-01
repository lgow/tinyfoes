package net.tinyallies.common.mixin;

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

@Environment(value = EnvType.CLIENT)
@Mixin(SnowGolemHeadLayer.class)
public abstract class MixinSnowGolemHeadLayer extends RenderLayer<SnowGolem, SnowGolemModel<SnowGolem>> {

	public MixinSnowGolemHeadLayer(RenderLayerParent<SnowGolem, SnowGolemModel<SnowGolem>> renderLayerParent) {
		super(renderLayerParent);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/SnowGolem;FFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", ordinal = 0))
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, SnowGolem snowGolem, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (this.getParentModel().young) {
			poseStack.scale(0.75f,0.75f,0.75f);
			poseStack.translate(0.0, 0.94375, 0.0);
		}
	}
}
