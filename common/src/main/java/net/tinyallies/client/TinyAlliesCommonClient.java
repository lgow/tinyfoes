package net.tinyallies.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.tinyallies.client.renderer.*;
import net.tinyallies.client.renderer.projectiles.BlobRenderer;
import net.tinyallies.entity.ModEntities;

@Environment(EnvType.CLIENT)
public class TinyAlliesCommonClient {
	public static void preClientInit() {
		//renderers
		EntityRendererRegistry.register(ModEntities.CREEPY, BabyCreeperRender::new);
		EntityRendererRegistry.register(ModEntities.SKELLY, BabySkeletonRenderer::new);
		EntityRendererRegistry.register(ModEntities.ENDERBOY, BabyEnderManRender::new);
		EntityRendererRegistry.register(ModEntities.SPIDEY, BabySpiderRender::new);
		EntityRendererRegistry.register(ModEntities.ZOMBY, BabyZombieRenderer::new);
		EntityRendererRegistry.register(ModEntities.BLOB, BlobRenderer::new);
	}

	public static Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}