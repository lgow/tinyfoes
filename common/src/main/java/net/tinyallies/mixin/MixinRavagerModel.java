package net.tinyallies.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.RavagerModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.tinyallies.util.ModUtil;
import org.spongepowered.asm.mixin.*;
@Environment(EnvType.CLIENT)
@Mixin(RavagerModel.class)
public abstract class MixinRavagerModel <T extends Entity> extends HierarchicalModel<T> {
	@Mutable @Shadow @Final private ModelPart root, head, mouth, rightHindLeg, leftHindLeg, rightFrontLeg, leftFrontLeg, neck;
	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if(this.young){
			ModUtil.babyfyModel(headParts(), bodyParts(), 7F, 9F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed,
				pGreen, pBlue, pAlpha);
		}
		else{
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root.getChild("neck"), root.getChild("body"), rightHindLeg, leftHindLeg, rightFrontLeg, leftFrontLeg);
	}

}
