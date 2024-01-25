package net.tinyallies.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.tinyallies.effect.Babyfication;
import net.tinyallies.util.TinyAlliesResLoc;

public class ModEffects {
	public static final MobEffect BABYFICATION;

	static {
		BABYFICATION = registerEffect("babyfication", new Babyfication());
	}

	private static MobEffect registerEffect(String name, MobEffect effect) {
		return Registry.register(Registry.MOB_EFFECT, new TinyAlliesResLoc(name), effect);
	}

	public static void register() {
	}
}
