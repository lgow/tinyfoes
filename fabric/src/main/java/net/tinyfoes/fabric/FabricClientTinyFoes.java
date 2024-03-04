package net.tinyfoes.fabric;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.tinyfoes.common.client.TinyFoesClientCommon;
import net.tinyfoes.common.client.renderer.BlobRenderer;
import net.tinyfoes.common.entity.ModEntities;

public class FabricClientTinyFoes implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		TinyFoesClientCommon.preClientInit();
		EntityRendererRegistry.register(ModEntities.BLOB, BlobRenderer::new);
	}
}