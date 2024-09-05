package net.tinyfoes.common.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.tinyfoes.common.util.TinyFoesResLoc;

public class ModEntityTypeTags {
	public static final TagKey<EntityType<?>> BABYFICATION_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE,
			new TinyFoesResLoc("babyfication_blacklist"));
}
