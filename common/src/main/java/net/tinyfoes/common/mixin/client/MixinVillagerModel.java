package net.tinyfoes.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.tinyfoes.common.config.TinyFoesConfigs;
import net.tinyfoes.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(VillagerModel.class)
public abstract class MixinVillagerModel <T extends Entity> extends HierarchicalModel<T> {
	@Shadow @Final private ModelPart root, rightLeg, leftLeg, head;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int k) {
		if (TinyFoesConfigs.VILLAGER_HEAD_FIX.get() && this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 16F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
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
		return ImmutableList.of(root.getChild("body"), root.getChild("arms"), rightLeg, leftLeg);
	}
}
