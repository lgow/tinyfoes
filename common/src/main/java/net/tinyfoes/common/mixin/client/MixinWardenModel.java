package net.tinyfoes.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.WardenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.warden.Warden;
import net.tinyfoes.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WardenModel.class)
public abstract class MixinWardenModel <T extends Warden> extends HierarchicalModel<T> {
	@Shadow @Final protected ModelPart head;
	@Shadow @Final private ModelPart root;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int k) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 0, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root);
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/warden/Warden;FFFFF)V", at = @At("TAIL"))
	public void setupAnim(T warden, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if (this.young) {
			this.head.xScale = 1.5F;
			this.head.yScale = 1.5F;
			this.head.zScale = 1.5F;
		}
		else {
			this.head.xScale = 1.0F;
			this.head.yScale = 1.0F;
			this.head.zScale = 1.0F;
		}
	}
}
