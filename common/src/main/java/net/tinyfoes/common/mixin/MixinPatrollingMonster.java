package net.tinyfoes.common.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PatrollingMonster.class)
public abstract class MixinPatrollingMonster extends Monster {
	protected MixinPatrollingMonster(EntityType<? extends Raider> entityType, Level level) {
		super(entityType, level);
	}

}
