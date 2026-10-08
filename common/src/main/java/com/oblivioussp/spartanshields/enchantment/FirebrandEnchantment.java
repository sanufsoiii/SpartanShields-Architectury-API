package com.oblivioussp.spartanshields.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class FirebrandEnchantment extends Enchantment {
   public FirebrandEnchantment(Enchantment.Rarity rarityIn, EquipmentSlot... slots) {
      super(rarityIn, EnchantmentSS.TYPE_SHIELD, slots);
   }

   @Override
   public int getMinCost(int enchantmentLevel) {
      return 10 + 20 * (enchantmentLevel - 1);
   }

   @Override
   public int getMaxCost(int enchantmentLevel) {
      return this.getMinCost(enchantmentLevel) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 2;
   }
}
