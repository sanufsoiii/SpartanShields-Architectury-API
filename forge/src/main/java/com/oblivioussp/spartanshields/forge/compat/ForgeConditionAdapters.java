package com.oblivioussp.spartanshields.forge.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oblivioussp.spartanshields.crafting.condition.AnyModLoadedCondition;
import com.oblivioussp.spartanshields.crafting.condition.ModLoadedCondition;
import com.oblivioussp.spartanshields.crafting.condition.NotCondition;
import com.oblivioussp.spartanshields.crafting.condition.SpartanCondition;
import com.oblivioussp.spartanshields.crafting.condition.TagEmptyCondition;
import com.oblivioussp.spartanshields.crafting.condition.TypeDisabledCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Teaches Forge's own {@code CraftingHelper} about this mod's condition types.
 * <p>
 * Note: the shipped data pack uses {@code spartanshields:*} conditions instead of Forge's
 * {@code forge:not} / {@code forge:tag_empty} / {@code forge:mod_loaded}, because Fabric cannot parse
 * Forge's conditions at all. Forge drops any recipe whose conditions it cannot resolve, so every
 * condition is additionally wrapped in a thin {@link ICondition} adapter here. The evaluation itself
 * is shared with {@code mixin.MixinRecipeManager}, which filters the same recipes with the same
 * rules, so both layers always agree and nothing is dropped twice.
 */
public final class ForgeConditionAdapters {
   private ForgeConditionAdapters() {
   }

   public static void register() {
      register(TypeDisabledCondition.ID, TypeDisabledCondition::new, TypeDisabledCondition::write);
      register(ModLoadedCondition.ID, ModLoadedCondition::new, ModLoadedCondition::write);
      register(AnyModLoadedCondition.ID, AnyModLoadedCondition::new, AnyModLoadedCondition::write);
      register(TagEmptyCondition.ID, TagEmptyCondition::new, TagEmptyCondition::write);
      register(NotCondition.ID, NotCondition::new, NotCondition::write);
   }

   private static <T extends SpartanCondition> void register(String id, Supplier<T> factory,
                                                             Function<T, JsonObject> writer) {
      CraftingHelper.register(new IConditionSerializer<ForgeCondition<T>>() {
         @Override
         public ForgeCondition<T> read(JsonObject json) {
            return new ForgeCondition<>(new ResourceLocation(id), cast(factory.get().read(json)));
         }

         @Override
         public void write(JsonObject json, ForgeCondition<T> value) {
            // The delegate writes its full json; Forge adds the "type" key itself, so it is
            // stripped here to keep the result a flat, round-trippable condition object.
            JsonObject delegateJson = writer.apply(value.delegate());
            for (String key : delegateJson.keySet()) {
               if (!"type".equals(key)) {
                  JsonElement element = delegateJson.get(key);
                  json.add(key, element);
               }
            }
         }

         @Override
         public ResourceLocation getID() {
            return new ResourceLocation(id);
         }

         @SuppressWarnings("unchecked")
         private T cast(SpartanCondition condition) {
            return (T) condition;
         }
      });
   }

   private record ForgeCondition<T extends SpartanCondition>(ResourceLocation id, T delegate) implements ICondition {
      @Override
      public ResourceLocation getID() {
         return this.id;
      }

      @Override
      public boolean test(IContext context) {
         if (this.delegate instanceof TagEmptyCondition tagEmpty) {
            ResourceLocation tagLoc = tagEmpty.getTagLocation();
            if (tagLoc != null) {
               return context.getTag(TagKey.create(Registries.ITEM, tagLoc)).isEmpty();
            }
         } else if (this.delegate instanceof NotCondition notCond) {
            if (notCond.getInner() instanceof TagEmptyCondition tagEmpty) {
               ResourceLocation tagLoc = tagEmpty.getTagLocation();
               if (tagLoc != null) {
                  return !context.getTag(TagKey.create(Registries.ITEM, tagLoc)).isEmpty();
               }
            }
         }
         return this.delegate.test();
      }
   }
}