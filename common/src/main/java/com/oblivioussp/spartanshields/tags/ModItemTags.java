package com.oblivioussp.spartanshields.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
   private static TagKey<Item> tag(String path) {
      return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("spartanshields", path));
   }

   // Repair / crafting material tags.
   //
   // Note: the Forge release pointed these at Forge's common tags
   // (`forge:ingots/tin`, `forge:gems/diamond`, ...). Forge's tag layer does not exist on
   // Fabric, and Forge's `forge:not + forge:tag_empty` recipe conditions cannot be parsed by
   // vanilla either, so every material tag now lives in this mod's own namespace and is
   // pre-populated with the item ids of the known provider mods. Entries pointing at
   // uninstalled mods are silently dropped by the tag loader, so this stays forward
   // compatible.
   public static final TagKey<Item> TIN_INGOT = tag("ingots/tin");
   public static final TagKey<Item> BRONZE_INGOT = tag("ingots/bronze");
   public static final TagKey<Item> STEEL_INGOT = tag("ingots/steel");
   public static final TagKey<Item> SILVER_INGOT = tag("ingots/silver");
   public static final TagKey<Item> ELECTRUM_INGOT = tag("ingots/electrum");
   public static final TagKey<Item> LEAD_INGOT = tag("ingots/lead");
   public static final TagKey<Item> NICKEL_INGOT = tag("ingots/nickel");
   public static final TagKey<Item> INVAR_INGOT = tag("ingots/invar");
   public static final TagKey<Item> CONSTANTAN_INGOT = tag("ingots/constantan");
   public static final TagKey<Item> PLATINUM_INGOT = tag("ingots/platinum");
   public static final TagKey<Item> ALUMINUM_INGOT = tag("ingots/aluminum");
   public static final TagKey<Item> MANASTEEL_INGOT = tag("ingots/manasteel");
   public static final TagKey<Item> TERRASTEEL_INGOT = tag("ingots/terrasteel");
   public static final TagKey<Item> ELEMENTIUM_INGOT = tag("ingots/elementium");
   public static final TagKey<Item> OSMIUM_INGOT = tag("ingots/osmium");
   public static final TagKey<Item> REFINED_GLOWSTONE_INGOT = tag("ingots/refined_glowstone");
   public static final TagKey<Item> REFINED_OBSIDIAN_INGOT = tag("ingots/refined_obsidian");
   public static final TagKey<Item> SIGNALUM_INGOT = tag("ingots/signalum");
   public static final TagKey<Item> LUMIUM_INGOT = tag("ingots/lumium");
   public static final TagKey<Item> ENDERIUM_INGOT = tag("ingots/enderium");
   public static final TagKey<Item> COBBLESTONE = tag("cobblestone");
   public static final TagKey<Item> OBSIDIAN = tag("obsidian");
   public static final TagKey<Item> WOODEN_ROD = tag("rods/wooden");

   // Vanilla material tags.
   public static final TagKey<Item> PLANKS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "planks"));
   public static final TagKey<Item> INGOTS_COPPER = tag("ingots/copper");
   public static final TagKey<Item> INGOTS_IRON = tag("ingots/iron");
   public static final TagKey<Item> INGOTS_GOLD = tag("ingots/gold");
   public static final TagKey<Item> INGOTS_NETHERITE = tag("ingots/netherite");
   public static final TagKey<Item> GEMS_DIAMOND = tag("gems/diamond");
   public static final TagKey<Item> GEMS_LAPIS = tag("gems/lapis");

   // Mod-owned behaviour tags.
   public static final TagKey<Item> BASIC_SHIELDS = tag("basic_shields");
   public static final TagKey<Item> TOWER_SHIELDS = tag("tower_shields");
   public static final TagKey<Item> SHIELDS_WITH_BASH = tag("shields_with_bash");
   public static final TagKey<Item> MANA_USING_ITEMS = tag("mana_using_items");

   private ModItemTags() {
   }
}
