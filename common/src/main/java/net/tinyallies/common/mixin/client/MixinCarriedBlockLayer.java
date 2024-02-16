package net.tinyallies.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.monster.EnderMan;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(CarriedBlockLayer.class)
public abstract class MixinCarriedBlockLayer extends RenderLayer<EnderMan, EndermanModel<EnderMan>> {

	public MixinCarriedBlockLayer(RenderLayerParent<EnderMan, EndermanModel<EnderMan>> renderLayerParent) {
		super(renderLayerParent);
	}

	@Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/monster/EnderMan;FFFFFF)V",
			at = @At(value =  "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 0))
	public void render(PoseStack poseStack, double d, double e, double f) {
		if (this.getParentModel().young) {
			poseStack.translate(0, 1.0, -0.43);
		}
		else {
			poseStack.translate(0.0, 0.6875, -0.75);
		}
//		poseStack.translate(0.8, 0.1875, 0.8);
	}
}
