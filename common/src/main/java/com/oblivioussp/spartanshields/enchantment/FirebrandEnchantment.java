package com.oblivioussp.spartanshields.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class FirebrandEnchantment extends EnchantmentSS {
   public FirebrandEnchantment(EnchantmentDefinition definition) {
      super(definition);
   }

   @Override
   public void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level) {
   }
}
