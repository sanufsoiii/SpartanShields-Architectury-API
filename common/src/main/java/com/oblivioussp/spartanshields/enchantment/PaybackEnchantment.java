package com.oblivioussp.spartanshields.enchantment;

import com.oblivioussp.spartanshields.init.ModSounds;
import com.oblivioussp.spartanshields.item.PaybackDamage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class PaybackEnchantment extends EnchantmentSS {
   public static final String NBT_PAYBACK_DMG = PaybackDamage.NBT_PAYBACK_DMG;

   @Override
   public void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level) {
      handleUserAttacked(user, attacker, damage, level);
   }

   public static void handleUserAttacked(LivingEntity user, Entity attacker, float damage, int level) {
      ItemStack activeStack = user.getUseItem();
      if (!activeStack.isEmpty()) {
         CustomData customData = activeStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
         float currentDmg = customData.copyTag().getFloatOr(NBT_PAYBACK_DMG, 0.0f);
         float maxDmg = getMaxDamageCapacity(level);
         float absorbedDmg = damage * getAbsorbedDamageRatio();
         float newDmg = Mth.clamp(currentDmg + absorbedDmg, 0.0F, maxDmg);
         CustomData.update(DataComponents.CUSTOM_DATA, activeStack, tag -> tag.putFloat(NBT_PAYBACK_DMG, newDmg));
         if (newDmg == maxDmg) {
            user.level().playSound(null, user.getX(), user.getY(), user.getZ(),
                  ModSounds.SHIELD_PAYBACK_CHARGE.get(), user.getSoundSource(), 0.5F, 2.0F);
         }
      }
   }

   public static float getMaxDamageCapacity(int level) {
      return 2.0F * level;
   }

   public static float getAbsorbedDamageRatio() {
      return 0.5F;
   }
}
