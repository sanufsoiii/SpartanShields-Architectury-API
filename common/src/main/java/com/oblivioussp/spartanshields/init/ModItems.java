package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.compat.BotaniaCompat;
import com.oblivioussp.spartanshields.item.BasicShieldItem;
import com.oblivioussp.spartanshields.item.BlockEffectShieldItem;
import com.oblivioussp.spartanshields.item.BotaniaShieldItem;
import com.oblivioussp.spartanshields.item.ElementiumShieldItem;
import com.oblivioussp.spartanshields.item.ExternalModShieldItem;
import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import com.oblivioussp.spartanshields.item.ObsidianShieldItem;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import com.oblivioussp.spartanshields.item.SilverShieldItem;
import com.oblivioussp.spartanshields.util.PowerUnit;
import com.oblivioussp.spartanshields.util.TierSS;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ModItems {
   public static final DeferredRegister<Item> REGISTER =
         DeferredRegister.create(SpartanShields.ID, Registries.ITEM);

   public static final RegistrySupplier<ShieldBaseItem> WOODEN_BASIC_SHIELD =
         REGISTER.register("wooden_basic_shield", () -> new BasicShieldItem(TierSS.WOOD, 104, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> WOODEN_TOWER_SHIELD =
         REGISTER.register("wooden_tower_shield", () -> new BasicShieldItem(TierSS.WOOD, 104, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> STONE_BASIC_SHIELD =
         REGISTER.register("stone_basic_shield", () -> new BasicShieldItem(TierSS.STONE, 230, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> STONE_TOWER_SHIELD =
         REGISTER.register("stone_tower_shield", () -> new BasicShieldItem(TierSS.STONE, 230, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> COPPER_BASIC_SHIELD =
         REGISTER.register("copper_basic_shield", () -> new BasicShieldItem(TierSS.COPPER, 400, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> COPPER_TOWER_SHIELD =
         REGISTER.register("copper_tower_shield", () -> new BasicShieldItem(TierSS.COPPER, 400, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> IRON_BASIC_SHIELD =
         REGISTER.register("iron_basic_shield", () -> new BasicShieldItem(TierSS.IRON, 538, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> IRON_TOWER_SHIELD =
         REGISTER.register("iron_tower_shield", () -> new BasicShieldItem(TierSS.IRON, 538, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> GOLDEN_BASIC_SHIELD =
         REGISTER.register("golden_basic_shield", () -> new BasicShieldItem(TierSS.GOLD, 192, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> GOLDEN_TOWER_SHIELD =
         REGISTER.register("golden_tower_shield", () -> new BasicShieldItem(TierSS.GOLD, 192, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> DIAMOND_BASIC_SHIELD =
         REGISTER.register("diamond_basic_shield", () -> new BasicShieldItem(TierSS.DIAMOND, 2560, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> DIAMOND_TOWER_SHIELD =
         REGISTER.register("diamond_tower_shield", () -> new BasicShieldItem(TierSS.DIAMOND, 2560, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> NETHERITE_BASIC_SHIELD =
         REGISTER.register("netherite_basic_shield", () -> new BasicShieldItem(TierSS.NETHERITE, 3554, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> NETHERITE_TOWER_SHIELD =
         REGISTER.register("netherite_tower_shield", () -> new BasicShieldItem(TierSS.NETHERITE, 3554, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> OBSIDIAN_BASIC_SHIELD =
         REGISTER.register("obsidian_basic_shield", () -> new ObsidianShieldItem(TierSS.OBSIDIAN, 1600, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> OBSIDIAN_TOWER_SHIELD =
         REGISTER.register("obsidian_tower_shield", () -> new ObsidianShieldItem(TierSS.OBSIDIAN, 1600, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> TIN_BASIC_SHIELD =
         REGISTER.register("tin_basic_shield", () -> new BasicShieldItem(TierSS.TIN, 320, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> TIN_TOWER_SHIELD =
         REGISTER.register("tin_tower_shield", () -> new BasicShieldItem(TierSS.TIN, 320, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> BRONZE_BASIC_SHIELD =
         REGISTER.register("bronze_basic_shield", () -> new BasicShieldItem(TierSS.BRONZE, 680, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> BRONZE_TOWER_SHIELD =
         REGISTER.register("bronze_tower_shield", () -> new BasicShieldItem(TierSS.BRONZE, 680, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> STEEL_BASIC_SHIELD =
         REGISTER.register("steel_basic_shield", () -> new BasicShieldItem(TierSS.STEEL, 800, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> STEEL_TOWER_SHIELD =
         REGISTER.register("steel_tower_shield", () -> new BasicShieldItem(TierSS.STEEL, 800, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> SILVER_BASIC_SHIELD =
         REGISTER.register("silver_basic_shield", () -> new SilverShieldItem(TierSS.SILVER, 216, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> SILVER_TOWER_SHIELD =
         REGISTER.register("silver_tower_shield", () -> new SilverShieldItem(TierSS.SILVER, 216, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> ELECTRUM_BASIC_SHIELD =
         REGISTER.register("electrum_basic_shield", () -> new BasicShieldItem(TierSS.ELECTRUM, 320, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> ELECTRUM_TOWER_SHIELD =
         REGISTER.register("electrum_tower_shield", () -> new BasicShieldItem(TierSS.ELECTRUM, 320, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> LEAD_BASIC_SHIELD =
         REGISTER.register("lead_basic_shield", () -> new BasicShieldItem(TierSS.LEAD, 480, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> LEAD_TOWER_SHIELD =
         REGISTER.register("lead_tower_shield", () -> new BasicShieldItem(TierSS.LEAD, 480, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> NICKEL_BASIC_SHIELD =
         REGISTER.register("nickel_basic_shield", () -> new BasicShieldItem(TierSS.NICKEL, 420, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> NICKEL_TOWER_SHIELD =
         REGISTER.register("nickel_tower_shield", () -> new BasicShieldItem(TierSS.NICKEL, 420, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> INVAR_BASIC_SHIELD =
         REGISTER.register("invar_basic_shield", () -> new BasicShieldItem(TierSS.INVAR, 720, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> INVAR_TOWER_SHIELD =
         REGISTER.register("invar_tower_shield", () -> new BasicShieldItem(TierSS.INVAR, 720, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> CONSTANTAN_BASIC_SHIELD =
         REGISTER.register("constantan_basic_shield", () -> new BasicShieldItem(TierSS.CONSTANTAN, 640, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> CONSTANTAN_TOWER_SHIELD =
         REGISTER.register("constantan_tower_shield", () -> new BasicShieldItem(TierSS.CONSTANTAN, 640, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> PLATINUM_BASIC_SHIELD =
         REGISTER.register("platinum_basic_shield", () -> new BasicShieldItem(TierSS.PLATINUM, 1536, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> PLATINUM_TOWER_SHIELD =
         REGISTER.register("platinum_tower_shield", () -> new BasicShieldItem(TierSS.PLATINUM, 1536, true, new Properties()));

   // Botania shields.
   //
   // Porting note: the Forge release evaluated `ModList.isLoaded("botania")` and picked between a
   // Botania item and an `ExternalModShieldItem` while the class was initialised. Everything is
   // now resolved inside the registration supplier, which Architectury runs at registry time, so
   // the lookup happens after all mods have initialised on both loaders.
   public static final RegistrySupplier<ShieldBaseItem> MANASTEEL_BASIC_SHIELD = REGISTER.register(
         "manasteel_basic_shield",
         () -> BotaniaCompat.isLoaded()
               ? new BotaniaShieldItem(manasteelTier(), 450, false, 60, new Properties())
               : new ExternalModShieldItem(TierSS.MANASTEEL, 450, false, "botania", new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> MANASTEEL_TOWER_SHIELD = REGISTER.register(
         "manasteel_tower_shield",
         () -> BotaniaCompat.isLoaded()
               ? new BotaniaShieldItem(manasteelTier(), 450, true, 60, new Properties())
               : new ExternalModShieldItem(TierSS.MANASTEEL, 450, true, "botania", new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> TERRASTEEL_BASIC_SHIELD = REGISTER.register(
         "terrasteel_basic_shield",
         () -> BotaniaCompat.isLoaded()
               ? new BotaniaShieldItem(terrasteelTier(), 2920, false, 100, new Properties())
               : new ExternalModShieldItem(TierSS.TERRASTEEL, 2920, false, "botania", new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> TERRASTEEL_TOWER_SHIELD = REGISTER.register(
         "terrasteel_tower_shield",
         () -> BotaniaCompat.isLoaded()
               ? new BotaniaShieldItem(terrasteelTier(), 2920, true, 100, new Properties())
               : new ExternalModShieldItem(TierSS.TERRASTEEL, 2920, true, "botania", new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> ELEMENTIUM_BASIC_SHIELD = REGISTER.register(
         "elementium_basic_shield",
         () -> BotaniaCompat.isLoaded()
               ? new ElementiumShieldItem(elementiumTier(), 1080, false, 60, new Properties())
               : new ExternalModShieldItem(TierSS.ELEMENTIUM, 1080, false, "botania", new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> ELEMENTIUM_TOWER_SHIELD = REGISTER.register(
         "elementium_tower_shield",
         () -> BotaniaCompat.isLoaded()
               ? new ElementiumShieldItem(elementiumTier(), 1080, true, 60, new Properties())
               : new ExternalModShieldItem(TierSS.ELEMENTIUM, 1080, true, "botania", new Properties()));

   public static final RegistrySupplier<ShieldBaseItem> ALUMINUM_BASIC_SHIELD =
         REGISTER.register("aluminum_basic_shield", () -> new BasicShieldItem(TierSS.ALUMINUM, 720, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> ALUMINUM_TOWER_SHIELD =
         REGISTER.register("aluminum_tower_shield", () -> new BasicShieldItem(TierSS.ALUMINUM, 720, true, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> OSMIUM_BASIC_SHIELD =
         REGISTER.register("osmium_basic_shield", () -> new BasicShieldItem(TierSS.OSMIUM, 750, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> LAPIS_BASIC_SHIELD =
         REGISTER.register("lapis_lazuli_basic_shield", () -> new BasicShieldItem(TierSS.LAPIS_LAZULI, 300, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> REFINED_GLOWSTONE_BASIC_SHIELD =
         REGISTER.register("refined_glowstone_basic_shield", () -> new BasicShieldItem(TierSS.REFINED_GLOWSTONE, 450, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> REFINED_OBSIDIAN_BASIC_SHIELD =
         REGISTER.register("refined_obsidian_basic_shield", () -> new BasicShieldItem(TierSS.REFINED_OBSIDIAN, 3750, new Properties()));

   public static final RegistrySupplier<FEPoweredShieldItem> BASIC_MEKANISTS_BASIC_SHIELD =
         REGISTER.register("basic_mekanists_basic_shield", () -> new FEPoweredShieldItem(800000, 4000, "mekanism", PowerUnit.ForgeEnergy, false, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> BASIC_MEKANISTS_TOWER_SHIELD =
         REGISTER.register("basic_mekanists_tower_shield", () -> new FEPoweredShieldItem(800000, 4000, "mekanism", PowerUnit.ForgeEnergy, true, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> ADVANCED_MEKANISTS_BASIC_SHIELD =
         REGISTER.register("advanced_mekanists_basic_shield", () -> new FEPoweredShieldItem(1600000, 8000, "mekanism", PowerUnit.ForgeEnergy, false, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> ADVANCED_MEKANISTS_TOWER_SHIELD =
         REGISTER.register("advanced_mekanists_tower_shield", () -> new FEPoweredShieldItem(1600000, 8000, "mekanism", PowerUnit.ForgeEnergy, true, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> ELITE_MEKANISTS_BASIC_SHIELD =
         REGISTER.register("elite_mekanists_basic_shield", () -> new FEPoweredShieldItem(2400000, 12000, "mekanism", PowerUnit.ForgeEnergy, false, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> ELITE_MEKANISTS_TOWER_SHIELD =
         REGISTER.register("elite_mekanists_tower_shield", () -> new FEPoweredShieldItem(2400000, 12000, "mekanism", PowerUnit.ForgeEnergy, true, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> ULTIMATE_MEKANISTS_BASIC_SHIELD =
         REGISTER.register("ultimate_mekanists_basic_shield", () -> new FEPoweredShieldItem(3200000, 16000, "mekanism", PowerUnit.ForgeEnergy, false, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> ULTIMATE_MEKANISTS_TOWER_SHIELD =
         REGISTER.register("ultimate_mekanists_tower_shield", () -> new FEPoweredShieldItem(3200000, 16000, "mekanism", PowerUnit.ForgeEnergy, true, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> DARK_STEEL_RIOT_BASIC_SHIELD =
         REGISTER.register("dark_steel_riot_basic_shield", () -> new FEPoweredShieldItem(2000000, 10000, "enderio", PowerUnit.MicroInfinity, false, new Properties()));
   public static final RegistrySupplier<FEPoweredShieldItem> DARK_STEEL_RIOT_TOWER_SHIELD =
         REGISTER.register("dark_steel_riot_tower_shield", () -> new FEPoweredShieldItem(2000000, 10000, "enderio", PowerUnit.MicroInfinity, true, new Properties()));

   public static final RegistrySupplier<ShieldBaseItem> SIGNALUM_BASIC_SHIELD =
         REGISTER.register("signalum_basic_shield", () -> new BlockEffectShieldItem(TierSS.SIGNALUM, 2048, MobEffects.WEAKNESS, 100, 1, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> SIGNALUM_TOWER_SHIELD =
         REGISTER.register("signalum_tower_shield", () -> new BlockEffectShieldItem(TierSS.SIGNALUM, 2048, true, MobEffects.WEAKNESS, 100, 1, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> LUMIUM_BASIC_SHIELD =
         REGISTER.register("lumium_basic_shield", () -> new BlockEffectShieldItem(TierSS.LUMIUM, 1280, MobEffects.GLOWING, 200, 0, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> LUMIUM_TOWER_SHIELD =
         REGISTER.register("lumium_tower_shield", () -> new BlockEffectShieldItem(TierSS.LUMIUM, 1280, true, MobEffects.GLOWING, 200, 0, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> ENDERIUM_BASIC_SHIELD =
         REGISTER.register("enderium_basic_shield", () -> new BlockEffectShieldItem(TierSS.ENDERIUM, 3120, MobEffects.MOVEMENT_SLOWDOWN, 100, 1, new Properties()));
   public static final RegistrySupplier<ShieldBaseItem> ENDERIUM_TOWER_SHIELD =
         REGISTER.register("enderium_tower_shield", () -> new BlockEffectShieldItem(TierSS.ENDERIUM, 3120, true, MobEffects.MOVEMENT_SLOWDOWN, 100, 1, new Properties()));

   private static TierSS manasteelTier() {
      return BotaniaCompat.tier("getManasteelItemTier", TierSS.MANASTEEL);
   }

   private static TierSS terrasteelTier() {
      return BotaniaCompat.tier("getTerrasteelItemTier", TierSS.TERRASTEEL);
   }

   private static TierSS elementiumTier() {
      return BotaniaCompat.tier("getElementiumItemTier", TierSS.ELEMENTIUM);
   }

   public static void register() {
      REGISTER.register();
   }

   private ModItems() {
   }
}
