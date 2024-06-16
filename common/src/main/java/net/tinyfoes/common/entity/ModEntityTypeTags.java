package net.tinyfoes.common.entity;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.tinyfoes.common.util.TinyFoesResLoc;

public class ModEntityTypeTags {
	public static final TagKey<EntityType<?>> BABYFICATION_BLACKLIST = TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
			new TinyFoesResLoc("babyfication_blacklist"));

//	private static TagKey<EntityType<?>> create(String string) {
//		return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new TinyFoesResLoc(string));
//	}
}
