package com.lgow.endofherobrine.enchantment;

import com.lgow.endofherobrine.entity.ModMobTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModDamageEnchantment extends Enchantment {

    public ModDamageEnchantment(Rarity rarity, EquipmentSlot... slotType) {
        super(rarity, EnchantmentCategory.WEAPON, slotType);
    }

    public int getMinCost(int level) {
        return 5 + (level - 1) * 8;
    }

    public int getMaxCost(int level) {
        return this.getMinCost(level) + 50;
    }

    public int getMaxLevel() {
        return 1;
    }


    public float getDamageBonus(int level, MobType mobType) {
        if (mobType == ModMobTypes.POSSESSED){
            return (float)level * 3f;
        }else if (mobType == ModMobTypes.HEROBRINE){
            return (float)level * 2.5f;
        }else {
            return (float)level * 1.8F;
        }
    }

    public boolean checkCompatibility(Enchantment ench) {
        return !(ench instanceof DamageEnchantment);
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}
