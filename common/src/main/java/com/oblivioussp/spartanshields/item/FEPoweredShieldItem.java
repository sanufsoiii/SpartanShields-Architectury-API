package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.util.PowerUnit;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
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

   /**
    * Forge-only hook in the original release (there is no {@code Item#setDamage} in vanilla), kept
    * without {@code @Override} so powered shields never accumulate durability on Forge.
    */
   public void setDamage(ItemStack stack, int damage) {
   }

   @Override
   public int getBarWidth(ItemStack stack) {
      int energy = stack.getOrCreateTag().getInt(NBT_ENERGY);
      return Math.round(13.0F * (float) energy / (float) this.energyCapacity);
   }

   @Override
   public boolean isBarVisible(ItemStack stack) {
      int energy = stack.getOrCreateTag().getInt(NBT_ENERGY);
      return energy < this.energyCapacity;
   }

   @Override
   public int getBarColor(ItemStack stack) {
      return BAR_COLOUR;
   }

   /**
    * Forge-only hook in the original release; kept (without {@code @Override}) so the energy
    * bar colour survives on Forge.
    */
   public int getRGBDurabilityForDisplay(ItemStack stack) {
      return BAR_COLOUR;
   }

   /**
    * Forge-only hook: returns the energy capacity as the "max damage" so the durability bar shows
    * charge. Vanilla has no equivalent, but {@link #getBarWidth(ItemStack)} drives the bar on both
    * loaders anyway.
    */
   public int getMaxDamage(ItemStack stack) {
      return this.energyCapacity;
   }

   @Override
   public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
      // Porting note: the original used Minecraft.getInstance() + I18n directly inside a
      // client-only method; the environment check keeps the class loadable on a dedicated
      // server, where tooltips are never rendered anyway.
      NumberFormat formatter = NumberFormat.getIntegerInstance(
              Platform.getEnvironment() == Env.CLIENT ? clientLocale() : Locale.getDefault());
      float scale = this.preferredEnergyUnit.getEnergyScaleToFE();

      tooltip.add(Component.translatable("tooltip.spartanshields." + this.preferredEnergyUnit.getCapacityTranslationKey(),
            formatter.format((long) Mth.floor((float) this.getFEStored(stack) * scale)),
            formatter.format((long) Mth.floor((float) this.getFECapacity(stack) * scale))));
      tooltip.add(Component.translatable("tooltip.spartanshields." + this.preferredEnergyUnit.getEnergyChargeRateTranslationKey(),
            formatter.format((long) Mth.floor((float) this.maxEnergyReceive * scale))));
      tooltip.add(Component.translatable("tooltip.spartanshields." + this.preferredEnergyUnit.getEnergyPerDamageTranslationKey(),
            formatter.format((long) Mth.floor((float) ((int) Config.INSTANCE.damageToFEMultiplier.get() * 2) * scale))));
      tooltip.add(Component.translatable("tooltip.spartanshields.fe_shield.desc"));
   }

   /**
    * Porting note: {@code LanguageManager#getJavaLocale()} is a Forge patch that does not exist on
    * Fabric, so the selected language code is turned into a {@link Locale} directly.
    */
   private static Locale clientLocale() {
      return Locale.forLanguageTag(net.minecraft.client.Minecraft.getInstance().getLanguageManager().getSelected());
   }

   @Override
   public UseAnim getUseAnimation(ItemStack stack) {
      return this.getFEStored(stack) > 0 ? UseAnim.BLOCK : UseAnim.NONE;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      player.startUsingItem(hand);
      ItemStack stack = player.getItemInHand(hand);
      if (this.getFEStored(stack) > 0) {
         return InteractionResultHolder.consume(stack);
      }

      if (level.isClientSide) {
         // Component.translatable is loader-agnostic, unlike the I18n.get() call the Forge
         // release used (which had to be guarded by @OnlyIn).
         player.sendSystemMessage(Component.literal(ChatFormatting.YELLOW.toString())
               .append(Component.translatable("message.spartanshields.powered_shield_block_fail", stack.getHoverName())));
      }

      return InteractionResultHolder.fail(stack);
   }

   @Override
   public void damageShield(ItemStack shieldStack, Player player, Entity attacker, float damage) {
      int energyToUse = Mth.floor(damage);
      int currentEnergy = shieldStack.getOrCreateTag().getInt(NBT_ENERGY);
      int energyRemoved = Math.min(energyToUse, currentEnergy);
      currentEnergy -= energyRemoved;
      shieldStack.getTag().putInt(NBT_ENERGY, currentEnergy);
      if (currentEnergy == 0) {
         Level level = player.level();
         player.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
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
      int energy = container.getOrCreateTag().getInt(NBT_ENERGY);
      int energyReceived = Math.min(this.energyCapacity - energy, Math.min(this.maxEnergyReceive, maxReceive));
      if (!simulate) {
         energy += energyReceived;
         container.getTag().putInt(NBT_ENERGY, energy);
      }

      return energyReceived;
   }

   @Override
   public int extractFE(ItemStack container, int maxExtract, boolean simulate) {
      return 0;
   }

   @Override
   public int getFEStored(ItemStack container) {
      return !container.getOrCreateTag().contains(NBT_ENERGY) ? 0 : container.getTag().getInt(NBT_ENERGY);
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
