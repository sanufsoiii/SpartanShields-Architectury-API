package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SilverShieldItem extends BasicShieldItem implements IDamageShield {
   public SilverShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop);
   }

   public SilverShieldItem(TierSS toolMaterial, int defaultMaxDamage, Properties prop) {
      this(toolMaterial, defaultMaxDamage, false, prop);
   }

   @Override
   public void appendHoverText(ItemStack stack, Level levelIn, List<Component> tooltip, TooltipFlag flagIn) {
      super.appendHoverText(stack, levelIn, tooltip, flagIn);
      tooltip.add(Component.translatable("tooltip.spartanshields.on_block",
                  Component.translatable("tooltip.spartanshields.shield_silver.desc").withStyle(ChatFormatting.GRAY))
            .withStyle(ChatFormatting.GOLD));
   }

   @Override
   public void damageShield(ItemStack shieldStack, Player player, Entity attacker, float damage) {
      if (attacker instanceof LivingEntity attackerLiving && attackerLiving.getMobType() == MobType.UNDEAD) {
         attackerLiving.hurt(player.damageSources().playerAttack(player), 2.0F);
      }
   }
}
