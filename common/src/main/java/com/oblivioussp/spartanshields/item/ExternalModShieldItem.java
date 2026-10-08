package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ExternalModShieldItem extends BasicShieldItem {
   protected final String modName;

   public ExternalModShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, String externalModName, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop);
      this.modName = externalModName;
   }

   public String getModName() {
      return this.modName;
   }

   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
      if (!Config.INSTANCE.forceDisableUncraftableTooltips.get()) {
         tooltip.add(Component.translatable("tooltip.spartanshields.uncraftable_missing_mods", this.modName)
               .withStyle(ChatFormatting.RED));
      }

      super.appendHoverText(stack, context, tooltip, flagIn);
   }
}
