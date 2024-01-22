package net.tinyallies.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.player.Player;
import net.tinyallies.client.model.BabifyerBlobModel;
import net.tinyallies.client.renderer.BlobRenderer;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.util.TinyAlliesResLoc;

@Environment(EnvType.CLIENT)
public class TinyAlliesClientCommon {
	public static ModelLayerLocation BLOB;

	public static void preClientInit() {
		EntityRendererRegistry.register(ModEntities.BLOB, BlobRenderer::new);
		BLOB = new ModelLayerLocation(new TinyAlliesResLoc("blob"), "main");
		EntityModelLayerRegistry.register(BLOB, BabifyerBlobModel::createBodyLayer);
	}

	public static Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}