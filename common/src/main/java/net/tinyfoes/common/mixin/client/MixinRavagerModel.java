package net.tinyfoes.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.RavagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.tinyfoes.common.util.ModUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RavagerModel.class)
public abstract class MixinRavagerModel <T extends Entity> extends HierarchicalModel<T> {
	@Mutable @Shadow @Final private ModelPart root, head, mouth, rightHindLeg, leftHindLeg, rightFrontLeg, leftFrontLeg, neck;
	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if(this.young){
			ModUtil.babyfyModel(headParts(), bodyParts(), 0F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed,
				pGreen, pBlue, pAlpha);
		}
		else{
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}


	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root.getChild("neck"), root.getChild("body"), rightHindLeg, leftHindLeg, rightFrontLeg, leftFrontLeg);
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/Ravager;FFFFF)V", at = @At("HEAD"))
	public void setupAnim(Ravager ravager, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if(this.young){
			this.head.xScale = 1.5F;
			this.head.yScale = 1.5F;
			this.head.zScale = 1.5F;
		}else{
			this.head.xScale = 1.0F;
			this.head.yScale = 1.0F;
			this.head.zScale = 1.0F;
		}
	}
}
