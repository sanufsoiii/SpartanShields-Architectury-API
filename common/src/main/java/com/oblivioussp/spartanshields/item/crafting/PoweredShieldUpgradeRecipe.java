package com.oblivioussp.spartanshields.item.crafting;

import com.mojang.serialization.MapCodec;
import com.oblivioussp.spartanshields.init.ModRecipes;
import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import com.oblivioussp.spartanshields.item.IItemPoweredFE;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class PoweredShieldUpgradeRecipe implements CraftingRecipe {
   private final ShapedRecipe internalRecipe;

   public PoweredShieldUpgradeRecipe(ShapedRecipe baseRecipe) {
      this.internalRecipe = baseRecipe;
   }

   @Override
   public boolean matches(CraftingInput inv, Level level) {
      return this.internalRecipe.matches(inv, level);
   }

   @Override
   public ItemStack assemble(CraftingInput inv, HolderLookup.Provider lookup) {
      ItemStack resultStack = this.getResultItem(lookup).copy();
      int feToTransfer = 0;

      for (int i = 0; i < inv.size(); i++) {
         ItemStack stack = inv.getItem(i);
         if (stack.getItem() instanceof IItemPoweredFE powered) {
            feToTransfer += powered.getFEStored(stack);
         }
      }

      if (resultStack.getItem() instanceof IItemPoweredFE powered) {
         int maxFE = powered.getFECapacity(resultStack);
         feToTransfer = Mth.clamp(feToTransfer, 0, maxFE);
         final int energy = feToTransfer;
         CustomData.update(DataComponents.CUSTOM_DATA, resultStack, tag -> tag.putInt(FEPoweredShieldItem.NBT_ENERGY, energy));
      }

      return resultStack;
   }

   @Override
   public boolean canCraftInDimensions(int width, int height) {
      return this.internalRecipe.canCraftInDimensions(width, height);
   }

   @Override
   public ItemStack getResultItem(HolderLookup.Provider lookup) {
      return this.internalRecipe.getResultItem(lookup);
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return ModRecipes.POWERED_SHIELD_UPGRADE.get();
   }

   @Override
   public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
      return this.internalRecipe.getRemainingItems(inv);
   }

   public int getWidth() {
      return this.internalRecipe.getWidth();
   }

   public int getHeight() {
      return this.internalRecipe.getHeight();
   }

   @Override
   public CraftingBookCategory category() {
      return CraftingBookCategory.EQUIPMENT;
   }

   public static class Serializer implements RecipeSerializer<PoweredShieldUpgradeRecipe> {
      public static final MapCodec<PoweredShieldUpgradeRecipe> CODEC = RecipeSerializer.SHAPED_RECIPE.codec()
            .xmap(PoweredShieldUpgradeRecipe::new, recipe -> recipe.internalRecipe);

      public static final StreamCodec<RegistryFriendlyByteBuf, PoweredShieldUpgradeRecipe> STREAM_CODEC =
            RecipeSerializer.SHAPED_RECIPE.streamCodec().map(PoweredShieldUpgradeRecipe::new, recipe -> recipe.internalRecipe);

      @Override
      public MapCodec<PoweredShieldUpgradeRecipe> codec() {
         return CODEC;
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, PoweredShieldUpgradeRecipe> streamCodec() {
         return STREAM_CODEC;
      }
   }
}
