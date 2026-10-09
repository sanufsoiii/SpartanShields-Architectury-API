package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.util.PowerUnit;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FEPoweredShieldItem extends ShieldBaseItem implements IDamageShield, IItemPoweredFE {
   public static final String NBT_ENERGY = "Energy";
   private static final int BAR_COLOUR = 6927359;

   protected int energyCapacity;
   protected int maxEnergyReceive;
   protected final String modName;
   protected final PowerUnit preferredEnergyUnit;

   public FEPoweredShieldItem(int capacity, int maxReceive, String modName, PowerUnit preferredUnit, boolean isTowerShieldIn, Properties prop) {
      super(0, isTowerShieldIn, prop);
      this.energyCapacity = isTowerShieldIn ? Mth.floor((float) capacity * 1.25F) : capacity;
      this.maxEnergyReceive = maxReceive;
      this.modName = modName;
      this.preferredEnergyUnit = preferredUnit;
   }

   public void setDamage(ItemStack stack, int damage) {
   }

   @Override
   public int getBarWidth(ItemStack stack) {
      int energy = getFEStored(stack);
      return Math.round(13.0F * (float) energy / (float) this.energyCapacity);
   }

   @Override
   public boolean isBarVisible(ItemStack stack) {
      int energy = getFEStored(stack);
      return energy < this.energyCapacity;
   }

   @Override
   public int getBarColor(ItemStack stack) {
      return BAR_COLOUR;
   }

   public int getRGBDurabilityForDisplay(ItemStack stack) {
      return BAR_COLOUR;
   }

   public int getMaxDamage(ItemStack stack) {
      return this.energyCapacity;
   }

   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, TooltipFlag flagIn) {
      NumberFormat formatter = NumberFormat.getIntegerInstance(
              Platform.getEnvironment() == Env.CLIENT ? clientLocale() : Locale.getDefault());
      float scale = this.preferredEnergyUnit.getEnergyScaleToFE();

      tooltip.accept(Component.translatable("tooltip.spartanshields." + this.preferredEnergyUnit.getCapacityTranslationKey(),
            formatter.format((long) Mth.floor((float) this.getFEStored(stack) * scale)),
            formatter.format((long) Mth.floor((float) this.getFECapacity(stack) * scale))));
      tooltip.accept(Component.translatable("tooltip.spartanshields." + this.preferredEnergyUnit.getEnergyChargeRateTranslationKey(),
            formatter.format((long) Mth.floor((float) this.maxEnergyReceive * scale))));
      tooltip.accept(Component.translatable("tooltip.spartanshields." + this.preferredEnergyUnit.getEnergyPerDamageTranslationKey(),
            formatter.format((long) Mth.floor((float) ((int) Config.INSTANCE.damageToFEMultiplier.get() * 2) * scale))));
      tooltip.accept(Component.translatable("tooltip.spartanshields.fe_shield.desc"));
   }

   private static Locale clientLocale() {
      return Locale.forLanguageTag(net.minecraft.client.Minecraft.getInstance().getLanguageManager().getSelected());
   }

   @Override
   public ItemUseAnimation getUseAnimation(ItemStack stack) {
      return this.getFEStored(stack) > 0 ? ItemUseAnimation.BLOCK : ItemUseAnimation.NONE;
   }

   @Override
   public InteractionResult use(Level level, Player player, InteractionHand hand) {
      player.startUsingItem(hand);
      ItemStack stack = player.getItemInHand(hand);
      if (this.getFEStored(stack) > 0) {
         return InteractionResult.CONSUME;
      }

      if (level.isClientSide()) {
         player.displayClientMessage(Component.literal(ChatFormatting.YELLOW.toString())
               .append(Component.translatable("message.spartanshields.powered_shield_block_fail", stack.getHoverName())), false);
      }

      return InteractionResult.FAIL;
   }

   @Override
   public void damageShield(ItemStack shieldStack, Player player, Entity attacker, float damage) {
      int energyToUse = Mth.floor(damage);
      int currentEnergy = getFEStored(shieldStack);
      int energyRemoved = Math.min(energyToUse, currentEnergy);
      currentEnergy -= energyRemoved;
      final int finalEnergy = currentEnergy;
      CustomData.update(DataComponents.CUSTOM_DATA, shieldStack, tag -> tag.putInt(NBT_ENERGY, finalEnergy));
      if (currentEnergy == 0) {
         Level level = player.level();
         player.playSound(SoundEvents.SHIELD_BREAK.value(), 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
      }
   }

   public FEPoweredShieldItem setCapacity(int capacity) {
      this.energyCapacity = capacity;
      return this;
   }

   public FEPoweredShieldItem setMaxReceive(int maxReceive) {
      this.maxEnergyReceive = maxReceive;
      return this;
   }

   public int getMaxEnergyReceive() {
      return this.maxEnergyReceive;
   }

   public String getModName() {
      return this.modName;
   }

   public PowerUnit getPreferredEnergyUnit() {
      return this.preferredEnergyUnit;
   }

   @Override
   public int receiveFE(ItemStack container, int maxReceive, boolean simulate) {
      int energy = getFEStored(container);
      int energyReceived = Math.min(this.energyCapacity - energy, Math.min(this.maxEnergyReceive, maxReceive));
      if (!simulate) {
         final int finalEnergy = energy + energyReceived;
         CustomData.update(DataComponents.CUSTOM_DATA, container, tag -> tag.putInt(NBT_ENERGY, finalEnergy));
      }

      return energyReceived;
   }

   @Override
   public int extractFE(ItemStack container, int maxExtract, boolean simulate) {
      return 0;
   }

   @Override
   public int getFEStored(ItemStack container) {
      CustomData customData = container.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return customData.copyTag().getIntOr(NBT_ENERGY, 0);
   }

   @Override
   public int getFECapacity(ItemStack container) {
      return this.energyCapacity;
   }

   @Override
   public boolean canExtractFE(ItemStack stack) {
      return false;
   }

   @Override
   public boolean canReceiveFE(ItemStack stack) {
      return true;
   }
}
