package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.enchantment.FirebrandEnchantment;
import com.oblivioussp.spartanshields.enchantment.PaybackEnchantment;
import com.oblivioussp.spartanshields.enchantment.SpikesEnchantment;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
   public static final DeferredRegister<Enchantment> REGISTER =
         DeferredRegister.create(SpartanShields.ID, Registries.ENCHANTMENT);

   public static final RegistrySupplier<Enchantment> SPIKES = REGISTER.register("spikes",
         () -> new SpikesEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
   public static final RegistrySupplier<Enchantment> FIREBRAND = REGISTER.register("firebrand",
         () -> new FirebrandEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
   public static final RegistrySupplier<Enchantment> PAYBACK = REGISTER.register("payback",
         () -> new PaybackEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));

   public static void register() {
      REGISTER.register();
   }

   private ModEnchantments() {
   }
}
