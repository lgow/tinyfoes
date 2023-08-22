package com.lgow.endofherobrine.client.renderer;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.block.BlockInit;
import com.lgow.endofherobrine.block.ModSkullBlock;
import com.lgow.endofherobrine.client.model.DopModel;
import com.lgow.endofherobrine.client.model.PosPigmanModel;
import com.lgow.endofherobrine.client.model.PosVillagerModel;
import com.lgow.endofherobrine.client.model.modellayer.ModModelLayers;
import com.lgow.endofherobrine.client.renderer.entity.*;
import com.lgow.endofherobrine.tileentities.BlockEntityInit;
import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.lgow.endofherobrine.entity.EntityInit.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderSetup {
	private static void registerRenders() {
		EntityRenderers.register(HEROBRINE_BOSS.get(), HerobrineRender::new);
		EntityRenderers.register(BUILDER.get(), BuilderRender::new);
		EntityRenderers.register(LURKER.get(), HerobrineRender::new);
		EntityRenderers.register(CHICKEN.get(), PosChickenRender::new);
		EntityRenderers.register(COW.get(), PosCowRender::new);
		EntityRenderers.register(HUSK.get(), PosHuskRender::new);
		EntityRenderers.register(PIG.get(), PosPigRender::new);
		EntityRenderers.register(PIGMAN.get(), PosPigmanRender::new);
		EntityRenderers.register(RABBIT.get(), PosRabbitRender::new);
		EntityRenderers.register(SHEEP.get(), PosSheepRender::new);
		EntityRenderers.register(SKELETON.get(), PosSkeletonRender::new);
		EntityRenderers.register(SILVERFISH.get(), PosSilverfishRender::new);
		EntityRenderers.register(STRAY.get(), PosStrayRender::new);
		EntityRenderers.register(VILLAGER.get(), PosVillagerRenderer::new);
		EntityRenderers.register(ZOMBIE.get(), PosZombieRender::new);
		EntityRenderers.register(ZOMBIE_VILLAGER.get(), PosZombieVillagerRenderer::new);
		BlockEntityRenderers.register(BlockEntityInit.SKULL.get(), SkullBlockRenderer::new);
	}

	//attaches the head skins to the skull types
	private static void registerHeadSkins() {
		SkullBlockRenderer.SKIN_BY_TYPE.put(ModSkullBlock.Types.HEROBRINE,
				new ModResourceLocation("textures/entity/herobrine.png"));
		SkullBlockRenderer.SKIN_BY_TYPE.put(ModSkullBlock.Types.CURSED,
				new ModResourceLocation("textures/block/cursed_head.png"));
	}

	@SubscribeEvent
	public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.PIGMAN, PosPigmanModel::createSnoutLayer);
		event.registerLayerDefinition(ModModelLayers.VILLAGER, PosVillagerModel::createUncrossedArmsLayer);
		event.registerLayerDefinition(ModModelLayers.DOPPELGANGER, DopModel::createBody);
	}

	@SubscribeEvent
	public static void registerSkullModels(EntityRenderersEvent.CreateSkullModels event) {
		event.registerSkullModel(ModSkullBlock.Types.HEROBRINE,
				new SkullModel(event.getEntityModelSet().bakeLayer(ModelLayers.PLAYER_HEAD)));
		event.registerSkullModel(ModSkullBlock.Types.CURSED,
				new SkullModel(event.getEntityModelSet().bakeLayer(ModelLayers.PLAYER_HEAD)));
	}

	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent event) {
		registerHeadSkins();
		registerRenders();
	}
}
