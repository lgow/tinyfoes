package net.tinyfoes.common.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.tinyfoes.common.CommonTinyFoes;
import net.tinyfoes.common.effect.Babyfication;
import net.tinyfoes.common.util.TinyFoesResLoc;

import java.util.function.Supplier;

public class ModEffects {
	public static final RegistrySupplier<MobEffect> BABYFICATION;
	public static final RegistrySupplier<Potion> BABYFICATION_POTION;
	private static final Registrar<MobEffect> MOB_EFFECTS = DeferredRegister.create(CommonTinyFoes.MODID,
			Registries.MOB_EFFECT).getRegistrar();
	private static final Registrar<Potion> POTIONS = DeferredRegister.create(CommonTinyFoes.MODID,
			Registries.POTION).getRegistrar();

	static {
		BABYFICATION = registerEffect("babyfication", Babyfication::new);
		BABYFICATION_POTION = POTIONS.register(new TinyFoesResLoc("babyfication"),
				() -> new Potion(new MobEffectInstance(ModEffects.BABYFICATION.get(), 1200)));
	}

	private static RegistrySupplier<MobEffect> registerEffect(String name, Supplier<MobEffect> effect) {
		return MOB_EFFECTS.register(new TinyFoesResLoc(name), effect);
	}

	public static void register() {
	}
}
