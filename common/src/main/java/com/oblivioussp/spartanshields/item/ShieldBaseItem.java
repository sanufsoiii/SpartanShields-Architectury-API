package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.init.ModEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public class ShieldBaseItem extends ShieldItem {
   protected final boolean isTowerShield;
   protected int maxDurability;

   public ShieldBaseItem(int defaultDurability, boolean isTowerShieldIn, Properties prop) {
      super(prop.durability(isTowerShieldIn ? Mth.floor((float) defaultDurability * 1.25F) : defaultDurability));
      this.maxDurability = defaultDurability;
      this.isTowerShield = isTowerShieldIn;
   }

   public boolean isTowerShield() {
      return this.isTowerShield;
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      return false;
   }

   @Override
   public String getDescriptionId(ItemStack stack) {
      return this.getDescriptionId();
   }

   public int getMaxDamage(ItemStack stack) {
      return this.maxDurability;
   }

   public int getConfiguredMaxDamage() {
      return this.maxDurability;
   }

   public void setMaxDamage(int maxDamage) {
      this.maxDurability = this.isTowerShield
            ? Mth.floor((double) maxDamage * Config.INSTANCE.towerShieldDurabilityMultiplier.get())
            : maxDamage;
   }

   @Override
   public void appendHoverText(ItemStack stackIn, Item.TooltipContext context, List<Component> tooltipIn, TooltipFlag flagIn) {
      DyeColor dyeColor = stackIn.get(DataComponents.BASE_COLOR);
      if (this.isTowerShield && !stackIn.isEmpty() && (dyeColor != null || stackIn.has(DataComponents.BANNER_PATTERNS))) {
         if (dyeColor != null) {
            tooltipIn.add(Component.empty());
            tooltipIn.add(Component.translatable("tooltip.spartanshields.has_patterns"));
            tooltipIn.add(Component.translatable(String.format("block.minecraft.%s_banner", dyeColor.name().toLowerCase()))
                  .withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY));
         }
         BannerItem.appendHoverTextFromBannerBlockEntityTag(stackIn, tooltipIn);
      }

      this.addEffectsTooltip(stackIn, context, tooltipIn, flagIn);
   }

   protected static int enchantmentLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
      return ModEnchantments.getEnchantmentLevel(stack, enchantment);
   }

   public void addEffectsTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
      if (enchantmentLevel(stack, ModEnchantments.PAYBACK) != 0) {
         CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
         float paybackDamage = customData.copyTag().getFloat(PaybackDamage.NBT_PAYBACK_DMG);
         tooltip.add(Component.translatable("tooltip.spartanshields.payback_bonus", ChatFormatting.GRAY + Float.toString(paybackDamage))
               .withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }
}
