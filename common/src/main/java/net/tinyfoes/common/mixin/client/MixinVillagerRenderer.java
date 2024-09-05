package net.tinyfoes.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.tinyfoes.common.config.TinyFoesConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(VillagerRenderer.class)
public abstract class MixinVillagerRenderer extends MobRenderer<WanderingTrader, VillagerModel<WanderingTrader>> {
	public MixinVillagerRenderer(EntityRendererProvider.Context context, VillagerModel<WanderingTrader> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "scale(Lnet/minecraft/world/entity/npc/Villager;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
			at = @At("HEAD"), cancellable = true)
	public void scale(Villager villager, PoseStack poseStack, float f, CallbackInfo ci) {
		if (TinyFoesConfigs.VILLAGER_HEAD_FIX.get()) {
			float g = 0.9375f;
			if (villager.isBaby()) {
				this.shadowRadius = 0.25f;
			}
			else {
				this.shadowRadius = 0.5f;
			}
			poseStack.scale(g, g, g);
			ci.cancel();
		}
	}
}

