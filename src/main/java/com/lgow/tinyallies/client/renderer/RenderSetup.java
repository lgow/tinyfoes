package com.lgow.tinyallies.client.renderer;

import com.lgow.tinyallies.Main;
import com.lgow.tinyallies.client.model.BabyCreeperModel;
import com.lgow.tinyallies.client.model.BabySpiderModel;
import com.lgow.tinyallies.client.model.ModModelLayers;
import com.lgow.tinyallies.client.renderer.projectiles.BlobRenderer;
import com.lgow.tinyallies.entity.EntityInit;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderSetup {
	private static void registerRenders() {
		EntityRenderers.register(EntityInit.CREEPY.get(), BabyCreeperRender::new);
		EntityRenderers.register(EntityInit.SKELLY.get(), BabySkeletonRenderer::new);
		EntityRenderers.register(EntityInit.ENDERBOY.get(), BabyEndermanRender::new);
		EntityRenderers.register(EntityInit.SPIDEY.get(), BabySpiderRender::new);
		EntityRenderers.register(EntityInit.ZOMBY.get(), BabyZombieRenderer::new);
		EntityRenderers.register(EntityInit.BLOB.get(), BlobRenderer::new);
	}
	@SubscribeEvent
	public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.CREEPER, BabyCreeperModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.SPIDER, BabySpiderModel::createSpiderBodyLayer);
//		event.registerLayerDefinition(ModModelLayers.POS_VILLAGER, PosVillagerModel::createUncrossedArmsLayer);
	}
	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent event) {
		registerRenders();
	}
}