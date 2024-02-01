package net.tinyallies.common.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.player.Player;
import net.tinyallies.common.client.model.BabyfierBlobModel;
import net.tinyallies.common.client.renderer.BlobRenderer;
import net.tinyallies.common.entity.ModEntities;
import net.tinyallies.common.util.TinyFoesResLoc;

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