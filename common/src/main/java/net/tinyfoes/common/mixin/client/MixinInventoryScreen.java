package net.tinyfoes.common.mixin.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventory;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen {

	@Inject(method = "renderEntityInInventoryFollowsMouse", at = @At("HEAD"), cancellable = true)
	private static void s(GuiGraphics guiGraphics, int i, int j, int k, float f, float g, LivingEntity livingEntity, CallbackInfo ci) {
		float h = (float)Math.atan(f / 40.0F);
		float l = (float)Math.atan(g / 40.0F);
		if(livingEntity instanceof Player player && player.isBaby()){
			l+= (float) (player.getAbilities().instabuild? 0.413333: 0.5);
		}
		Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
		Quaternionf quaternionf2 = (new Quaternionf()).rotateX(l * 20.0F * 0.017453292F);
		quaternionf.mul(quaternionf2);
		float m = livingEntity.yBodyRot;
		float n = livingEntity.getYRot();
		float o = livingEntity.getXRot();
		float p = livingEntity.yHeadRotO;
		float q = livingEntity.yHeadRot;
		livingEntity.yBodyRot = 180.0F + h * 20.0F;
		livingEntity.setYRot(180.0F + h * 40.0F);
		livingEntity.setXRot(-l * 20.0F);
		livingEntity.yHeadRot = livingEntity.getYRot();
		livingEntity.yHeadRotO = livingEntity.getYRot();
		renderEntityInInventory(guiGraphics, i, j, k, quaternionf, quaternionf2, livingEntity);
		livingEntity.yBodyRot = m;
		livingEntity.setYRot(n);
		livingEntity.setXRot(o);
		livingEntity.yHeadRotO = p;
		livingEntity.yHeadRot = q;
		ci.cancel();
	}
}
