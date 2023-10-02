package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.client.layer.WhiteEyesLayer;
import com.lgow.endofherobrine.client.model.PosArmorStandModel;
import com.lgow.endofherobrine.client.model.modellayer.ModModelLayers;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.herobrine.AbstractHerobrine;
import com.lgow.endofherobrine.entity.possessed.PosArmorStand;
import com.lgow.endofherobrine.util.ModResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class PosArmorStandRenderer extends LivingEntityRenderer<PosArmorStand, PosArmorStandModel<PosArmorStand>> {
	private final RandomSource random = RandomSource.create();

	public PosArmorStandRenderer(EntityRendererProvider.Context context) {
		super(context, new PosArmorStandModel<>(context.bakeLayer(ModModelLayers.ARMOR_STAND)), 0);
		this.addLayer(new HumanoidArmorLayer<>(this,
				new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
				new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(PosArmorStand armor_stand) {
		return new ModResourceLocation("textures/entity/armor_stand.png");
	}

	@Override
	protected void renderNameTag(PosArmorStand pEntity, Component pDisplayName, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
	}
}