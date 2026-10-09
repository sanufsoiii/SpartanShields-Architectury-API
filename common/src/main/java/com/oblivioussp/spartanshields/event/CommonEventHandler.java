package com.oblivioussp.spartanshields.event;

import com.oblivioussp.spartanshields.enchantment.PaybackEnchantment;
import com.oblivioussp.spartanshields.enchantment.SpikesEnchantment;
import com.oblivioussp.spartanshields.init.ModEnchantments;
import com.oblivioussp.spartanshields.item.IDamageShield;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CommonEventHandler {
   public static void register() {
      EntityEvent.LIVING_HURT.register(CommonEventHandler::onLivingHurt);
   }

   private static EventResult onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
      if (!(entity instanceof Player player)) {
         return EventResult.pass();
      }

      ItemStack activeStack = player.getUseItem();
      if (activeStack.isEmpty() || !(activeStack.getItem() instanceof ShieldBaseItem)) {
         return EventResult.pass();
      }

      Entity directEntity = source.getDirectEntity();

      int spikesLevel = ModEnchantments.getEnchantmentLevel(activeStack, ModEnchantments.SPIKES);
      if (spikesLevel > 0) {
         SpikesEnchantment.handleUserAttacked(player, source.getEntity(), amount, spikesLevel);
      }

      int paybackLevel = ModEnchantments.getEnchantmentLevel(activeStack, ModEnchantments.PAYBACK);
      if (paybackLevel > 0) {
         PaybackEnchantment.handleUserAttacked(player, source.getEntity(), amount, paybackLevel);
      }

      if (amount >= 3.0F && activeStack.getItem() instanceof IDamageShield damageShield && directEntity instanceof LivingEntity) {
         damageShield.damageShield(activeStack, player, (LivingEntity) directEntity, amount);
      }

      return EventResult.pass();
   }

   private CommonEventHandler() {
   }
}
