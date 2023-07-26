package net.tinyallies.client.renderer;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;
import net.tinyallies.client.layer.BabyHeldItemLayer;
import net.tinyallies.client.model.ZombyModel;
import net.tinyallies.entity.Zomby;
import net.tinyallies.util.TinyAlliesResLoc;

public class ZombyRenderer extends AbstractZombieRenderer<Zomby, ZombieModel<Zomby>> {
	public ZombyRenderer(EntityRendererProvider.Context p_174456_) {
		this(p_174456_, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
	}

	public ZombyRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation pZombieLayer, ModelLayerLocation pInnerArmor, ModelLayerLocation pOuterArmor) {
		super(pContext, new ZombyModel(pContext.bakeLayer(pZombieLayer)),
				new ZombieModel<>(pContext.bakeLayer(pInnerArmor)), new ZombieModel<>(pContext.bakeLayer(pOuterArmor)));
		this.layers.clear();
		this.addLayer(new HumanoidArmorLayer<>(this, new ZombyModel(pContext.bakeLayer(pInnerArmor)),
				new ZombyModel(pContext.bakeLayer(pOuterArmor)), pContext.getModelManager()));
		this.addLayer(new CustomHeadLayer<>(this, pContext.getModelSet(), 1.0F, 1.0F, 1.0F,
				pContext.getItemInHandRenderer()));
		//		this.addLayer(new ElytraLayer<>(this, pContext.getModelSet()));
		this.addLayer(new BabyHeldItemLayer<>(this, pContext.getItemInHandRenderer()));
	}

	@Override
	public Vec3 getRenderOffset(Zomby entity, float f) {
		return entity.isInSittingPose() ? new Vec3(0, -0.31, 0) : super.getRenderOffset(entity, f);
	}

	@Override
	public ResourceLocation getTextureLocation(Zombie pEntity) {
		return pEntity.getName().getString().equals("PhoenixSC") ? new TinyAlliesResLoc(
				"textures/entity/zombie/phoenixsc.png") : super.getTextureLocation(pEntity);
	}
}