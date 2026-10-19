package com.oblivioussp.spartanshields.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.oblivioussp.spartanshields.init.ModRecipes;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import net.minecraft.world.level.Level;

/**
 * Applies a banner to a Spartan Shield.
 * <p>
 * Note: {@code ForgeRegistries.ITEMS} was replaced by the vanilla {@link BuiltInRegistries},
 * which behaves identically on Fabric.
 */
public class ShieldBannerRecipe extends ShieldDecorationRecipe {
   protected final Item shieldItem;

   public ShieldBannerRecipe(ResourceLocation loc, Item shield) {
      super(loc, CraftingBookCategory.EQUIPMENT);
      this.shieldItem = shield;
   }

   @Override
   public boolean matches(CraftingContainer inv, Level level) {
      ItemStack shield = ItemStack.EMPTY;
      ItemStack banner = ItemStack.EMPTY;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (!stack.isEmpty()) {
            if (stack.is(ItemTags.BANNERS)) {
               if (!banner.isEmpty()) {
                  return false;
               }
               banner = stack;
            } else {
               if (stack.getItem() != this.shieldItem || !shield.isEmpty()) {
                  return false;
               }

               if (stack.getTagElement("BlockEntityTag") != null) {
                  return false;
               }

               shield = stack;
            }
         }
      }

      return !shield.isEmpty() && !banner.isEmpty();
   }

   @Override
   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      ItemStack banner = ItemStack.EMPTY;
      ItemStack shield = ItemStack.EMPTY;

      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack stack = inv.getItem(i);
         if (!stack.isEmpty()) {
            if (stack.is(ItemTags.BANNERS)) {
               banner = stack;
            } else if (stack.getItem() == this.shieldItem) {
               shield = stack.copy();
            }
         }
      }

      if (shield.isEmpty()) {
         return shield;
      }

      CompoundTag tag = banner.getTagElement("BlockEntityTag");
      CompoundTag tagCopy = tag == null ? new CompoundTag() : tag.copy();
      tagCopy.putInt("Base", ((BannerItem) banner.getItem()).getColor().getId());
      shield.addTagElement("BlockEntityTag", tagCopy);
      return shield;
   }

   @Override
   public ItemStack getResultItem(RegistryAccess registryAccess) {
      return ItemStack.EMPTY;
   }

   /*
    * Note: the Forge release overrode getRemainingItems() through Forge's
    * Item#getCraftingRemainingItem(ItemStack)/hasCraftingRemainingItem(ItemStack) pair. Those
    * overloads do not exist in vanilla (where ItemStack#getCraftingRemainingItem() is used instead)
    * and shields have no crafting remaining item, so Recipe's default implementation is equivalent.
    */

   @Override
   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 2;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return ModRecipes.SHIELD_BANNER.get();
   }

   public Item getShieldItem() {
      return this.shieldItem;
   }

   public static class Serializer implements RecipeSerializer<ShieldBannerRecipe> {
      @Override
      public ShieldBannerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
         String shieldName = GsonHelper.getAsString(json, "shield");
         Item shieldItem = BuiltInRegistries.ITEM.get(new ResourceLocation(shieldName));
         if (shieldItem == null) {
            throw new JsonSyntaxException("Item " + shieldName + " did not exist!");
         }

         if (!(shieldItem instanceof ShieldBaseItem)) {
            throw new JsonSyntaxException("Item " + shieldName + " is not a valid Shield!");
         }

         return new ShieldBannerRecipe(recipeId, shieldItem);
      }

      @Override
      public ShieldBannerRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
         ResourceLocation shieldName = buffer.readResourceLocation();
         Item shield = BuiltInRegistries.ITEM.get(shieldName);
         return new ShieldBannerRecipe(recipeId, shield);
      }

      @Override
      public void toNetwork(FriendlyByteBuf buffer, ShieldBannerRecipe recipe) {
         buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(recipe.shieldItem));
      }
   }
}
