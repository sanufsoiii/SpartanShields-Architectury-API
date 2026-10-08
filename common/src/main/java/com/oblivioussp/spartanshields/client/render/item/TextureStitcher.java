package com.oblivioussp.spartanshields.client.render.item;

import net.minecraft.resources.ResourceLocation;

/**
 * Texture bindings for the tower shields.
 *
 * Porting note: the Forge release loaded these through its own `TextureStitcher` plus the
 * `assets/minecraft/atlases/shield_patterns.json` atlas. The atlas entry is not needed any
 * more: the shield textures already live in `assets/spartanshields/textures/entity/**`, and
 * Minecraft's built-in `shields` texture sheet picks them up because the renderer asks for
 * `Material(Sheets.SHIELD_SHEET, id)` - the same mechanism vanilla banners/shields use.
 */

public class TextureStitcher {
   public static final TowerShieldRenderInfo RENDER_INFO_WOODEN_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/wooden_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/wooden_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_STONE_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/stone_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/stone_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_COPPER_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/copper_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/copper_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_IRON_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/iron_tower_shield_nopattern"), new ResourceLocation("spartanshields", "entity/iron_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_GOLDEN_TOWER_SHIELD_ = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/golden_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/golden_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_DIAMOND_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/diamond_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/diamond_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_NETHERITE_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/netherite_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/netherite_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_OBSIDIAN_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/obsidian_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/obsidian_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_TIN_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/tin_tower_shield_nopattern"), new ResourceLocation("spartanshields", "entity/tin_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_BRONZE_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/bronze_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/bronze_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_STEEL_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/steel_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/steel_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_SILVER_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/silver_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/silver_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_ELECTRUM_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/electrum_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/electrum_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_LEAD_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/lead_tower_shield_nopattern"), new ResourceLocation("spartanshields", "entity/lead_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_NICKEL_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/nickel_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/nickel_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_INVAR_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/invar_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/invar_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_CONSTANTAN_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/constantan_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/constantan_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_PLATINUM_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/platinum_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/platinum_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_ALUMINUM_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/aluminum_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/aluminum_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_SIGNALUM_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/signalum_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/signalum_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_LUMIUM_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/lumium_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/lumium_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_ENDERIUM_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/enderium_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/enderium_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_MANASTEEL_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/botania/manasteel_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/botania/manasteel_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_TERRASTEEL_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/botania/terrasteel_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/botania/terrasteel_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_ELEMENTIUM_TOWER_SHIELD = new TowerShieldRenderInfo(
      new ResourceLocation("spartanshields", "entity/botania/elementium_tower_shield_nopattern"),
      new ResourceLocation("spartanshields", "entity/botania/elementium_tower_shield_pattern")
   );
   public static final TowerShieldRenderInfo RENDER_INFO_BASIC_MEKANISTS_TOWER_SHIELD = new MekanismTowerShieldRenderInfo(0.46875F, 0.9375F, 0.51171875F);
   public static final TowerShieldRenderInfo RENDER_INFO_ADVANCED_MEKANISTS_TOWER_SHIELD = new MekanismTowerShieldRenderInfo(0.9375F, 0.46875F, 0.51171875F);
   public static final TowerShieldRenderInfo RENDER_INFO_ELITE_MEKANISTS_TOWER_SHIELD = new MekanismTowerShieldRenderInfo(0.46875F, 0.51171875F, 0.9375F);
   public static final TowerShieldRenderInfo RENDER_INFO_ULTIMATE_MEKANISTS_TOWER_SHIELD = new MekanismTowerShieldRenderInfo(0.9375F, 0.51171875F, 0.9375F);
   public static final TowerShieldRenderInfo RENDER_INFO_DARK_STEEL_TOWER_SHIELD = new DarkSteelTowerShieldRenderInfo(0.5F, 1.0F, 0.627451F);
}
