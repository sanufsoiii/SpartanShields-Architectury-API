package com.oblivioussp.spartanshields.forge.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.crafting.condition.AnyModLoadedCondition;
import com.oblivioussp.spartanshields.crafting.condition.ModLoadedCondition;
import com.oblivioussp.spartanshields.crafting.condition.NotCondition;
import com.oblivioussp.spartanshields.crafting.condition.SpartanCondition;
import com.oblivioussp.spartanshields.crafting.condition.TagEmptyCondition;
import com.oblivioussp.spartanshields.crafting.condition.TypeDisabledCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Teaches Forge's condition serializer registry about this mod's condition types.
 */
public final class ForgeConditionAdapters {
   private static final DeferredRegister<Codec<? extends ICondition>> CONDITION_SERIALIZERS =
         DeferredRegister.create(ForgeRegistries.Keys.CONDITION_SERIALIZERS, SpartanShields.ID);

   private static final Map<String, Codec<? extends ICondition>> CODECS = new HashMap<>();

   private ForgeConditionAdapters() {
   }

   public static void register(IEventBus bus) {
      registerType(TypeDisabledCondition.ID, TypeDisabledCondition::new, TypeDisabledCondition::write);
      registerType(ModLoadedCondition.ID, ModLoadedCondition::new, ModLoadedCondition::write);
      registerType(AnyModLoadedCondition.ID, AnyModLoadedCondition::new, AnyModLoadedCondition::write);
      registerType(TagEmptyCondition.ID, TagEmptyCondition::new, TagEmptyCondition::write);
      registerType(NotCondition.ID, NotCondition::new, NotCondition::write);

      CONDITION_SERIALIZERS.register(bus);
   }

   private static <T extends SpartanCondition> void registerType(String fullId, Supplier<T> factory,
                                                                 Function<T, JsonObject> writer) {
      ResourceLocation loc = new ResourceLocation(fullId);
      String path = loc.getPath();
      Codec<ForgeCondition<T>> codec = ExtraCodecs.JSON.xmap(
            json -> new ForgeCondition<>(cast(factory.get().read(json.getAsJsonObject())), path),
            cond -> writer.apply(cond.delegate())
      );
      CODECS.put(path, codec);
      CONDITION_SERIALIZERS.register(path, () -> codec);
   }

   @SuppressWarnings("unchecked")
   private static <T extends SpartanCondition> T cast(SpartanCondition condition) {
      return (T) condition;
   }

   private record ForgeCondition<T extends SpartanCondition>(T delegate, String typePath) implements ICondition {
      @Override
      public Codec<? extends ICondition> codec() {
         return CODECS.get(this.typePath);
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