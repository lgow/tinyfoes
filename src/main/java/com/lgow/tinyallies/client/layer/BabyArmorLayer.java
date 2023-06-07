package com.lgow.tinyallies.client.layer;

import com.google.common.collect.Maps;
import com.lgow.tinyallies.entity.BabyMonster;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class BabyArmorLayer <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();

	private final A innerModel;

	private final A outerModel;

	private final TextureAtlas armorTrimAtlas;

	private final float offsetY;

	public BabyArmorLayer(RenderLayerParent<T, M> p_267286_, A p_267110_, A p_267150_, ModelManager p_267238_, float offsetY) {
		super(p_267286_);
		this.innerModel = p_267110_;
		this.outerModel = p_267150_;
		this.armorTrimAtlas = p_267238_.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
		this.offsetY = offsetY;
	}

	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if (pLivingEntity instanceof BabyMonster baby && baby.isInSittingPose()) {
			pMatrixStack.pushPose();
			pMatrixStack.translate(0, offsetY, 0);
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.CHEST, pPackedLight,
					this.getArmorModel(EquipmentSlot.CHEST));
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.LEGS, pPackedLight,
					this.getArmorModel(EquipmentSlot.LEGS));
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.FEET, pPackedLight,
					this.getArmorModel(EquipmentSlot.FEET));
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.HEAD, pPackedLight,
					this.getArmorModel(EquipmentSlot.HEAD));
			pMatrixStack.popPose();
		}
		else {
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.CHEST, pPackedLight,
					this.getArmorModel(EquipmentSlot.CHEST));
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.LEGS, pPackedLight,
					this.getArmorModel(EquipmentSlot.LEGS));
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.FEET, pPackedLight,
					this.getArmorModel(EquipmentSlot.FEET));
			this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.HEAD, pPackedLight,
					this.getArmorModel(EquipmentSlot.HEAD));
		}
	}

	private void renderArmorPiece(PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, EquipmentSlot pSlot, int pPackedLight, A pModel) {
		ItemStack itemstack = pLivingEntity.getItemBySlot(pSlot);
		Item $$9 = itemstack.getItem();
		if ($$9 instanceof ArmorItem armoritem) {
			if (armoritem.getEquipmentSlot() == pSlot) {
				this.getParentModel().copyPropertiesTo(pModel);
				this.setPartVisibility(pModel, pSlot);
				net.minecraft.client.model.Model model = getArmorModelHook(pLivingEntity, itemstack, pSlot, pModel);
				boolean flag1 = this.usesInnerModel(pSlot);
				boolean flag = itemstack.hasFoil();
				if (armoritem instanceof net.minecraft.world.item.DyeableLeatherItem) {
					int i = ((net.minecraft.world.item.DyeableLeatherItem) armoritem).getColor(itemstack);
					float f = (float) (i >> 16 & 255) / 255.0F;
					float f1 = (float) (i >> 8 & 255) / 255.0F;
					float f2 = (float) (i & 255) / 255.0F;
					this.renderModel(pPoseStack, pBuffer, pPackedLight, flag, model, f, f1, f2,
							this.getArmorResource(pLivingEntity, itemstack, pSlot, null));
					this.renderModel(pPoseStack, pBuffer, pPackedLight, flag, model, 1.0F, 1.0F, 1.0F,
							this.getArmorResource(pLivingEntity, itemstack, pSlot, "overlay"));
				}
				else {
					this.renderModel(pPoseStack, pBuffer, pPackedLight, flag, model, 1.0F, 1.0F, 1.0F,
							this.getArmorResource(pLivingEntity, itemstack, pSlot, null));
				}
				if (pLivingEntity.level.enabledFeatures().contains(FeatureFlags.UPDATE_1_20)) {
					ArmorTrim.getTrim(pLivingEntity.level.registryAccess(), itemstack).ifPresent((p_267897_) -> {
						this.renderTrim(armoritem.getMaterial(), pPoseStack, pBuffer, pPackedLight, p_267897_, flag,
								pModel, flag1, 1.0F, 1.0F, 1.0F);
					});
				}
			}
		}
	}

	protected void setPartVisibility(A pModel, EquipmentSlot pSlot) {
		pModel.setAllVisible(false);
		switch (pSlot) {
			case HEAD:
				pModel.head.visible = true;
				pModel.hat.visible = true;
				break;
			case CHEST:
				pModel.body.visible = true;
				pModel.rightArm.visible = true;
				pModel.leftArm.visible = true;
				break;
			case LEGS:
				pModel.body.visible = true;
				pModel.rightLeg.visible = true;
				pModel.leftLeg.visible = true;
				break;
			case FEET:
				pModel.rightLeg.visible = true;
				pModel.leftLeg.visible = true;
		}
	}

	private void renderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorItem pArmorItem, boolean pWithGlint, A pModel, boolean pLayer2, float pRed, float pGreen, float pBlue, @Nullable String pArmorSuffix) {
		renderModel(pPoseStack, pBuffer, pPackedLight, pWithGlint, pModel, pRed, pGreen, pBlue,
				this.getArmorLocation(pArmorItem, pLayer2, pArmorSuffix));
	}

	private void renderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, boolean pWithGlint, net.minecraft.client.model.Model pModel, float pRed, float pGreen, float pBlue, ResourceLocation armorResource) {
		VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBuffer,
				RenderType.armorCutoutNoCull(armorResource), false, pWithGlint);
		pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, pRed, pGreen, pBlue,
				1.0F);
	}

	private void renderTrim(ArmorMaterial p_267946_, PoseStack p_268019_, MultiBufferSource p_268023_, int p_268190_, ArmorTrim p_267984_, boolean p_267965_, A p_267949_, boolean p_268259_, float p_268337_, float p_268095_, float p_268305_) {
		TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(
				p_268259_ ? p_267984_.innerTexture(p_267946_) : p_267984_.outerTexture(p_267946_));
		VertexConsumer vertexconsumer = textureatlassprite.wrap(
				ItemRenderer.getFoilBufferDirect(p_268023_, Sheets.armorTrimsSheet(), true, p_267965_));
		p_267949_.renderToBuffer(p_268019_, vertexconsumer, p_268190_, OverlayTexture.NO_OVERLAY, p_268337_, p_268095_,
				p_268305_, 1.0F);
	}

	private A getArmorModel(EquipmentSlot pSlot) {
		return this.usesInnerModel(pSlot) ? this.innerModel : this.outerModel;
	}

	private boolean usesInnerModel(EquipmentSlot pSlot) {
		return pSlot == EquipmentSlot.LEGS;
	}

	@Deprecated //Use the more sensitive version getArmorResource below
	private ResourceLocation getArmorLocation(ArmorItem pArmorItem, boolean pLayer2, @Nullable String pSuffix) {
		String s = "textures/models/armor/" + pArmorItem.getMaterial().getName() + "_layer_" + (pLayer2 ? 2 : 1) + (
				pSuffix == null ? "" : "_" + pSuffix) + ".png";
		return ARMOR_LOCATION_CACHE.computeIfAbsent(s, ResourceLocation::new);
	}

	protected net.minecraft.client.model.Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
		return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
	}

	public ResourceLocation getArmorResource(net.minecraft.world.entity.Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
		ArmorItem item = (ArmorItem) stack.getItem();
		String texture = item.getMaterial().getName();
		String domain = "minecraft";
		int idx = texture.indexOf(':');
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}
		String s1 = String.format(java.util.Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain, texture,
				(usesInnerModel(slot) ? 2 : 1), type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));
		s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);
		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
		}
		return resourcelocation;
	}
	/*=================================== FORGE END ===========================================*/
}