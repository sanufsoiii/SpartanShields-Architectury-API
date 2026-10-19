package com.oblivioussp.spartanshields.forge.util;

import com.oblivioussp.spartanshields.item.IItemPoweredFE;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Forge bridge that exposes a powered shield's energy through the Forge capability API, so
 * Mekanism/EnderIO energy pipes keep charging the shields exactly as they did in the Forge release.
 * <p>
 * Note: this is the only place in the project that still touches Forge's energy API.
 * On Fabric there is no equivalent capability layer in 1.20.1, so shields keep their charge in NBT
 * and expose it through {@link IItemPoweredFE}.
 */
public class EnergyCapabilityProviderItem implements ICapabilityProvider {
   public final IEnergyStorage storage;

   public EnergyCapabilityProviderItem(final ItemStack stack, final IItemPoweredFE item) {
      this.storage = new IEnergyStorage() {
         @Override
         public int receiveEnergy(int maxReceive, boolean simulate) {
            return item.receiveFE(stack, maxReceive, simulate);
         }

         @Override
         public int extractEnergy(int maxExtract, boolean simulate) {
            return item.extractFE(stack, maxExtract, simulate);
         }

         @Override
         public int getEnergyStored() {
            return item.getFEStored(stack);
         }

         @Override
         public int getMaxEnergyStored() {
            return item.getFECapacity(stack);
         }

         @Override
         public boolean canExtract() {
            return item.canExtractFE(stack);
         }

         @Override
         public boolean canReceive() {
            return item.canReceiveFE(stack);
         }
      };
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
      return capability == ForgeCapabilities.ENERGY ? LazyOptional.of(() -> this.storage).cast() : LazyOptional.empty();
   }
}
