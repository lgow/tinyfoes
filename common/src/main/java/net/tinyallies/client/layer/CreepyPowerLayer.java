package net.tinyallies.client.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.tinyallies.client.model.CreepyModel;
import net.tinyallies.entity.Creepy;

public class CreepyPowerLayer extends EnergySwirlLayer<Creepy, CreepyModel> {
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation(
			"textures/entity/creeper/creeper_armor.png");
	private final CreepyModel model;

	public CreepyPowerLayer(RenderLayerParent<Creepy, CreepyModel> pRenderer, EntityModelSet pModelSet) {
		super(pRenderer);
		this.model = new CreepyModel(pModelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
	}

	protected float xOffset(float pTickCount) {
		return pTickCount * 0.01F;
	}

	protected ResourceLocation getTextureLocation() {
		return POWER_LOCATION;
	}

	protected EntityModel<Creepy> model() {
		return this.model;
	}
}