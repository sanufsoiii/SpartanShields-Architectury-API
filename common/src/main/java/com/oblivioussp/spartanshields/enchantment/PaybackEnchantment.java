package com.oblivioussp.spartanshields.enchantment;

import com.oblivioussp.spartanshields.init.ModSounds;
import com.oblivioussp.spartanshields.item.PaybackDamage;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class PaybackEnchantment extends EnchantmentSS {
   public static final String NBT_PAYBACK_DMG = PaybackDamage.NBT_PAYBACK_DMG;

   public PaybackEnchantment(Enchantment.Rarity rarityIn, EquipmentSlot... slots) {
      super(rarityIn, EnchantmentSS.TYPE_SHIELD, slots);
   }

   @Override
   public int getMinCost(int enchantmentLevel) {
      return 15 + (enchantmentLevel - 1) * 7;
   }

   @Override
   public int getMaxCost(int enchantmentLevel) {
      return this.getMinCost(enchantmentLevel) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 4;
   }

   @Override
   public void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level) {
      ItemStack activeStack = user.getUseItem();
      if (!activeStack.isEmpty()) {
         float currentDmg = activeStack.getOrCreateTag().getFloat(NBT_PAYBACK_DMG);
         float maxDmg = this.getMaxDamageCapacity(level);
         float absorbedDmg = damage * this.getAbsorbedDamageRatio();
         currentDmg = Mth.clamp(currentDmg + absorbedDmg, 0.0F, maxDmg);
         activeStack.getTag().putFloat(NBT_PAYBACK_DMG, currentDmg);
         if (currentDmg == maxDmg) {
            user.level().playSound(null, user.getX(), user.getY(), user.getZ(),
                  ModSounds.SHIELD_PAYBACK_CHARGE.get(), user.getSoundSource(), 0.5F, 2.0F);
         }
      }
   }

   protected float getMaxDamageCapacity(int level) {
      return 2.0F * level;
   }

   protected float getAbsorbedDamageRatio() {
      return 0.5F;
   }
}
