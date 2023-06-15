package com.lgow.endofherobrine.client.model;

import com.lgow.endofherobrine.entity.possessed.PosPigman;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PosPigmanModel <T extends PosPigman> extends ZombieModel<T> {
	public PosPigmanModel(ModelPart modelPart) {
		super(modelPart);
		this.head.getChild("snout");
	}

	public static LayerDefinition createSnoutLayer() {
		CubeDeformation none = CubeDeformation.NONE;
		MeshDefinition meshdefinition = HumanoidModel.createMesh(none, 0.0F);
		meshdefinition.getRoot().getChild("head").addOrReplaceChild("snout",
				new CubeListBuilder().texOffs(54, 16).addBox(- 2.0F, - 4.0F, - 5.0F, 4.0F, 3.0F, 1.0F, none),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}