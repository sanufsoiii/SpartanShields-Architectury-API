package com.oblivioussp.spartanshields.item.crafting;

import com.google.gson.JsonObject;
import com.oblivioussp.spartanshields.init.ModRecipes;
import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import com.oblivioussp.spartanshields.item.IItemPoweredFE;
import com.oblivioussp.spartanshields.util.Log;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

/**
 * Wraps a shaped recipe and moves the stored energy of the ingredients into the result.
 * <p>
 * Porting note: the Forge release summed {@code ForgeCapabilities.ENERGY} over the crafting grid
 * through {@code LazyOptional}. Energy is now read through this mod's own
 * {@link IItemPoweredFE} interface, which the Forge-side capability provider also implements, so
 * the transfer works identically on both loaders (and keeps working with foreign energy items
 * that expose a shield item of ours).
 */
public class PoweredShieldUpgradeRecipe implements CraftingRecipe {
   private final ShapedRecipe internalRecipe;

   public PoweredShieldUpgradeRecipe(ShapedRecipe baseRecipe) {
      this.internalRecipe = baseRecipe;
   }

   @Override
   public boolean matches(CraftingContainer inv, Level level) {
      return this.internalRecipe.matches(inv, level);
   }

   @Override
   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      ItemStack resultStack = this.getResultItem(registryAccess).copy();
      int feToTransfer = 0;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (stack.getItem() instanceof IItemPoweredFE powered) {
            feToTransfer += powered.getFEStored(stack);
         }
      }

      if (resultStack.getItem() instanceof IItemPoweredFE powered) {
         int maxFE = powered.getFECapacity(resultStack);
         feToTransfer = Mth.clamp(feToTransfer, 0, maxFE);
         resultStack.getOrCreateTag().putInt(FEPoweredShieldItem.NBT_ENERGY, feToTransfer);
      }

      return resultStack;
   }

   @Override
   public boolean canCraftInDimensions(int width, int height) {
      return this.internalRecipe.canCraftInDimensions(width, height);
   }

   @Override
   public ItemStack getResultItem(RegistryAccess registryAccess) {
      return this.internalRecipe.getResultItem(registryAccess);
   }

   @Override
   public ResourceLocation getId() {
      return this.internalRecipe.getId();
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return ModRecipes.POWERED_SHIELD_UPGRADE.get();
   }

   @Override
   public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
      return this.internalRecipe.getRemainingItems(inv);
   }

   @Override
   public NonNullList<Ingredient> getIngredients() {
      return this.internalRecipe.getIngredients();
   }

   @Override
   public boolean isSpecial() {
      return this.internalRecipe.isSpecial();
   }

   @Override
   public String getGroup() {
      return this.internalRecipe.getGroup();
   }

   @Override
   public ItemStack getToastSymbol() {
      return this.internalRecipe.getToastSymbol();
   }

   /*
    * Porting note: the Forge release delegated getRecipeWidth()/getRecipeHeight(), which are Forge
    * additions to ShapedRecipe; vanilla exposes getWidth()/getHeight() instead. The recipe book
    * reads the dimensions from the wrapped recipe, so no delegation is needed.
    */

   @Override
   public CraftingBookCategory category() {
      return CraftingBookCategory.EQUIPMENT;
   }

   public static class Serializer implements RecipeSerializer<PoweredShieldUpgradeRecipe> {
      @Override
      public PoweredShieldUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
         return new PoweredShieldUpgradeRecipe(
               (ShapedRecipe) RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json));
      }

      @Override
      public PoweredShieldUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
         try {
            return new PoweredShieldUpgradeRecipe(
                  (ShapedRecipe) RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer));
         } catch (Exception e) {
            Log.error("Failed to read a Powered Shield Upgrade Recipe from a packet!");
            throw e;
         }
      }

      @Override
      public void toNetwork(FriendlyByteBuf buffer, PoweredShieldUpgradeRecipe recipe) {
         try {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe.internalRecipe);
         } catch (Exception e) {
            Log.error("Failed to write a Powered Shield Upgrade Recipe to a packet!");
         }
      }
   }
}
