package com.lgow.endofherobrine.client.layer;

import com.lgow.endofherobrine.entity.herobrine.Doppelganger;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class DopElytraLayer<T extends Doppelganger, M extends PlayerModel<T>> extends RenderLayer<T, M> {

    private final ElytraModel<T> elytraModel;

    public DopElytraLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
        super(parent);
        this.elytraModel = new ElytraModel<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    public void render(PoseStack poseStack, MultiBufferSource source, int lightIn, T dop, float swing, float swingAmount, float parTicks, float age, float headYaw, float headPitch) {
        ItemStack itemstack = dop.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack.getItem() == Items.ELYTRA) {
            ResourceLocation resLoc;
            AbstractClientPlayer clientPlayer = dop.getClientPlayer();
            if (clientPlayer.isElytraLoaded() && clientPlayer.getElytraTextureLocation() != null) {
                resLoc = clientPlayer.getElytraTextureLocation();
            } else if (clientPlayer.isCapeLoaded() && clientPlayer.getCloakTextureLocation() != null
                    && clientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
                resLoc = clientPlayer.getCloakTextureLocation();
            } else {
                resLoc = new ResourceLocation("texture/entity/elytra.png");
            }
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.0D, 0.125D);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(dop, swing, swingAmount, age, headYaw, headPitch);
            VertexConsumer vertex = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(resLoc), false, itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertex, lightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}

