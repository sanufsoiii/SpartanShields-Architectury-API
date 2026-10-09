package com.oblivioussp.spartanshields.enchantment;

import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public abstract class EnchantmentSS extends Enchantment {
   protected EnchantmentSS(EnchantmentDefinition definition) {
      super(definition);
   }

   @Override
   public boolean canEnchant(ItemStack stack) {
      return stack.getItem() instanceof ShieldBaseItem;
   }

   public abstract void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level);
}
