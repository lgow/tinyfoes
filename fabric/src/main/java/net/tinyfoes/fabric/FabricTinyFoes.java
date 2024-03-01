package net.tinyfoes.fabric;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.tinyfoes.common.TinyFoesCommon;
import net.tinyfoes.common.client.renderer.BlobRenderer;
import net.tinyfoes.common.entity.ModEntities;
import net.tinyfoes.common.util.ModUtil;
import net.tinyfoes.common.util.TinyFoesResLoc;

import static net.tinyfoes.common.util.ModUtil.TAB_ICON;

public class FabricTinyFoes implements ModInitializer {
	@Override
	public void onInitialize() {
		EntityRendererRegistry.register(ModEntities.BLOB, BlobRenderer::new);
		TinyFoesCommon.init();
		FabricItemGroupBuilder.create(new TinyFoesResLoc("tiny_tab")).appendItems((n) -> {
			n.addAll(ModUtil.TAB_ITEM_LIST);
		}).icon(() -> TAB_ICON).build();
	}
}
