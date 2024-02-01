package net.tinyallies.common.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.tinyallies.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(value= EnvType.CLIENT)
@Mixin(IronGolemModel.class)
public abstract class MixinIronGolemModel <T extends Entity>
		extends HierarchicalModel<T> {
	@Shadow @Final private ModelPart leftArm;
	@Shadow @Final private ModelPart rightArm;
	@Shadow @Final private ModelPart head;
	@Shadow @Final private ModelPart rightLeg;
	@Shadow @Final private ModelPart leftLeg;

	@Shadow public abstract ModelPart root();

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 19F, 1F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
					pRed, pGreen, pBlue, pAlpha);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(head);
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root().getChild("body") , leftArm, rightArm, leftLeg, rightLeg);
	}
}
