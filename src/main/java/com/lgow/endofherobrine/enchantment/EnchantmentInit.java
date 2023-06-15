package com.lgow.endofherobrine.enchantment;

import com.lgow.endofherobrine.registries.ModRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
	public static final RegistryObject<Enchantment> BLESSING = ModRegistries.MOD_ENCHANTMENTS.register("blessing",
			() -> new ModDamageEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

	public static void register() { }
}
