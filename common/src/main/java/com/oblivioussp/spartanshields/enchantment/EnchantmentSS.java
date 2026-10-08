package com.oblivioussp.spartanshields.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class EnchantmentSS {
   public abstract void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level);
}
