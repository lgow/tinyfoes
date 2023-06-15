package com.lgow.endofherobrine.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class    DopModel <T extends LivingEntity> extends PlayerModel<T> {

    public final ModelPart ear;
    public final ModelPart cloak;
    public final ModelPart jacket;
    public final ModelPart wideLeftArm;
    public final ModelPart wideLeftSleeve;
    public final ModelPart wideRightArm;
    public final ModelPart wideRightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;

    public DopModel(ModelPart pRoot) {
        super(pRoot, false);
        this.ear = pRoot.getChild("ear");
        this.cloak = pRoot.getChild("cloak");
        this.jacket = pRoot.getChild("jacket");
        this.wideLeftArm = pRoot.getChild("wide_left_arm");
        this.wideLeftSleeve = pRoot.getChild("wide_left_sleeve");
        this.wideRightArm = pRoot.getChild("wide_right_arm");
        this.wideRightSleeve = pRoot.getChild("wide_right_sleeve");
        this.leftPants = pRoot.getChild("left_pants");
        this.rightPants = pRoot.getChild("right_pants");
    }

    public static LayerDefinition createBody() {
        CubeDeformation none = CubeDeformation.NONE;
        MeshDefinition meshdefinition = HumanoidModel.createMesh(none, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, none), PartPose.ZERO);
        partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, none, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
        //slim
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, none), PartPose.offset(5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, none), PartPose.offset(-5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, none.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
        partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, none.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
        //slim
        partdefinition.addOrReplaceChild("wide_right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, none), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("wide_left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, none), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("wide_left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, none.extend(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("wide_right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, none.extend(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, none), PartPose.offset(1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, none.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, none.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, none.extend(0.25F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.wideLeftArm.copyFrom(this.leftArm);
        this.wideRightArm.copyFrom(this.rightArm);
        this.wideLeftSleeve.copyFrom(this.leftArm);
        this.wideRightSleeve.copyFrom(this.rightArm);
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
    }
}
