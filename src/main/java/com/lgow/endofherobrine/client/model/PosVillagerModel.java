package com.lgow.endofherobrine.client.model;

import com.lgow.endofherobrine.entity.possessed.animal.PosVillager;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosVillagerModel <T extends PosVillager> extends VillagerModel<T> implements VillagerHeadModel {
	private final ModelPart arms, rightArm, leftArm;

	public PosVillagerModel(ModelPart modelPart) {
		super(modelPart);
		this.arms = this.root().getChild("arms");
		this.rightArm = this.root().getChild("right_arm");
		this.leftArm = this.root().getChild("left_arm");
	}

	public static LayerDefinition createUncrossedArmsLayer() {
		MeshDefinition meshdefinition = VillagerModel.createBodyModel();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(44, 22).addBox(- 3.0F, - 2.0F,
						- 2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(- 5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(- 1.0F,
				- 2.0F, - 2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, pEntity.isAggressive(), this.attackTime,
				pAgeInTicks);

		boolean isAggressive = pEntity.isAggressive();
		this.arms.visible = !isAggressive;
		this.leftArm.visible = isAggressive;
		this.rightArm.visible = isAggressive;
	}
}