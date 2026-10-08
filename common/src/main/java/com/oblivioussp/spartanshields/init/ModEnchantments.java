package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ModEnchantments {
   public static final ResourceKey<Enchantment> SPIKES =
         ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(SpartanShields.ID, "spikes"));
   public static final ResourceKey<Enchantment> FIREBRAND =
         ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(SpartanShields.ID, "firebrand"));
   public static final ResourceKey<Enchantment> PAYBACK =
         ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(SpartanShields.ID, "payback"));

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
   }

   private ModEnchantments() {
   }
}
