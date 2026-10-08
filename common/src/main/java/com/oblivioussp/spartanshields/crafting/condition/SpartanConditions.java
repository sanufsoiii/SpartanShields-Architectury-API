package com.oblivioussp.spartanshields.crafting.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oblivioussp.spartanshields.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Registry of this mod's own recipe conditions (loader-agnostic replacement for ICondition). */
public final class SpartanConditions {
   private static final Map<String, Supplier<? extends SpartanCondition>> REGISTRY = new LinkedHashMap<>();

   static {
      REGISTRY.put(TypeDisabledCondition.ID, TypeDisabledCondition::new);
      REGISTRY.put(ModLoadedCondition.ID, ModLoadedCondition::new);
      REGISTRY.put(AnyModLoadedCondition.ID, AnyModLoadedCondition::new);
      REGISTRY.put(TagEmptyCondition.ID, TagEmptyCondition::new);
      REGISTRY.put(NotCondition.ID, NotCondition::new);
   }

   public static SpartanCondition byId(String id) {
      Supplier<? extends SpartanCondition> factory = REGISTRY.get(id);
      return factory == null ? null : factory.get();
   }

   /**
    * Evaluates the {@code conditions} array of a recipe json. Unknown condition types are reported
    * and treated as "pass" so a typo can never silently drop every recipe.
    */
   public static boolean testRecipe(JsonObject recipeJson) {
      if (recipeJson == null || !recipeJson.has("conditions") || !recipeJson.get("conditions").isJsonArray()) {
         return true;
      }

      JsonArray conditions = recipeJson.getAsJsonArray("conditions");
      for (JsonElement element : conditions) {
         if (!element.isJsonObject()) {
            continue;
         }

         SpartanCondition condition = SpartanCondition.fromJson(element.getAsJsonObject());
         if (condition == null) {
            String type = element.getAsJsonObject().has("type") ? element.getAsJsonObject().get("type").getAsString() : "?";
            Log.warn("Unknown recipe condition type: " + type);
            continue;
         }

         if (!condition.test()) {
            return false;
         }
      }

      return true;
   }

   public static void bootstrap() {
      // static registration happens in the static block; kept for symmetry with ModRecipes
   }

   private SpartanConditions() {
   }
}
