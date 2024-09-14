package net.tinyfoes.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.tinyfoes.common.util.ModUtil;
import org.spongepowered.asm.mixin.*;

@Environment(EnvType.CLIENT)
@Mixin(SpiderModel.class)
public abstract class MixinSpiderModel <T extends Entity> extends HierarchicalModel<T> {
	@Mutable @Shadow @Final private ModelPart root, head, rightHindLeg, leftHindLeg, rightMiddleHindLeg, leftMiddleHindLeg, rightMiddleFrontLeg, leftMiddleFrontLeg, rightFrontLeg, leftFrontLeg;
	@Unique private ModelPart body0, body1;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int k) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 11F, 1F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	public void prepareMobModel(T entity, float f, float g, float h) {
		super.prepareMobModel(entity, f, g, h);
		body0 = root.getChild("body0");
		body1 = root.getChild("body1");
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body0, body1, rightHindLeg, leftHindLeg, rightMiddleHindLeg, leftMiddleHindLeg,
				rightMiddleFrontLeg, leftMiddleFrontLeg, rightFrontLeg, leftFrontLeg);
	}
}
