package net.tinyfoes.common.mixin.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen {
	@Unique private boolean babyFlag;

	@Inject(method = "renderEntityInInventory", at = @At("HEAD"), cancellable = true)
	private static void s(int i, int j, int k, float f, float g, LivingEntity livingEntity, CallbackInfo ci) {
		float h = (float)Math.atan(f / 40.0F);
		float l = (float)Math.atan(g / 40.0F);
		if(livingEntity instanceof Player player && player.isBaby()){
			l+=player.getAbilities().instabuild? 0.413333: 0.5;
		}
		PoseStack poseStack = RenderSystem.getModelViewStack();
		poseStack.pushPose();
		poseStack.translate(i, j, 1050.0);
		poseStack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack poseStack2 = new PoseStack();
		poseStack2.translate(0.0, 0.0, 1000.0);
		poseStack2.scale((float)k, (float)k, (float)k);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion2 = Vector3f.XP.rotationDegrees(l * 20.0F);
		quaternion.mul(quaternion2);
		poseStack2.mulPose(quaternion);
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
		Lighting.setupForEntityInInventory();
		EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		quaternion2.conj();
		entityRenderDispatcher.overrideCameraOrientation(quaternion2);
		entityRenderDispatcher.setRenderShadow(false);
		MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> {
			entityRenderDispatcher.render(livingEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack2, bufferSource, 15728880);
		});
		bufferSource.endBatch();
		entityRenderDispatcher.setRenderShadow(true);
		livingEntity.yBodyRot = m;
		livingEntity.setYRot(n);
		livingEntity.setXRot(o);
		livingEntity.yHeadRotO = p;
		livingEntity.yHeadRot = q;
		poseStack.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
		ci.cancel();
	}
}
