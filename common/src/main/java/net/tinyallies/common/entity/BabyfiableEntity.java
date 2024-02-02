package net.tinyallies.common.entity;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public interface BabyfiableEntity {
	AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(
			UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), "Baby speed boost", 0.5,
			AttributeModifier.Operation.MULTIPLY_BASE);

	default void $setBabyfied(boolean b) {
	}

	default void $setBaby(boolean b) {
	}

	default boolean $isBabyfied() {
		return false;
	}

	;

	boolean $isBaby();
}
