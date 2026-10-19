package com.oblivioussp.spartanshields.event;

import com.oblivioussp.spartanshields.enchantment.EnchantmentSS;
import com.oblivioussp.spartanshields.item.IDamageShield;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

/**
 * Note: this replaces the Forge {@code LivingAttackEvent} subscriber. Architectury's
 * {@link EntityEvent#LIVING_HURT} is bridged from {@code LivingAttackEvent} on Forge, so the
 * trigger point and {@code getAmount()} semantics are unchanged.
 */
public class CommonEventHandler {
   public static void register() {
      EntityEvent.LIVING_HURT.register(CommonEventHandler::onLivingHurt);
   }

   private static EventResult onLivingHurt(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source, float amount) {
      if (!(entity instanceof Player player)) {
         return EventResult.pass();
      }

      ItemStack activeStack = player.getUseItem();
      if (activeStack.isEmpty() || !(activeStack.getItem() instanceof ShieldBaseItem)) {
         return EventResult.pass();
      }

      Entity directEntity = source.getDirectEntity();

      Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(activeStack);
      for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
         if (enchantment.getKey() instanceof EnchantmentSS shieldEnchantment) {
            shieldEnchantment.onUserAttacked(player, source.getEntity(), amount, enchantment.getValue());
         }
      }

      if (amount >= 3.0F && activeStack.getItem() instanceof IDamageShield damageShield && directEntity instanceof LivingEntity) {
         damageShield.damageShield(activeStack, player, (LivingEntity) directEntity, amount);
      }

      return EventResult.pass();
   }

   private CommonEventHandler() {
   }
}
