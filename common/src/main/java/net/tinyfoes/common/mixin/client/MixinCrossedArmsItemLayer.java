package net.tinyfoes.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(CrossedArmsItemLayer.class)
public abstract class MixinCrossedArmsItemLayer <T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	public MixinCrossedArmsItemLayer(RenderLayerParent<T, M> renderLayerParent) {
		super(renderLayerParent);
	}

	@Redirect(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0))
	public void render(PoseStack poseStack, float f, float g, float h) {
		if (this.getParentModel().young) {
			poseStack.translate(0.0, 1.1f, -0.2f);
		}
		else {
			poseStack.translate(0.0, 0.4f, -0.4f);
		}
	}
}
