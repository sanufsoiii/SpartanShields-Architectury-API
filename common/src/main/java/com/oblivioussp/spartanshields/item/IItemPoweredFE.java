package com.oblivioussp.spartanshields.item;

import net.minecraft.world.item.ItemStack;

/**
 * Loader-agnostic energy storage contract for the powered shields.
 * <p>
 * Note: on Forge this is bridged to {@code IEnergyStorage} through
 * {@code forge/util/EnergyCapabilityProviderItem}, so pipes from Mekanism/EnderIO keep working.
 * Fabric has no Forge-equivalent capability API in 1.20.1, so the energy is kept in the stack's
 * NBT and read/written through this interface.
 */
public interface IItemPoweredFE {
   int receiveFE(ItemStack container, int maxReceive, boolean simulate);

   int extractFE(ItemStack container, int maxExtract, boolean simulate);

   int getFEStored(ItemStack container);

   int getFECapacity(ItemStack container);

   boolean canExtractFE(ItemStack stack);

   boolean canReceiveFE(ItemStack stack);
}
