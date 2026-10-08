package com.oblivioussp.spartanshields.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oblivioussp.spartanshields.init.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class ShieldBannerRecipe extends CustomRecipe {
   protected final Item shieldItem;

   public ShieldBannerRecipe(Item shield) {
      this(CraftingBookCategory.EQUIPMENT, shield);
   }

   public ShieldBannerRecipe(CraftingBookCategory category, Item shield) {
      super(category);
      this.shieldItem = shield;
   }

   @Override
   public boolean matches(CraftingInput inv, Level level) {
      ItemStack shield = ItemStack.EMPTY;
      ItemStack banner = ItemStack.EMPTY;

      for (int i = 0; i < inv.size(); i++) {
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

               BannerPatternLayers bannerPatterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
               if (!bannerPatterns.layers().isEmpty()) {
                  return false;
               }

               shield = stack;
            }
         }
      }

      return !shield.isEmpty() && !banner.isEmpty();
   }

   @Override
   public ItemStack assemble(CraftingInput inv, HolderLookup.Provider lookup) {
      ItemStack banner = ItemStack.EMPTY;
      ItemStack shield = ItemStack.EMPTY;

      for (int i = 0; i < inv.size(); i++) {
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

      shield.set(DataComponents.BANNER_PATTERNS, banner.get(DataComponents.BANNER_PATTERNS));
      shield.set(DataComponents.BASE_COLOR, ((BannerItem) banner.getItem()).getColor());
      return shield;
   }

   @Override
   public RecipeSerializer<? extends CustomRecipe> getSerializer() {
      return ModRecipes.SHIELD_BANNER.get();
   }

   public Item getShieldItem() {
      return this.shieldItem;
   }

   public static class Serializer implements RecipeSerializer<ShieldBannerRecipe> {
      public static final MapCodec<ShieldBannerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
         instance.group(
            CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.EQUIPMENT).forGetter(CustomRecipe::category),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("shield").forGetter(ShieldBannerRecipe::getShieldItem)
         ).apply(instance, ShieldBannerRecipe::new)
      );

      public static final StreamCodec<RegistryFriendlyByteBuf, ShieldBannerRecipe> STREAM_CODEC = StreamCodec.of(
         (buf, recipe) -> {
            buf.writeEnum(recipe.category());
            buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(recipe.shieldItem));
         },
         buf -> new ShieldBannerRecipe(
            buf.readEnum(CraftingBookCategory.class),
            BuiltInRegistries.ITEM.getValue(buf.readResourceLocation())
         )
      );

      @Override
      public MapCodec<ShieldBannerRecipe> codec() {
         return CODEC;
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, ShieldBannerRecipe> streamCodec() {
         return STREAM_CODEC;
      }
   }
}
