package net.tinyfoes.common.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.tinyfoes.common.TinyFoesCommon;
import net.tinyfoes.common.effect.Babyfication;
import net.tinyfoes.common.util.TinyFoesResLoc;

import java.util.function.Supplier;

public class ModEffects {
	public static final RegistrySupplier<MobEffect> BABYFICATION;

	private static final Registrar<MobEffect> MOB_EFFECTS = DeferredRegister.create(TinyFoesCommon.MODID, Registry.MOB_EFFECT_REGISTRY).getRegistrar();
private static final Registrar<Potion> POTIONS = DeferredRegister.create(TinyFoesCommon.MODID, Registry.POTION_REGISTRY).getRegistrar();
	public static final RegistrySupplier<Potion> BABYFICATION_POTION;

	static {
		BABYFICATION = registerEffect("babyfication", Babyfication::new);
		BABYFICATION_POTION = POTIONS.register(new TinyFoesResLoc("babyfication"), () -> new Potion(new MobEffectInstance(ModEffects.BABYFICATION.get(), 260)));
	}

	private static RegistrySupplier<MobEffect> registerEffect(String name, Supplier<MobEffect> effect){
		return MOB_EFFECTS.register(new TinyFoesResLoc(name), effect);
	}

	public static void register() {
	}
}
