package net.tinyallies.fabric;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.tinyallies.common.TinyFoesCommon;
import net.tinyallies.common.client.renderer.BlobRenderer;
import net.tinyallies.common.entity.ModEntities;
import net.tinyallies.common.util.ModUtil;
import net.tinyallies.common.util.TinyFoesResLoc;
import net.tinyallies.fabric.persistent_data.BabyficationComponent;
import net.tinyallies.fabric.persistent_data.BooleanComponent;

import static net.tinyallies.common.util.ModUtil.TAB_ICON;

public class FabricTinyFoes implements ModInitializer, EntityComponentInitializer {
	public static final ComponentKey<BabyficationComponent> BABYFICATION =
			ComponentRegistry.getOrCreate(new TinyFoesResLoc( "babyfication"), BabyficationComponent.class);
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
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(BABYFICATION, BabyficationComponent::new, RespawnCopyStrategy.INVENTORY);
	}

	public static void setBabyfication(Entity provider, boolean b) { // anything will work, as long as a module allows it!
		BABYFICATION.get(provider).setValue(b);
	}

	public static boolean getBabyfication(Entity provider) { // anything will work, as long as a module allows it!
		return BABYFICATION.get(provider).getValue();
	}
}
