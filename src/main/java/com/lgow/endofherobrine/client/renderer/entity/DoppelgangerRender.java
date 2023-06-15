package com.lgow.endofherobrine.client.renderer.entity;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.client.layer.DopCapeLayer;
import com.lgow.endofherobrine.client.layer.DopElytraLayer;
import com.lgow.endofherobrine.client.layer.DopEyesLayer;
import com.lgow.endofherobrine.client.model.DopModel;
import com.lgow.endofherobrine.client.model.modellayer.ModModelLayers;
import com.lgow.endofherobrine.config.ModConfigs;
import com.lgow.endofherobrine.entity.herobrine.Doppelganger;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class DoppelgangerRender extends LivingEntityRenderer<Doppelganger, DopModel<Doppelganger>> {

//todo test slim arms
    public DoppelgangerRender(EntityRendererProvider.Context context) {
        super(context, new DopModel<>(context.bakeLayer(ModModelLayers.DOPPELGANGER)), 0);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new ArrowLayer<>(context, this));
        this.addLayer(new DopCapeLayer<>(this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new DopElytraLayer<>(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
        this.addLayer(new DopEyesLayer<>(this));
    }

    @Override
    protected void scale(Doppelganger dop, PoseStack poseStack, float parTicks) {
        float f = 0.9375F;
        poseStack.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(Doppelganger dop) {
        if (dop.getTargetPlayer() != null) {
            AbstractClientPlayer localPlayer = dop.getClientPlayer();
            Minecraft instance = Minecraft.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textureMap = instance.getSkinManager()
                    .getInsecureSkinInformation(localPlayer.getGameProfile());
            return textureMap.containsKey(MinecraftProfileTexture.Type.SKIN) ? (instance.getSkinManager().registerTexture(
                    textureMap.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) :
                    DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(localPlayer.getGameProfile()));
        }
        return new ResourceLocation(Main.MOD_ID, "textures/entity/layer/biped_eyes.png");
    }

    @Override
    public void render(Doppelganger dop, float rotYaw, float parTicks, PoseStack stack, MultiBufferSource source, int lightIn) {
        this.setModelProperties(dop);
        super.render(dop, rotYaw, parTicks, stack, source, lightIn);
    }

    private void setModelProperties(Doppelganger dop) {
        AbstractClientPlayer clientPlayer = dop.getClientPlayer();
        model = this.getModel();
        model.setAllVisible(true);
        if(clientPlayer!=null) {
            model.hat.visible = clientPlayer.isModelPartShown(PlayerModelPart.HAT);
            model.jacket.visible = clientPlayer.isModelPartShown(PlayerModelPart.JACKET);
            model.leftPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            model.rightPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            model.leftSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            model.rightSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            if (clientPlayer.getModelName().equals("default")) {
                model.leftSleeve.visible = false;
                model.rightSleeve.visible = false;
                model.wideLeftArm.visible = true;
                model.wideRightArm.visible = true;
            }
        }
    }

    @Override
    protected boolean shouldShowName(Doppelganger dop) {
        return (ModConfigs.SHOW_NAMETAG.get() && dop.getDisplayName().getString().equals("Herobrine")) && super.shouldShowName(dop);
    }

//    @Override
//    protected void setupRotations(Doppelganger dop, PoseStack poseStack, float age, float rotYaw, float parTicks) {
//        float f = dop.getSwimAmount(parTicks);
//         if (f > 0.0F) {
//            super.setupRotations(dop, poseStack, age, rotYaw, parTicks);
//            float f3 = dop.isInWater() || dop.isInFluidType((fluidType, height)
//                    -> dop.canSwimInFluidType(fluidType)) ? -90.0F - dop.getXRot() : -90.0F;
//            float f4 = Mth.lerp(f, 0.0F, f3);
//            poseStack.mulPose(Vector3f.XP.rotationDegrees(f4));
//            if (dop.isVisuallySwimming()) {
//                poseStack.translate(0.0D, -1.0D, 0.3F);
//            }
//        } else {
//            super.setupRotations(dop, poseStack, age, rotYaw, parTicks);
//        }
//    }
}
