package net.tinyfoes.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(WitchItemLayer.class)
public abstract class MixinWitchItemLayer <T extends LivingEntity> extends CrossedArmsItemLayer<T, WitchModel<T>> {
	public MixinWitchItemLayer(RenderLayerParent<T, WitchModel<T>> renderLayerParent, ItemInHandRenderer itemInHandRenderer) {
		super(renderLayerParent, itemInHandRenderer);
	}

	@Redirect(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0))
	public void render(PoseStack instance, float f, float g, float h) {
		if (this.getParentModel().young) {
			instance.translate(-0.05, 0.04, .36);
		}
		else {
			instance.translate(0.0625, 0.25, 0.0);
		}
	}
}
