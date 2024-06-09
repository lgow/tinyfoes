package net.tinyapi.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.tinyapi.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(IllagerModel.class)
public abstract class MixinIllagerModel <T extends AbstractIllager> extends HierarchicalModel<T> {
	@Shadow @Final private ModelPart root, rightLeg, leftLeg, head, hat, arms, leftArm;
	@Shadow @Final private ModelPart rightArm;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 16F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
					pRed, pGreen, pBlue, pAlpha);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(head, hat);
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root.getChild("body"), arms, rightArm, leftArm, rightLeg, leftLeg);
	}
}
