package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTabs {
   /**
    * Porting note: the tab has to go through a deferred register. Architectury's
    * {@code CreativeTabRegistry.create(Consumer<CreativeModeTab.Builder>)} looks like the natural
    * replacement for Forge's {@code DeferredRegister<CreativeModeTab>}, but on Forge it ends up in a
    * direct {@code Registry.register(...)} call, and doing that from the mod constructor happens
    * before the creative tab registry is opened - the tab is silently dropped and no tab shows up in
    * the creative inventory. A deferred register hooks the registry event on both loaders.
    */
   public static final DeferredRegister<CreativeModeTab> REGISTER =
         DeferredRegister.create(SpartanShields.ID, Registries.CREATIVE_MODE_TAB);

   // Porting note: vanilla 1.20.1 only has CreativeModeTab#builder(Row, int) - the no-arg overload is
   // a Forge addition - so the tab is built with the row/column form, which exists on both loaders.
   public static final RegistrySupplier<CreativeModeTab> TAB = REGISTER.register("tab", () ->
         CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
               .title(Component.translatable("itemGroup.spartanshields"))
               .icon(() -> new ItemStack(ModItems.STONE_BASIC_SHIELD.get()))
               .displayItems((displayParams, output) -> output.acceptAll(shields()))
               .build());

   public static void register() {
      REGISTER.register();
   }

   private static List<ItemStack> shields() {
      List<RegistrySupplier<? extends ShieldBaseItem>> basic = List.of(
            ModItems.WOODEN_BASIC_SHIELD, ModItems.WOODEN_TOWER_SHIELD,
            ModItems.STONE_BASIC_SHIELD, ModItems.STONE_TOWER_SHIELD,
            ModItems.COPPER_BASIC_SHIELD, ModItems.COPPER_TOWER_SHIELD,
            ModItems.IRON_BASIC_SHIELD, ModItems.IRON_TOWER_SHIELD,
            ModItems.GOLDEN_BASIC_SHIELD, ModItems.GOLDEN_TOWER_SHIELD,
            ModItems.DIAMOND_BASIC_SHIELD, ModItems.DIAMOND_TOWER_SHIELD,
            ModItems.NETHERITE_BASIC_SHIELD, ModItems.NETHERITE_TOWER_SHIELD,
            ModItems.OBSIDIAN_BASIC_SHIELD, ModItems.OBSIDIAN_TOWER_SHIELD,
            ModItems.TIN_BASIC_SHIELD, ModItems.TIN_TOWER_SHIELD,
            ModItems.BRONZE_BASIC_SHIELD, ModItems.BRONZE_TOWER_SHIELD,
            ModItems.STEEL_BASIC_SHIELD, ModItems.STEEL_TOWER_SHIELD,
            ModItems.SILVER_BASIC_SHIELD, ModItems.SILVER_TOWER_SHIELD,
            ModItems.ELECTRUM_BASIC_SHIELD, ModItems.ELECTRUM_TOWER_SHIELD,
            ModItems.LEAD_BASIC_SHIELD, ModItems.LEAD_TOWER_SHIELD,
            ModItems.NICKEL_BASIC_SHIELD, ModItems.NICKEL_TOWER_SHIELD,
            ModItems.INVAR_BASIC_SHIELD, ModItems.INVAR_TOWER_SHIELD,
            ModItems.CONSTANTAN_BASIC_SHIELD, ModItems.CONSTANTAN_TOWER_SHIELD,
            ModItems.PLATINUM_BASIC_SHIELD, ModItems.PLATINUM_TOWER_SHIELD,
            ModItems.ALUMINUM_BASIC_SHIELD, ModItems.ALUMINUM_TOWER_SHIELD,
            ModItems.MANASTEEL_BASIC_SHIELD, ModItems.MANASTEEL_TOWER_SHIELD,
            ModItems.TERRASTEEL_BASIC_SHIELD, ModItems.TERRASTEEL_TOWER_SHIELD,
            ModItems.ELEMENTIUM_BASIC_SHIELD, ModItems.ELEMENTIUM_TOWER_SHIELD,
            ModItems.OSMIUM_BASIC_SHIELD,
            ModItems.LAPIS_BASIC_SHIELD,
            ModItems.REFINED_GLOWSTONE_BASIC_SHIELD,
            ModItems.REFINED_OBSIDIAN_BASIC_SHIELD,
            ModItems.SIGNALUM_BASIC_SHIELD, ModItems.SIGNALUM_TOWER_SHIELD,
            ModItems.LUMIUM_BASIC_SHIELD, ModItems.LUMIUM_TOWER_SHIELD,
            ModItems.ENDERIUM_BASIC_SHIELD, ModItems.ENDERIUM_TOWER_SHIELD);

      List<RegistrySupplier<FEPoweredShieldItem>> powered = List.of(
            ModItems.BASIC_MEKANISTS_BASIC_SHIELD, ModItems.BASIC_MEKANISTS_TOWER_SHIELD,
            ModItems.ADVANCED_MEKANISTS_BASIC_SHIELD, ModItems.ADVANCED_MEKANISTS_TOWER_SHIELD,
            ModItems.ELITE_MEKANISTS_BASIC_SHIELD, ModItems.ELITE_MEKANISTS_TOWER_SHIELD,
            ModItems.ULTIMATE_MEKANISTS_BASIC_SHIELD, ModItems.ULTIMATE_MEKANISTS_TOWER_SHIELD,
            ModItems.DARK_STEEL_RIOT_BASIC_SHIELD, ModItems.DARK_STEEL_RIOT_TOWER_SHIELD);

      List<ItemStack> stacks = new ArrayList<>();
      for (Supplier<? extends ShieldBaseItem> supplier : basic) {
         stacks.add(new ItemStack(supplier.get()));
      }

      for (RegistrySupplier<FEPoweredShieldItem> supplier : powered) {
         stacks.add(new ItemStack(supplier.get()));
         FEPoweredShieldItem item = supplier.get();
         ItemStack charged = new ItemStack(item);
         charged.getOrCreateTag().putInt(FEPoweredShieldItem.NBT_ENERGY, item.getFECapacity(charged));
         stacks.add(charged);
      }

      return stacks;
   }

   private ModCreativeTabs() {
   }
}
