package com.lgow.endofherobrine.client.renderer;

import com.lgow.endofherobrine.Main;
import com.lgow.endofherobrine.block.ModSkullBlock;
import com.lgow.endofherobrine.client.model.DopModel;
import com.lgow.endofherobrine.client.model.PosArmorStandModel;
import com.lgow.endofherobrine.client.model.PosPigmanModel;
import com.lgow.endofherobrine.client.model.PosVillagerModel;
import com.lgow.endofherobrine.client.model.modellayer.ModModelLayers;
import com.lgow.endofherobrine.client.renderer.entity.*;
import com.lgow.endofherobrine.entity.possessed.PosArmorStand;
import com.lgow.endofherobrine.tileentities.BlockEntityInit;
import com.lgow.endofherobrine.util.ModResourceLocation;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
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
		EntityRenderers.register(P_ARMOR_STAND.get(), PosArmorStandRenderer::new);
		EntityRenderers.register(P_CHICKEN.get(), PosChickenRender::new);
		EntityRenderers.register(P_COW.get(), PosCowRender::new);
		EntityRenderers.register(P_HUSK.get(), PosHuskRender::new);
		EntityRenderers.register(P_PIG.get(), PosPigRender::new);
		EntityRenderers.register(PIGMAN.get(), PosPigmanRender::new);
		EntityRenderers.register(P_RABBIT.get(), PosRabbitRender::new);
		EntityRenderers.register(P_SHEEP.get(), PosSheepRender::new);
		EntityRenderers.register(P_SKELETON.get(), PosSkeletonRender::new);
		EntityRenderers.register(P_SILVERFISH.get(), PosSilverfishRender::new);
		EntityRenderers.register(P_STRAY.get(), PosStrayRender::new);
		EntityRenderers.register(P_VILlAGER.get(), PosVillagerRenderer::new);
		EntityRenderers.register(P_ZOMBIE.get(), PosZombieRender::new);
		EntityRenderers.register(P_ZOMBIE_VILLAGER.get(), PosZombieVillagerRenderer::new);
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
		event.registerLayerDefinition(ModModelLayers.ARMOR_STAND, PosArmorStandModel::createBodyLayer);
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
