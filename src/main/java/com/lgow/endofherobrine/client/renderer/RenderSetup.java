package com.lgow.endofherobrine.client.renderer;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.client.model.PosPigmanModel;
import com.lgow.endofherobrine.client.model.PosVillagerModel;
import com.lgow.endofherobrine.client.model.modellayer.ModModelLayers;
import com.lgow.endofherobrine.client.renderer.entity.*;
import com.lgow.endofherobrine.tileentities.TileEntityInit;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.lgow.endofherobrine.entity.EntityInit.*;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderSetup {

    private static void registerRenders(){
        EntityRenderers.register(HEROBRINE_BOSS.get(), HerobrineRender::new);
        EntityRenderers.register(BUILDER.get(), HerobrineRender::new);
        EntityRenderers.register(LURKER.get(), HerobrineRender::new);
        EntityRenderers.register(POS_CHICKEN.get(), PosChickenRender::new);
        EntityRenderers.register(POS_COW.get(), PosCowRender::new);
        EntityRenderers.register(POS_HUSK.get(), PosHuskRender::new);
        EntityRenderers.register(POS_PIG.get(), PosPigRender::new);
        EntityRenderers.register(POS_PIGMAN.get(), PosPigmanRender::new);
        EntityRenderers.register(POS_RABBIT.get(), PosRabbitRender::new);
        EntityRenderers.register(POS_SHEEP.get(), PosSheepRender::new);
        EntityRenderers.register(POS_SKELETON.get(), PosSkeletonRender::new);
        EntityRenderers.register(POS_SILVERFISH.get(), PosSilverfishRender::new);
        EntityRenderers.register(POS_STRAY.get(), PosStrayRender::new);
        EntityRenderers.register(POS_VILLAGER.get(), PosVillagerRenderer::new);
        EntityRenderers.register(POS_ZOMBIE.get(), PosZombieRender::new);
        EntityRenderers.register(POS_ZOMBIE_VILLAGER.get(), PosZombieVillagerRenderer::new);
    }

    private static void registerSkulls() {
        SkullBlockRenderer.SKIN_BY_TYPE.put(BlockInit.Types.HEROBRINE, new ResourceLocation(Main.MOD_ID, "textures/entity/herobrine.png"));
        SkullBlockRenderer.SKIN_BY_TYPE.put(BlockInit.Types.CURSED, new ResourceLocation(Main.MOD_ID, "textures/block/cursed_head.png"));
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.PIGMAN, PosPigmanModel::createSnout);
        event.registerLayerDefinition(ModModelLayers.POS_VILLAGER, PosVillagerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerSkullModels(EntityRenderersEvent.CreateSkullModels event) {
        event.registerSkullModel(BlockInit.Types.HEROBRINE, new SkullModel(event.getEntityModelSet().bakeLayer(ModelLayers.PLAYER_HEAD)));
        event.registerSkullModel(BlockInit.Types.CURSED, new SkullModel(event.getEntityModelSet().bakeLayer(ModelLayers.PLAYER_HEAD)));
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        registerSkulls();
        registerRenders();
        BlockEntityRenderers.register(TileEntityInit.SKULL.get(), SkullBlockRenderer::new);
    }
}
