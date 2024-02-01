package net.tinyallies.common.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Phantom;
import net.tinyallies.common.util.ModUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(PhantomModel.class)
public abstract class MixinPhantomModel <T extends Entity> extends HierarchicalModel<T> {
	@Shadow @Final private ModelPart root;
	@Shadow @Final private ModelPart leftWingBase;
	@Shadow @Final private ModelPart leftWingTip;
	@Shadow @Final private ModelPart rightWingBase;
	@Shadow @Final private ModelPart rightWingTip;
	@Shadow @Final private ModelPart tailBase;
	@Shadow @Final private ModelPart tailTip;

	ModelPart body, head;

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		if (this.young) {
			ModUtil.babyfyModel(headParts(), bodyParts(), 0f, 0F, 0, pPoseStack, pBuffer, pPackedLight, pPackedOverlay,
					pRed, pGreen, pBlue, pAlpha);
		}
		else {
			super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		}
	}

	@Unique
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Unique
	protected Iterable<ModelPart> bodyParts() {
		return List.of(body);
	}

	@Override
	public void prepareMobModel(T entity, float f, float g, float h) {
		super.prepareMobModel(entity, f, g, h);
		body = root.getChild("body");
		head = body.getChild("head");
	}

	@Override
	public void setupAnim(T entity, float f, float g, float h, float i, float j) {
		if(this.young){
			this.head.xScale = 1.5F;
			this.head.yScale = 1.5F;
			this.head.zScale = 1.5F;
		}else{
			this.head.xScale = 1.0F;
			this.head.yScale = 1.0F;
			this.head.zScale = 1.0F;
		}
		float k = ((float)((Phantom)entity).getUniqueFlapTickOffset() + h) * 7.448451f * ((float)Math.PI / 180);
		float l = 16.0f;
		this.leftWingBase.zRot = Mth.cos(k) * 16.0f * ((float)Math.PI / 180);
		this.leftWingTip.zRot = Mth.cos(k) * 16.0f * ((float)Math.PI / 180);
		this.rightWingBase.zRot = -this.leftWingBase.zRot;
		this.rightWingTip.zRot = -this.leftWingTip.zRot;
		this.tailBase.xRot = -(5.0f + Mth.cos(k * 2.0f) * 5.0f) * ((float)Math.PI / 180);
		this.tailTip.xRot = -(5.0f + Mth.cos(k * 2.0f) * 5.0f) * ((float)Math.PI / 180);
	}
}
