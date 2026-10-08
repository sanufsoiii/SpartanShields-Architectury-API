package com.oblivioussp.spartanshields.mixin;

import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import com.oblivioussp.spartanshields.item.IManaDamageShield;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

/**
 * Lets shields that pay for damage with an external resource (Botania mana) keep their durability.
 * <p>
 * Porting note: the Forge release relied on {@code Item#damageItem}, a Forge-only method invoked
 * from Forge's patched {@code ItemStack#hurtAndBreak}. Vanilla 1.20.1 has neither, and Fabric never
 * calls Forge's hook, so the decision is made here instead - which keeps the behaviour identical on
 * both loaders and removes the need for a loader-specific override.
 */
@Mixin(ItemStack.class)
public abstract class MixinItemStack {
   @Inject(method = "hurtAndBreak", at = @At("HEAD"), cancellable = true)
   private <T extends LivingEntity> void spartanshields$payForDamage(int amount, T entity, Consumer<T> onBroken,
                                                                      CallbackInfo callback) {
      ItemStack self = (ItemStack) (Object) this;
      if (self.getItem() instanceof IManaDamageShield manaShield && manaShield.payForDamage(self, amount, entity)) {
         callback.cancel();
      }
   }

   /**
    * Applies the configured durability to the shields.
    * <p>
    * Porting note: the Forge release overrode {@code Item#getMaxDamage(ItemStack)} to make the
    * config's durability effective. Vanilla's {@code Item#getMaxDamage()} is {@code final}, so the
    * stack-level lookup is redirected instead - which is what both loaders actually use when they
    * damage an item. Powered shields are excluded: they track energy, not durability.
    */
   @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
   private void spartanshields$configuredDurability(CallbackInfoReturnable<Integer> callback) {
      ItemStack self = (ItemStack) (Object) this;
      if (self.getItem() instanceof ShieldBaseItem shield && !(shield instanceof FEPoweredShieldItem)) {
         callback.setReturnValue(shield.getConfiguredMaxDamage());
      }
   }
}
