package com.oblivioussp.spartanshields.util;

import com.oblivioussp.spartanshields.tags.ModItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public class TierSS implements Tier {
   public static final TierSS WOOD = new TierSS(Tiers.WOOD, ModItemTags.PLANKS);
   public static final TierSS STONE = new TierSS(Tiers.STONE, ModItemTags.COBBLESTONE);
   public static final TierSS COPPER = new TierSS(200, 5.0F, 1.5F, 8, ModItemTags.INGOTS_COPPER);
   public static final TierSS IRON = new TierSS(Tiers.IRON, ModItemTags.INGOTS_IRON);
   public static final TierSS GOLD = new TierSS(Tiers.GOLD, ModItemTags.INGOTS_GOLD);
   public static final TierSS DIAMOND = new TierSS(Tiers.DIAMOND, ModItemTags.GEMS_DIAMOND);
   public static final TierSS NETHERITE = new TierSS(Tiers.NETHERITE, ModItemTags.INGOTS_NETHERITE);
   public static final TierSS OBSIDIAN = new TierSS(1024, 5.0F, 1.5F, 5, ModItemTags.OBSIDIAN);
   public static final TierSS TIN = new TierSS(180, 5.25F, 1.75F, 6, ModItemTags.TIN_INGOT);
   public static final TierSS BRONZE = new TierSS(320, 5.75F, 2.0F, 12, ModItemTags.BRONZE_INGOT);
   public static final TierSS STEEL = new TierSS(480, 6.5F, 3.0F, 14, ModItemTags.STEEL_INGOT);
   public static final TierSS SILVER = new TierSS(48, 5.0F, 1.5F, 16, ModItemTags.SILVER_INGOT);
   public static final TierSS ELECTRUM = new TierSS(180, 3.5F, 3.5F, 16, ModItemTags.ELECTRUM_INGOT);
   public static final TierSS LEAD = new TierSS(240, 4.5F, 2.0F, 5, ModItemTags.LEAD_INGOT);
   public static final TierSS NICKEL = new TierSS(200, 5.0F, 2.0F, 6, ModItemTags.NICKEL_INGOT);
   public static final TierSS INVAR = new TierSS(440, 6.0F, 2.2F, 12, ModItemTags.INVAR_INGOT);
   public static final TierSS CONSTANTAN = new TierSS(300, 5.5F, 2.75F, 7, ModItemTags.CONSTANTAN_INGOT);
   public static final TierSS PLATINUM = new TierSS(1024, 4.0F, 4.0F, 18, ModItemTags.PLATINUM_INGOT);
   public static final TierSS ALUMINUM = new TierSS(400, 5.0F, 2.125F, 7, ModItemTags.ALUMINUM_INGOT);
   public static final TierSS MANASTEEL = new TierSS(300, 6.2F, 2.0F, 20, ModItemTags.MANASTEEL_INGOT);
   public static final TierSS TERRASTEEL = new TierSS(2300, 9.0F, 3.0F, 26, ModItemTags.TERRASTEEL_INGOT);
   public static final TierSS ELEMENTIUM = new TierSS(720, 6.2F, 2.0F, 20, ModItemTags.ELEMENTIUM_INGOT);
   public static final TierSS OSMIUM = new TierSS(750, 10.0F, 2.0F, 12, ModItemTags.OSMIUM_INGOT);
   public static final TagKey<Item> LAPIS_TAG = ModItemTags.GEMS_LAPIS;
   public static final TierSS LAPIS_LAZULI = new TierSS(200, 5.0F, 2.0F, 8, LAPIS_TAG);
   public static final TierSS REFINED_GLOWSTONE = new TierSS(300, 14.0F, 2.0F, 18, ModItemTags.REFINED_GLOWSTONE_INGOT);
   public static final TierSS REFINED_OBSIDIAN = new TierSS(2500, 20.0F, 10.0F, 40, ModItemTags.REFINED_OBSIDIAN_INGOT);
   public static final TierSS SIGNALUM = new TierSS(1536, 6.0F, 5.0F, 14, ModItemTags.SIGNALUM_INGOT);
   public static final TierSS LUMIUM = new TierSS(840, 5.0F, 4.0F, 16, ModItemTags.LUMIUM_INGOT);
   public static final TierSS ENDERIUM = new TierSS(2048, 7.5F, 8.0F, 18, ModItemTags.ENDERIUM_INGOT);

   private final int durability;
   private final float speed;
   private final float attackDamage;
   private final int enchantability;
   private final TagKey<Item> repairTag;

   public TierSS(int durabilityIn, float speedIn, float attackDamageIn, int enchantabilityIn, TagKey<Item> repairMaterialTag) {
      this.durability = durabilityIn;
      this.speed = speedIn;
      this.attackDamage = attackDamageIn;
      this.enchantability = enchantabilityIn;
      this.repairTag = repairMaterialTag;
   }

   public TierSS(Tier tier, TagKey<Item> repairMaterialTag) {
      this(tier.getUses(), tier.getSpeed(), tier.getAttackDamageBonus(), tier.getEnchantmentValue(), repairMaterialTag);
   }

   @Override
   public int getUses() {
      return this.durability;
   }

   @Override
   public float getSpeed() {
      return this.speed;
   }

   @Override
   public float getAttackDamageBonus() {
      return this.attackDamage;
   }

   @Override
   public int getEnchantmentValue() {
      return this.enchantability;
   }

   @Override
   public TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
      return net.minecraft.tags.BlockTags.INCORRECT_FOR_IRON_TOOL;
   }

   @Override
   public Ingredient getRepairIngredient() {
      return Ingredient.of(this.repairTag);
   }

   public TagKey<Item> getRepairTag() {
      return this.repairTag;
   }

   public String getRepairTagName() {
      return this.repairTag.location().toString();
   }
}
