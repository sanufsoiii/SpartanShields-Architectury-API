package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BlockEffectShieldItem extends BasicShieldItem implements IDamageShield {
   private final Holder<MobEffect> effect;
   private final int effectTicks;
   private final int effectLevel;

   public BlockEffectShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, Holder<MobEffect> effectIn,
                                int effectTicksIn, int effectLevelIn, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop);
      this.effect = effectIn;
      this.effectTicks = effectTicksIn;
      this.effectLevel = effectLevelIn;
   }

   public BlockEffectShieldItem(TierSS toolMaterial, int defaultMaxDamage, Holder<MobEffect> effectIn, int effectTicksIn,
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
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
      super.appendHoverText(stack, context, tooltip, flagIn);
      MutableComponent effectName = Component.translatable(this.effect.value().getDescriptionId());
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
