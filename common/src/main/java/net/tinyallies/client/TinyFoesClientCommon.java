package net.tinyallies.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.player.Player;
import net.tinyallies.client.model.BabyfierBlobModel;
import net.tinyallies.client.renderer.BlobRenderer;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.util.TinyFoesResLoc;

@Environment(EnvType.CLIENT)
public class TinyFoesClientCommon {
	public static ModelLayerLocation BLOB;

	public static void preClientInit() {
		EntityRendererRegistry.register(ModEntities.BLOB, BlobRenderer::new);
		BLOB = new ModelLayerLocation(new TinyFoesResLoc("blob"), "main");
		EntityModelLayerRegistry.register(BLOB, BabyfierBlobModel::createBodyLayer);
	}

	public static Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}