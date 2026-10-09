package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.enchantment.FirebrandEnchantment;
import com.oblivioussp.spartanshields.enchantment.PaybackEnchantment;
import com.oblivioussp.spartanshields.enchantment.SpikesEnchantment;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ModEnchantments {
   public static final DeferredRegister<Enchantment> REGISTER =
         DeferredRegister.create(SpartanShields.ID, Registries.ENCHANTMENT);

   public static final ResourceKey<Enchantment> SPIKES =
         ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation(SpartanShields.ID, "spikes"));
   public static final ResourceKey<Enchantment> FIREBRAND =
         ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation(SpartanShields.ID, "firebrand"));
   public static final ResourceKey<Enchantment> PAYBACK =
         ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation(SpartanShields.ID, "payback"));

   public static final RegistrySupplier<Enchantment> SPIKES_SUPPLIER = REGISTER.register("spikes",
         () -> new SpikesEnchantment(Enchantment.definition(
               ItemTags.DURABILITY_ENCHANTABLE, 5, 3,
               Enchantment.dynamicCost(10, 20),
               Enchantment.dynamicCost(60, 20),
               4,
               EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)));

   public static final RegistrySupplier<Enchantment> FIREBRAND_SUPPLIER = REGISTER.register("firebrand",
         () -> new FirebrandEnchantment(Enchantment.definition(
               ItemTags.DURABILITY_ENCHANTABLE, 2, 2,
               Enchantment.dynamicCost(10, 20),
               Enchantment.dynamicCost(60, 20),
               4,
               EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)));

   public static final RegistrySupplier<Enchantment> PAYBACK_SUPPLIER = REGISTER.register("payback",
         () -> new PaybackEnchantment(Enchantment.definition(
               ItemTags.DURABILITY_ENCHANTABLE, 1, 4,
               Enchantment.dynamicCost(10, 20),
               Enchantment.dynamicCost(60, 20),
               8,
               EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)));

   public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> key) {
      if (stack.isEmpty()) {
         return 0;
      }
      ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
      for (var entry : enchantments.entrySet()) {
         if (entry.getKey().is(key)) {
            return entry.getIntValue();
         }
      }
      return 0;
   }

   public static void register() {
      REGISTER.register();
   }

   private ModEnchantments() {
   }
}
