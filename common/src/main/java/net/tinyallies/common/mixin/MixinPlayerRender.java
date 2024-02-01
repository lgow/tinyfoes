package net.tinyallies.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRender extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	public MixinPlayerRender(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "scale(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
			at = @At("HEAD"))
	public void scale(AbstractClientPlayer abstractClientPlayer, PoseStack poseStack, float f, CallbackInfo ci) {
		if (abstractClientPlayer.isBaby()) {
			this.shadowRadius = 0.25f;
		}
		else {
			this.shadowRadius = 0.5f;
		}
	}
}
