package com.lgow.endofherobrine.client.layer;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.entity.possessed.animal.PosVillager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UncrossedVillagerArmsLayer <T extends PosVillager & VillagerDataHolder, M extends EntityModel<T> & VillagerHeadModel> extends VillagerProfessionLayer<T, M> {

    public UncrossedVillagerArmsLayer(RenderLayerParent pRenderer, ResourceManager pResourceManager) {
	    super(pRenderer, pResourceManager, "");
	}

	//only renders when the entity is attacking
	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (pLivingEntity.isAggressive()) {
			VillagerProfession villagerprofession = pLivingEntity.getVillagerData().getProfession();
			M m = this.getParentModel();
			ResourceLocation type = this.getResourceLocation("type",
					BuiltInRegistries.VILLAGER_TYPE.getKey(pLivingEntity.getVillagerData().getType()));
			renderColoredCutoutModel(m, new ResourceLocation(Main.MOD_ID, "textures/entity/villager/arms/villager.png"),
					pMatrixStack, pBuffer, pPackedLight, pLivingEntity, 1.0F, 1.0F, 1.0F);
			renderColoredCutoutModel(m, type, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, 1.0F, 1.0F, 1.0F);
			if (villagerprofession != VillagerProfession.NONE && !pLivingEntity.isBaby()) {
				ResourceLocation profession = this.getResourceLocation("profession",
						BuiltInRegistries.VILLAGER_PROFESSION.getKey(villagerprofession));
				renderColoredCutoutModel(m, profession, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, 1.0F, 1.0F,
						1.0F);
			}
		}
	}

	@Override
	public ResourceLocation getResourceLocation(String dataType, ResourceLocation loc) {
		return new ResourceLocation(Main.MOD_ID,
				"textures/entity/villager/arms/" + dataType + "/" + loc.getPath() + ".png");
	}
}
