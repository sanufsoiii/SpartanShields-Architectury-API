package com.oblivioussp.spartanshields.enchantment;

import com.oblivioussp.spartanshields.init.ModEnchantments;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SpikesEnchantment extends EnchantmentSS {
   public SpikesEnchantment(Enchantment.Rarity rarityIn, EquipmentSlot... slots) {
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
      return 3;
   }

   @Override
   public void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level) {
      RandomSource random = user.getRandom();
      ItemStack itemStack = EnchantmentHelper.getRandomItemWith(ModEnchantments.SPIKES.get(), user).getValue();
      ItemStack activeStack = user.getUseItem();
      if (!activeStack.isEmpty() && activeStack == itemStack) {
         if (shouldHit(level, random)) {
            float spikesDamage = getDamage(level, random);
            if (attacker != null) {
               attacker.hurt(user.damageSources().thorns(user), spikesDamage);
            }

            if (!itemStack.isEmpty()) {
               itemStack.hurtAndBreak(9, user, livingEntity -> livingEntity.broadcastBreakEvent(livingEntity.getUsedItemHand()));
            }
         } else if (!itemStack.isEmpty()) {
            itemStack.hurtAndBreak(3, user, livingEntity -> livingEntity.broadcastBreakEvent(livingEntity.getUsedItemHand()));
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
