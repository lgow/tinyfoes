package net.tinyallies.fabric;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.tinyallies.common.TinyFoesCommon;
import net.tinyallies.common.client.renderer.BlobRenderer;
import net.tinyallies.common.entity.ModEntities;
import net.tinyallies.common.util.ModUtil;
import net.tinyallies.common.util.TinyFoesResLoc;
import net.tinyallies.fabric.persistent_data.BooleanComponent;
import net.tinyallies.fabric.persistent_data.RandomIntComponent;

import static net.tinyallies.common.util.ModUtil.TAB_ICON;

public class FabricTinyFoes implements ModInitializer, LevelComponentInitializer {
	public static final ComponentKey<BooleanComponent> MAGIK =
			ComponentRegistry.getOrCreate(new TinyFoesResLoc( "magik"), BooleanComponent.class);
	@Override
	public void onInitialize() {
		EntityRendererRegistry.register(ModEntities.BLOB, BlobRenderer::new);
		TinyFoesCommon.init();
		FabricItemGroupBuilder.create(new TinyFoesResLoc("tiny_tab")).appendItems((n) -> {
			for (ItemLike item : ModUtil.TAB_ITEM_LIST) {
				n.add(new ItemStack(item));
			}
		}).icon(() -> TAB_ICON).build();



	}

	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
//		registry.register(MAGIK, Levevwl::new);
	}
}
