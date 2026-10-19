package com.oblivioussp.spartanshields.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Implemented by shields that do not consume durability but pay with an external resource
 * (Botania mana).
 * <p>
 * Note: the Forge release hooked this through Forge-only
 * {@code Item#damageItem(ItemStack, int, LivingEntity, Consumer)}, which vanilla/Fabric never
 * calls. The behaviour is now reached through a loader-agnostic mixin on
 * {@code ItemStack#hurtAndBreak} (see {@code mixin.MixinItemStack}), so it behaves identically
 * on both loaders.
 */
public interface IManaDamageShield {
   /**
    * @return {@code true} when the damage was paid for and the stack must keep its durability
    */
   boolean payForDamage(ItemStack stack, int amount, LivingEntity entity);
}
