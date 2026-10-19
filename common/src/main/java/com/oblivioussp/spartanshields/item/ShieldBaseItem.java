package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.init.ModEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

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

   /**
    * Configured durability.
    * <p>
    * Note: the Forge release overrode {@code Item#getMaxDamage(ItemStack)}, a Forge-only
    * hook that vanilla/Fabric never call, and {@code Item#getMaxDamage()} is {@code final} in
    * vanilla. The value is therefore published through {@link #getConfiguredMaxDamage()} and
    * applied by {@code mixin.MixinItemStack#getMaxDamage}, which both loaders share.
    */
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
   public void appendHoverText(ItemStack stackIn, Level levelIn, List<Component> tooltipIn, TooltipFlag flagIn) {
      if (this.isTowerShield && !stackIn.isEmpty() && stackIn.hasTag() && stackIn.getTag().contains("BlockEntityTag")) {
         DyeColor dyeColor = ShieldItem.getColor(stackIn);
         tooltipIn.add(Component.empty());
         tooltipIn.add(Component.translatable("tooltip.spartanshields.has_patterns"));
         tooltipIn.add(Component.translatable(String.format("block.minecraft.%s_banner", dyeColor.name().toLowerCase()))
               .withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY));
         BannerItem.appendHoverTextFromBannerBlockEntityTag(stackIn, tooltipIn);
      }

      this.addEffectsTooltip(stackIn, levelIn, tooltipIn, flagIn);
   }

   /**
    * Note: {@code ItemStack#getEnchantmentLevel} is a Forge extension
    * ({@code IForgeItemStack}) and 1.20.1's {@code EnchantmentHelper} only offers the entity and raw
    * nbt overloads, so the level is read from the stack's enchantment map.
    */
   protected static int enchantmentLevel(ItemStack stack, Enchantment enchantment) {
      return EnchantmentHelper.getEnchantments(stack).getOrDefault(enchantment, 0);
   }

   public void addEffectsTooltip(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
      if (enchantmentLevel(stack, ModEnchantments.PAYBACK.get()) != 0) {
         float paybackDamage = stack.getOrCreateTag().getFloat(PaybackDamage.NBT_PAYBACK_DMG);
         tooltip.add(Component.translatable("tooltip.spartanshields.payback_bonus", ChatFormatting.GRAY + Float.toString(paybackDamage))
               .withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }
}
