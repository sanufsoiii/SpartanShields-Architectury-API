package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.compat.BotaniaCompat;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BotaniaShieldItem extends BasicShieldItem implements IManaDamageShield {
   protected int manaPerDamage = 60;

   public BotaniaShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, int manaPerDamage, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop);
      this.manaPerDamage = manaPerDamage;
   }

   public int getManaPerDamage() {
      return this.manaPerDamage;
   }

   @Override
   public void inventoryTick(ItemStack stack, net.minecraft.server.level.ServerLevel level, Entity entityIn, net.minecraft.world.entity.EquipmentSlot slot) {
      if (entityIn instanceof Player player
            && stack.getDamageValue() > 0
            && BotaniaCompat.requestManaForTool(stack, player, this.manaPerDamage * 2)) {
         stack.setDamageValue(stack.getDamageValue() - 1);
      }
   }

   @Override
   public boolean payForDamage(ItemStack stack, int amount, LivingEntity entity) {
      return entity instanceof Player player && BotaniaCompat.requestManaForTool(stack, player, amount * this.manaPerDamage);
   }
}
