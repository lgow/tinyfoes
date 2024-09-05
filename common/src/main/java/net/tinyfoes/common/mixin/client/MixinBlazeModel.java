package net.tinyfoes.common.mixin.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.tinyfoes.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(BlazeModel.class)
public abstract class MixinBlazeModel <T extends Entity> extends HierarchicalModel<T> {
	@Shadow @Final private ModelPart head;
	@Shadow @Final private ModelPart[] upperBodyParts;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 15F, 0F, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
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
		return List.of(upperBodyParts);
	}

	@Override
	public void setupAnim(T entity, float f, float g, float h, float i, float j) {
		int l;
		float rotationSpeedModifier = this.young ? 1.5f : 1.0f; // Increase speed by 50% for babies
		float k = h * (float) Math.PI * -0.1f * rotationSpeedModifier;
		float layer1 = this.young ? 11 : 9;
		float layer2 = this.young ? 8 : 7;
		float layer3 = 5;
		for (l = 0; l < 4; ++l) {
			this.upperBodyParts[l].y = -2.0f + Mth.cos(((float) (l * 2) + h) * 0.25f);
			this.upperBodyParts[l].x = Mth.cos(k) * layer1;
			this.upperBodyParts[l].z = Mth.sin(k) * layer1;
			k += 1.5707964f;
		}
		k = 0.7853982f + h * (float) Math.PI * 0.03f * rotationSpeedModifier;
		for (l = 4; l < 8; ++l) {
			this.upperBodyParts[l].y = 2.0f + Mth.cos(((float) (l * 2) + h) * 0.25f);
			this.upperBodyParts[l].x = Mth.cos(k) * layer2;
			this.upperBodyParts[l].z = Mth.sin(k) * layer2;
			k += 1.5707964f;
		}
		k = 0.47123894f + h * (float) Math.PI * -0.05f * rotationSpeedModifier;
		for (l = 8; l < 12; ++l) {
			this.upperBodyParts[l].y = 11.0f + Mth.cos(((float) l * 1.5f + h) * 0.5f);
			this.upperBodyParts[l].x = Mth.cos(k) * layer3;
			this.upperBodyParts[l].z = Mth.sin(k) * layer3;
			k += 1.5707964f;
		}
		this.head.yRot = i * ((float) Math.PI / 180);
		this.head.xRot = j * ((float) Math.PI / 180);
	}
}
