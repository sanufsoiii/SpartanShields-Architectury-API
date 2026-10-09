package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BasicShieldItem extends ShieldBaseItem {
   protected final TierSS material;

   public BasicShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, Properties prop) {
      super(defaultMaxDamage, isTowerShieldIn, prop);
      this.material = toolMaterial;
   }

   public BasicShieldItem(TierSS toolMaterial, int defaultMaxDamage, Properties prop) {
      this(toolMaterial, defaultMaxDamage, false, prop);
   }

   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
      tooltip.add(Component.translatable("tooltip.spartanshields.protection", this.getMaxDamage(stack)));

      if (!Config.INSTANCE.forceDisableUncraftableTooltips.get()) {
         var tagOpt = net.minecraft.core.registries.BuiltInRegistries.ITEM.getTag(this.material.getRepairTag());
         if (tagOpt.isEmpty() || tagOpt.get().size() == 0) {
            tooltip.add(Component.translatable("tooltip.spartanshields.uncraftable_missing_material", this.material.getRepairTagName())
                  .withStyle(ChatFormatting.RED));
         }
      }

      super.appendHoverText(stack, context, tooltip, flagIn);
   }

   @Override
   public int getEnchantmentValue() {
      return this.material.getEnchantmentValue();
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      return this.material.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
   }
}
