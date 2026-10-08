package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
   public static final DeferredRegister<SoundEvent> REGISTER =
         DeferredRegister.create(SpartanShields.ID, Registries.SOUND_EVENT);

   public static final ResourceLocation LOC_SHIELD_BASH_HIT = ResourceLocation.fromNamespaceAndPath(SpartanShields.ID, "shield.bash.hit");
   public static final ResourceLocation LOC_SHIELD_BASH_MISS = ResourceLocation.fromNamespaceAndPath(SpartanShields.ID, "shield.bash.miss");
   public static final ResourceLocation LOC_SHIELD_PAYBACK_CHARGE = ResourceLocation.fromNamespaceAndPath(SpartanShields.ID, "shield.payback.charge");

   public static final RegistrySupplier<SoundEvent> SHIELD_BASH_HIT =
         REGISTER.register("shield.bash.hit", () -> SoundEvent.createVariableRangeEvent(LOC_SHIELD_BASH_HIT));
   public static final RegistrySupplier<SoundEvent> SHIELD_BASH_MISS =
         REGISTER.register("shield.bash.miss", () -> SoundEvent.createVariableRangeEvent(LOC_SHIELD_BASH_MISS));
   public static final RegistrySupplier<SoundEvent> SHIELD_PAYBACK_CHARGE =
         REGISTER.register("shield.payback.charge", () -> SoundEvent.createVariableRangeEvent(LOC_SHIELD_PAYBACK_CHARGE));

   public static void register() {
      REGISTER.register();
   }

   private ModSounds() {
   }
}
