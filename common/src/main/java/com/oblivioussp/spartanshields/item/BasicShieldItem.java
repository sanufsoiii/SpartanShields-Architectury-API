package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BasicShieldItem extends ShieldBaseItem {
   protected final TierSS material;
   protected boolean doCraftCheck = true;
   protected boolean canBeCrafted = true;

   public BasicShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, Properties prop) {
      super(defaultMaxDamage, isTowerShieldIn, prop);
      this.material = toolMaterial;
   }

   public BasicShieldItem(TierSS toolMaterial, int defaultMaxDamage, Properties prop) {
      this(toolMaterial, defaultMaxDamage, false, prop);
   }

   @Override
   public void appendHoverText(ItemStack stack, Level levelIn, List<Component> tooltip, TooltipFlag flagIn) {
      tooltip.add(Component.translatable("tooltip.spartanshields.protection", this.getMaxDamage(stack)));

      if (this.doCraftCheck && levelIn != null) {
         if (!Config.INSTANCE.forceDisableUncraftableTooltips.get()) {
            // Porting note: Forge used ForgeRegistries.ITEMS.tags() + ITagManager to ask whether a
            // repair tag is known and non-empty. Vanilla has no equivalent query, so emptiness of
            // the repair ingredient is used as the "material unavailable" signal on both loaders.
            if (this.material.getRepairIngredient().getItems().length == 0) {
               this.canBeCrafted = false;
            }
         }

         this.doCraftCheck = false;
      }

      if (!this.canBeCrafted) {
         tooltip.add(Component.translatable("tooltip.spartanshields.uncraftable_missing_material", this.material.getRepairTagName())
               .withStyle(ChatFormatting.RED));
      }

      super.appendHoverText(stack, levelIn, tooltip, flagIn);
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
