package com.oblivioussp.spartanshields.enchantment;

import com.oblivioussp.spartanshields.init.ModEnchantments;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpikesEnchantment extends EnchantmentSS {
   public SpikesEnchantment(EnchantmentDefinition definition) {
      super(definition);
   }

   @Override
   public void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level) {
      handleUserAttacked(user, attacker, damage, level);
   }

   public static void handleUserAttacked(LivingEntity user, Entity attacker, float damage, int level) {
      RandomSource random = user.getRandom();
      ItemStack activeStack = user.getUseItem();
      if (!activeStack.isEmpty() && ModEnchantments.getEnchantmentLevel(activeStack, ModEnchantments.SPIKES) > 0) {
         EquipmentSlot slot = user.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
         if (shouldHit(level, random)) {
            float spikesDamage = getDamage(level, random);
            if (attacker != null) {
               attacker.hurt(user.damageSources().thorns(user), spikesDamage);
            }

            if (!activeStack.isEmpty()) {
               activeStack.hurtAndBreak(9, user, slot);
            }
         } else if (!activeStack.isEmpty()) {
            activeStack.hurtAndBreak(3, user, slot);
         }
      }
   }

   public static boolean shouldHit(int level, RandomSource rnd) {
      return level > 0 && rnd.nextFloat() < 0.2F * level;
   }

   public static int getDamage(int level, RandomSource rnd) {
      return level == 1 ? 2 : 2 + rnd.nextInt(level - 1);
   }
}
