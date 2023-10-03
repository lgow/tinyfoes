package net.tinyallies.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tinyallies.entity.ModEntities;
import net.tinyallies.util.ModUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {
	protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract boolean isBaby();

	@Override
	protected void customServerAiStep() {
		if (this.isBaby()) { ModUtil.babifyMob(this); }
		super.customServerAiStep();
	}
}

