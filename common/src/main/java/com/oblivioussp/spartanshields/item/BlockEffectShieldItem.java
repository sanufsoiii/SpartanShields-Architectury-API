package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlockEffectShieldItem extends BasicShieldItem implements IDamageShield {
   private final MobEffect effect;
   private final int effectTicks;
   private final int effectLevel;

   public BlockEffectShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, MobEffect effectIn,
                                int effectTicksIn, int effectLevelIn, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop);
      this.effect = effectIn;
      this.effectTicks = effectTicksIn;
      this.effectLevel = effectLevelIn;
   }

   public BlockEffectShieldItem(TierSS toolMaterial, int defaultMaxDamage, MobEffect effectIn, int effectTicksIn,
                                int effectLevelIn, Properties prop) {
      this(toolMaterial, defaultMaxDamage, false, effectIn, effectTicksIn, effectLevelIn, prop);
   }

   @Override
   public void damageShield(ItemStack shieldStack, Player player, Entity attacker, float damage) {
      if (attacker instanceof LivingEntity attackerLiving) {
         attackerLiving.addEffect(new MobEffectInstance(this.effect, this.effectTicks, this.effectLevel, false, true));
      }
   }

   @Override
   public void appendHoverText(ItemStack stack, Level levelIn, List<Component> tooltip, TooltipFlag flagIn) {
      super.appendHoverText(stack, levelIn, tooltip, flagIn);
      MutableComponent effectName = Component.translatable(this.effect.getDescriptionId());
      if (this.effectLevel > 0) {
         effectName = Component.translatable("potion.withAmplifier", effectName, Component.translatable("potion.potency." + this.effectLevel));
      }

      tooltip.add(Component.translatable("tooltip.spartanshields.on_block",
                  Component.translatable("tooltip.spartanshields.inflict_mob_effect.desc", effectName.withStyle(ChatFormatting.AQUA),
                        (float) this.effectTicks / 20.0F)
                        .withStyle(ChatFormatting.GRAY))
            .withStyle(ChatFormatting.GOLD));
   }
}
