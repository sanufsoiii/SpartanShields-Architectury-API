package com.oblivioussp.spartanshields.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.crafting.condition.TypeDisabledCondition;
import com.oblivioussp.spartanshields.init.ModItems;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import com.oblivioussp.spartanshields.util.Defaults;
import com.oblivioussp.spartanshields.util.Log;
import dev.architectury.platform.Platform;
import dev.architectury.utils.value.BooleanValue;
import dev.architectury.utils.value.DoubleValue;
import dev.architectury.utils.value.IntValue;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Mod configuration.
 * <p>
 * Porting note: the Forge release used {@code ForgeConfigSpec}, which only exists on Forge and
 * could not be reused for Fabric (Architectury API 9.x has no cross-loader config API - it only
 * appeared in Architectury 13.x for MC 1.21). The option set, keys, defaults and ranges are kept
 * 1:1, but the storage is a single JSON document in the loader-agnostic config directory
 * ({@code Platform.getConfigFolder()}), which both Forge and Fabric provide. Architectury's
 * {@code Value} suppliers are used for the option handles so the call sites stay unchanged.
 */
public class Config {
   public static final String CATEGORY_GENERAL = "general";
   public static final String CATEGORY_SHIELD_BASH = "shield_bash";
   public static final String CATEGORY_VANILLA = "vanilla";
   public static final String CATEGORY_MODDED_COMMON = "modded_common";
   public static final String CATEGORY_BOTANIA = "modded_botania";
   public static final String CATEGORY_ABYSSALCRAFT = "modded_abyssalcraft";
   public static final String CATEGORY_BWM = "modded_bwm";
   public static final String CATEGORY_MECHANISM = "modded_mekanism";
   public static final String CATEGORY_THERMAL = "modded_thermal";
   public static final String CATEGORY_POWERED = "modded_powered";

   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   private static final int MIN_DURABILITY = 0;
   private static final int MAX_DURABILITY = 100000;

   public static final Config INSTANCE = new Config();

   public final BooleanValue vanillaOnly;
   public final BooleanValue forceDisableUncraftableTooltips;
   public final DoubleValue towerShieldDurabilityMultiplier;
   public final IntValue cooldownShieldBash;
   public final IntValue cooldownTowerShieldBash;
   public final BooleanValue disableShieldBash;
   public final IntValue damageToFEMultiplier;

   public final IntValue durabilityWoodShield;
   public final IntValue durabilityStoneShield;
   public final IntValue durabilityCopperShield;
   public final IntValue durabilityIronShield;
   public final IntValue durabilityGoldShield;
   public final IntValue durabilityDiamondShield;
   public final IntValue durabilityNetheriteShield;
   public final ShieldValues obsidian;
   public final ShieldValues tin;
   public final ShieldValues bronze;
   public final ShieldValues steel;
   public final ShieldValues silver;
   public final ShieldValues electrum;
   public final ShieldValues lead;
   public final ShieldValues nickel;
   public final ShieldValues invar;
   public final ShieldValues constantan;
   public final ShieldValues platinum;
   public final ShieldValues aluminum;
   public final IntValue durabilityManasteelShield;
   public final IntValue durabilityTerrasteelShield;
   public final IntValue durabilityElementiumShield;
   public final IntValue durabilityOsmiumShield;
   public final IntValue durabilityLapisLazuliShield;
   public final IntValue durabilityRefinedGlowstoneShield;
   public final IntValue durabilityRefinedObsidianShield;
   public final IntValue durabilitySignalumShield;
   public final IntValue durabilityLumiumShield;
   public final IntValue durabilityEnderiumShield;

   private final JsonObject data = new JsonObject();
   private final Path path;

   private Config() {
      this.path = Platform.getConfigFolder().resolve(SpartanShields.ID + ".json");

      this.vanillaOnly = bool(CATEGORY_GENERAL, "vanilla_only", false);
      this.forceDisableUncraftableTooltips = bool(CATEGORY_GENERAL, "force_disable_uncraftable_tooltips", false);
      this.towerShieldDurabilityMultiplier = dbl(CATEGORY_GENERAL, "tower_shield_durability_multiplier",
            Defaults.DefaultTowerShieldDurabilityMultiplier, 1.0D, 10.0D);

      this.cooldownShieldBash = integer(CATEGORY_SHIELD_BASH, "cooldown", Defaults.DefaultCooldownShieldBash, 1, 1000);
      this.cooldownTowerShieldBash = integer(CATEGORY_SHIELD_BASH, "cooldown_tower", 50, 1, 1000);
      this.disableShieldBash = bool(CATEGORY_SHIELD_BASH, "disabled", false);

      this.durabilityWoodShield = durability(CATEGORY_VANILLA, "wood", Defaults.DefaultDurabilityWoodShield);
      this.durabilityStoneShield = durability(CATEGORY_VANILLA, "stone", Defaults.DefaultDurabilityStoneShield);
      this.durabilityCopperShield = durability(CATEGORY_VANILLA, "copper", Defaults.DefaultDurabilityCopperShield);
      this.durabilityIronShield = durability(CATEGORY_VANILLA, "iron", Defaults.DefaultDurabilityIronShield);
      this.durabilityGoldShield = durability(CATEGORY_VANILLA, "gold", Defaults.DefaultDurabilityGoldShield);
      this.durabilityDiamondShield = durability(CATEGORY_VANILLA, "diamond", Defaults.DefaultDurabilityDiamondShield);
      this.durabilityNetheriteShield = durability(CATEGORY_VANILLA, "netherite", Defaults.DefaultDurabilityNetheriteShield);
      this.obsidian = new ShieldValues(CATEGORY_VANILLA, "obsidian", Defaults.DefaultDurabilityObsidianShield, TypeDisabledCondition.OBSIDIAN);

      this.tin = new ShieldValues(CATEGORY_MODDED_COMMON, "tin", Defaults.DefaultDurabilityTinShield, TypeDisabledCondition.TIN);
      this.bronze = new ShieldValues(CATEGORY_MODDED_COMMON, "bronze", Defaults.DefaultDurabilityBronzeShield, TypeDisabledCondition.BRONZE);
      this.steel = new ShieldValues(CATEGORY_MODDED_COMMON, "steel", Defaults.DefaultDurabilitySteelShield, TypeDisabledCondition.STEEL);
      this.silver = new ShieldValues(CATEGORY_MODDED_COMMON, "silver", Defaults.DefaultDurabilitySilverShield, TypeDisabledCondition.SILVER);
      this.electrum = new ShieldValues(CATEGORY_MODDED_COMMON, "electrum", Defaults.DefaultDurabilityElectrumShield, TypeDisabledCondition.ELECTRUM);
      this.lead = new ShieldValues(CATEGORY_MODDED_COMMON, "lead", Defaults.DefaultDurabilityLeadShield, TypeDisabledCondition.LEAD);
      this.nickel = new ShieldValues(CATEGORY_MODDED_COMMON, "nickel", Defaults.DefaultDurabilityNickelShield, TypeDisabledCondition.NICKEL);
      this.invar = new ShieldValues(CATEGORY_MODDED_COMMON, "invar", Defaults.DefaultDurabilityInvarShield, TypeDisabledCondition.INVAR);
      this.constantan = new ShieldValues(CATEGORY_MODDED_COMMON, "constantan", Defaults.DefaultDurabilityConstantanShield, TypeDisabledCondition.CONSTANTAN);
      this.platinum = new ShieldValues(CATEGORY_MODDED_COMMON, "platinum", Defaults.DefaultDurabilityPlatinumShield, TypeDisabledCondition.PLATINUM);
      this.aluminum = new ShieldValues(CATEGORY_MODDED_COMMON, "aluminum", Defaults.DefaultDurabilityAluminumShield, TypeDisabledCondition.ALUMINUM);

      this.durabilityManasteelShield = durability(CATEGORY_BOTANIA, "manasteel", Defaults.DefaultDurabilityManasteelShield);
      this.durabilityTerrasteelShield = durability(CATEGORY_BOTANIA, "terrasteel", Defaults.DefaultDurabilityTerrasteelShield);
      this.durabilityElementiumShield = durability(CATEGORY_BOTANIA, "elementium", Defaults.DefaultDurabilityElementiumShield);

      this.durabilityOsmiumShield = durability(CATEGORY_MECHANISM, "osmium", Defaults.DefaultDurabilityOsmiumShield);
      this.durabilityLapisLazuliShield = durability(CATEGORY_MECHANISM, "lapis_lazuli", Defaults.DefaultDurabilityLapisLazuliShield);
      this.durabilityRefinedGlowstoneShield = durability(CATEGORY_MECHANISM, "refined_glowstone", Defaults.DefaultDurabilityRefinedGlowstoneShield);
      this.durabilityRefinedObsidianShield = durability(CATEGORY_MECHANISM, "refined_obsidian", Defaults.DefaultDurabilityRefinedObsidianShield);

      this.durabilitySignalumShield = durability(CATEGORY_THERMAL, "signalum", Defaults.DefaultDurabilitySignalumShield);
      this.durabilityLumiumShield = durability(CATEGORY_THERMAL, "lumium", Defaults.DefaultDurabilityLumiumShield);
      this.durabilityEnderiumShield = durability(CATEGORY_THERMAL, "enderium", Defaults.DefaultDurabilityEnderiumShield);

      this.damageToFEMultiplier = integer(CATEGORY_POWERED, "damageToFEMultpier", Defaults.DefaultDamageToFEMultiplier, 0, 1000000);

      this.load();
   }

   // ------------------------------------------------------------------ option plumbing

   private JsonObject category(String name) {
      if (!this.data.has(name) || !this.data.get(name).isJsonObject()) {
         this.data.add(name, new JsonObject());
      }
      return this.data.getAsJsonObject(name);
   }

   // Porting note: Architectury's Value types extend both Supplier and Consumer, so they are
   // interfaces with two abstract methods and cannot be written as lambdas - anonymous classes it is.
   private BooleanValue bool(String category, String key, boolean defaultValue) {
      JsonObject object = category(category);
      if (!object.has(key)) {
         object.addProperty(key, defaultValue);
      }

      return new BooleanValue() {
         @Override
         public boolean getAsBoolean() {
            return category(category).get(key).getAsBoolean();
         }

         @Override
         public void accept(boolean value) {
            category(category).addProperty(key, value);
            Config.this.save();
         }
      };
   }

   private IntValue integer(String category, String key, int defaultValue, int min, int max) {
      JsonObject object = category(category);
      if (!object.has(key)) {
         object.addProperty(key, defaultValue);
      }

      return new IntValue() {
         @Override
         public int getAsInt() {
            JsonObject cat = category(category);
            int current = clamp(cat.has(key) ? cat.get(key).getAsInt() : defaultValue, min, max);
            cat.addProperty(key, current);
            return current;
         }

         @Override
         public void accept(int value) {
            category(category).addProperty(key, clamp(value, min, max));
            Config.this.save();
         }
      };
   }

   private DoubleValue dbl(String category, String key, double defaultValue, double min, double max) {
      JsonObject object = category(category);
      if (!object.has(key)) {
         object.addProperty(key, defaultValue);
      }

      return new DoubleValue() {
         @Override
         public double getAsDouble() {
            JsonObject cat = category(category);
            double current = cat.has(key) ? cat.get(key).getAsDouble() : defaultValue;
            current = Math.max(min, Math.min(max, current));
            cat.addProperty(key, current);
            return current;
         }

         @Override
         public void accept(double value) {
            category(category).addProperty(key, Math.max(min, Math.min(max, value)));
            Config.this.save();
         }
      };
   }

   private IntValue durability(String category, String name, int defaultValue) {
      String key = name.toLowerCase(java.util.Locale.ROOT) + ".durability";
      return integer(category, key, defaultValue, MIN_DURABILITY, MAX_DURABILITY);
   }

   private JsonElement get(String category, String key) {
      return this.data.getAsJsonObject(category).get(key);
   }

   private static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   // ------------------------------------------------------------------ persistence

   public void load() {
      if (Files.exists(this.path)) {
         try (Reader reader = Files.newBufferedReader(this.path, StandardCharsets.UTF_8)) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (parsed.isJsonObject()) {
               // merge file over defaults so newly added options appear automatically
               merge(this.data, parsed.getAsJsonObject());
            }
         } catch (IOException | RuntimeException e) {
            Log.warn("Failed to read config " + this.path + ": " + e);
         }
      }

      this.apply();
      this.save();
   }

   private static void merge(JsonObject target, JsonObject source) {
      for (String key : source.keySet()) {
         JsonElement value = source.get(key);
         if (value.isJsonObject() && target.has(key) && target.get(key).isJsonObject()) {
            merge(target.getAsJsonObject(key), value.getAsJsonObject());
         } else {
            target.add(key, value);
         }
      }
   }

   public void save() {
      try {
         Files.createDirectories(this.path.getParent());
         try (Writer writer = Files.newBufferedWriter(this.path, StandardCharsets.UTF_8)) {
            GSON.toJson(this.data, writer);
         }
      } catch (IOException e) {
         Log.warn("Failed to write config " + this.path + ": " + e);
      }
   }

   /** Re-reads the file and pushes the values into the registries (durability + conditions). */
   public void reload() {
      Log.info("Reloading Spartan Shields config");
      this.load();
      applyDurabilities();
   }

   private void apply() {
      TypeDisabledCondition.clear();
      TypeDisabledCondition.updateType(TypeDisabledCondition.MODDED, this.vanillaOnly.get());
      for (ShieldValues values : this.commonShieldValues()) {
         values.updateFromConfig();
      }
   }

   /**
    * Pushes the configured durabilities into the item registry. Must only run once the item
    * registry is populated, which is why it is called from the platform setup instead of the
    * config constructor (the Forge release relied on ForgeConfigSpec's reload event for this).
    */
   public void applyDurabilities() {
      this.apply();

      updateMaxDurability(ModItems.WOODEN_BASIC_SHIELD.get(), ModItems.WOODEN_TOWER_SHIELD.get(), this.durabilityWoodShield.get());
      updateMaxDurability(ModItems.STONE_BASIC_SHIELD.get(), ModItems.STONE_TOWER_SHIELD.get(), this.durabilityStoneShield.get());
      updateMaxDurability(ModItems.COPPER_BASIC_SHIELD.get(), ModItems.COPPER_TOWER_SHIELD.get(), this.durabilityCopperShield.get());
      updateMaxDurability(ModItems.IRON_BASIC_SHIELD.get(), ModItems.IRON_TOWER_SHIELD.get(), this.durabilityIronShield.get());
      updateMaxDurability(ModItems.GOLDEN_BASIC_SHIELD.get(), ModItems.GOLDEN_TOWER_SHIELD.get(), this.durabilityGoldShield.get());
      updateMaxDurability(ModItems.DIAMOND_BASIC_SHIELD.get(), ModItems.DIAMOND_TOWER_SHIELD.get(), this.durabilityDiamondShield.get());
      updateMaxDurability(ModItems.NETHERITE_BASIC_SHIELD.get(), ModItems.NETHERITE_TOWER_SHIELD.get(), this.durabilityNetheriteShield.get());
      updateMaxDurability(ModItems.OBSIDIAN_BASIC_SHIELD.get(), ModItems.OBSIDIAN_TOWER_SHIELD.get(), this.obsidian.getDurability());

      updateMaxDurability(ModItems.TIN_BASIC_SHIELD.get(), ModItems.TIN_TOWER_SHIELD.get(), this.tin.getDurability());
      updateMaxDurability(ModItems.BRONZE_BASIC_SHIELD.get(), ModItems.BRONZE_TOWER_SHIELD.get(), this.bronze.getDurability());
      updateMaxDurability(ModItems.STEEL_BASIC_SHIELD.get(), ModItems.STEEL_TOWER_SHIELD.get(), this.steel.getDurability());
      updateMaxDurability(ModItems.SILVER_BASIC_SHIELD.get(), ModItems.SILVER_TOWER_SHIELD.get(), this.silver.getDurability());
      updateMaxDurability(ModItems.ELECTRUM_BASIC_SHIELD.get(), ModItems.ELECTRUM_TOWER_SHIELD.get(), this.electrum.getDurability());
      updateMaxDurability(ModItems.LEAD_BASIC_SHIELD.get(), ModItems.LEAD_TOWER_SHIELD.get(), this.lead.getDurability());
      updateMaxDurability(ModItems.NICKEL_BASIC_SHIELD.get(), ModItems.NICKEL_TOWER_SHIELD.get(), this.nickel.getDurability());
      updateMaxDurability(ModItems.INVAR_BASIC_SHIELD.get(), ModItems.INVAR_TOWER_SHIELD.get(), this.invar.getDurability());
      updateMaxDurability(ModItems.CONSTANTAN_BASIC_SHIELD.get(), ModItems.CONSTANTAN_TOWER_SHIELD.get(), this.constantan.getDurability());
      updateMaxDurability(ModItems.PLATINUM_BASIC_SHIELD.get(), ModItems.PLATINUM_TOWER_SHIELD.get(), this.platinum.getDurability());
      updateMaxDurability(ModItems.ALUMINUM_BASIC_SHIELD.get(), ModItems.ALUMINUM_TOWER_SHIELD.get(), this.aluminum.getDurability());

      updateMaxDurability(ModItems.MANASTEEL_BASIC_SHIELD.get(), ModItems.MANASTEEL_TOWER_SHIELD.get(), this.durabilityManasteelShield.get());
      updateMaxDurability(ModItems.TERRASTEEL_BASIC_SHIELD.get(), ModItems.TERRASTEEL_TOWER_SHIELD.get(), this.durabilityTerrasteelShield.get());
      updateMaxDurability(ModItems.ELEMENTIUM_BASIC_SHIELD.get(), ModItems.ELEMENTIUM_TOWER_SHIELD.get(), this.durabilityElementiumShield.get());
      updateMaxDurability(ModItems.OSMIUM_BASIC_SHIELD.get(), null, this.durabilityOsmiumShield.get());
      updateMaxDurability(ModItems.LAPIS_BASIC_SHIELD.get(), null, this.durabilityLapisLazuliShield.get());
      updateMaxDurability(ModItems.REFINED_GLOWSTONE_BASIC_SHIELD.get(), null, this.durabilityRefinedGlowstoneShield.get());
      updateMaxDurability(ModItems.REFINED_OBSIDIAN_BASIC_SHIELD.get(), null, this.durabilityRefinedObsidianShield.get());
      updateMaxDurability(ModItems.SIGNALUM_BASIC_SHIELD.get(), ModItems.SIGNALUM_TOWER_SHIELD.get(), this.durabilitySignalumShield.get());
      updateMaxDurability(ModItems.LUMIUM_BASIC_SHIELD.get(), ModItems.LUMIUM_TOWER_SHIELD.get(), this.durabilityLumiumShield.get());
      updateMaxDurability(ModItems.ENDERIUM_BASIC_SHIELD.get(), ModItems.ENDERIUM_TOWER_SHIELD.get(), this.durabilityEnderiumShield.get());
   }

   private List<ShieldValues> commonShieldValues() {
      List<ShieldValues> values = new ArrayList<>();
      values.add(this.obsidian);
      values.add(this.tin);
      values.add(this.bronze);
      values.add(this.steel);
      values.add(this.silver);
      values.add(this.electrum);
      values.add(this.lead);
      values.add(this.nickel);
      values.add(this.invar);
      values.add(this.constantan);
      values.add(this.platinum);
      values.add(this.aluminum);
      return values;
   }

   public static void updateMaxDurability(ShieldBaseItem basicShieldItem, ShieldBaseItem towerShieldItem, int value) {
      if (basicShieldItem != null) {
         basicShieldItem.setMaxDamage(value);
      }

      if (towerShieldItem != null) {
         towerShieldItem.setMaxDamage(value);
      }
   }

   /** A durability + disable pair, mirroring the Forge {@code ShieldValues} inner class. */
   public class ShieldValues {
      private final String disabledName;
      private final IntValue durability;
      private final BooleanValue isDisabled;

      ShieldValues(String category, String name, int defaultValue, String disableType) {
         this.disabledName = disableType;
         this.durability = durability(category, name, defaultValue);
         this.isDisabled = bool(category, name.toLowerCase(java.util.Locale.ROOT) + ".disable", false);
      }

      public int getDurability() {
         return this.durability.get();
      }

      public boolean getIsDisabled() {
         return this.isDisabled.get();
      }

      public void updateFromConfig() {
         TypeDisabledCondition.updateType(this.disabledName, this.getIsDisabled());
      }
   }
}
