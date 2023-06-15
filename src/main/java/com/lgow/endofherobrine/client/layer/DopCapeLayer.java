package com.lgow.endofherobrine.client.layer;

import com.lgow.endofherobrine.entity.herobrine.Doppelganger;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DopCapeLayer<T extends Doppelganger, M extends PlayerModel<T>> extends RenderLayer<T, M> {

    public DopCapeLayer(RenderLayerParent<T, M>rendererIn) { super(rendererIn);}

    @Override
    public void render(PoseStack poseStack, MultiBufferSource source, int lightIn, T dop, float swing, float swingAmount, float parTicks, float age, float headYaw, float headPitch) {
        AbstractClientPlayer player = dop.getClientPlayer();
        ItemStack itemStack = dop.getItemBySlot(EquipmentSlot.CHEST);
        if(player != null && player.isCapeLoaded() && player.isModelPartShown(PlayerModelPart.CAPE)
                && player.getCloakTextureLocation() != null && itemStack.getItem() != Items.ELYTRA) {
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.0D, 0.125D);
            double d0 = Mth.lerp(parTicks, dop.xCloakO, dop.xCloak) - Mth.lerp(parTicks, dop.xo, dop.getX());
            double d1 = Mth.lerp(parTicks, dop.yCloakO, dop.yCloak) - Mth.lerp(parTicks, dop.yo, dop.getY());
            double d2 = Mth.lerp(parTicks, dop.zCloakO, dop.zCloak) - Mth.lerp(parTicks, dop.zo, dop.getZ());
            float f = dop.yBodyRotO + (dop.yBodyRot - dop.yBodyRotO);
            double d3 = Mth.sin(f * ((float) Math.PI / 180F));
            double d4 = -Mth.cos(f * ((float) Math.PI / 180F));
            float f1 = (float) d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F);
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F);
            float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
            f3 = Mth.clamp(f3, -20.0F, 20.0F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            float f4 = Mth.lerp(parTicks, dop.oBob, dop.bob);
            f1 += Mth.sin(Mth.lerp(parTicks, dop.walkDistO, dop.walkDist) * 6.0F) * 32.0F * f4;
            if (dop.isCrouching()) {
                f1 += 25.0F;
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
            poseStack.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
            VertexConsumer vertex = source.getBuffer(RenderType.entitySolid(player.getCloakTextureLocation()));
            this.getParentModel().renderCloak(poseStack, vertex, lightIn, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}
