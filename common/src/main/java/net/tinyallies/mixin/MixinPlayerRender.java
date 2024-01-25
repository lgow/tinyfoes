package net.tinyallies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRender
		extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	public MixinPlayerRender(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Override
	public void scale(AbstractClientPlayer livingEntity, PoseStack poseStack, float f) {
		if (livingEntity.isBaby()) {
			this.shadowRadius = 0.25f;
		} else {
			this.shadowRadius = 0.5f;
		}
		poseStack.scale(0.9375f, 0.9375f, 0.9375f);
	}
}
