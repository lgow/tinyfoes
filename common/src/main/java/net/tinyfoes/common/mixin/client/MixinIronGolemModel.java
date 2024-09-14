package net.tinyfoes.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.tinyfoes.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(IronGolemModel.class)
public abstract class MixinIronGolemModel <T extends Entity> extends HierarchicalModel<T> {
	@Shadow @Final private ModelPart leftArm;
	@Shadow @Final private ModelPart rightArm;
	@Shadow @Final private ModelPart head;
	@Shadow @Final private ModelPart rightLeg;
	@Shadow @Final private ModelPart leftLeg;

	@Shadow
	public abstract ModelPart root();

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int k) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 19F, 1F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(head);
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(root().getChild("body"), leftArm, rightArm, leftLeg, rightLeg);
	}
}
