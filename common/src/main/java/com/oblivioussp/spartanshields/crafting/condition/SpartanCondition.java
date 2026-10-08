package com.oblivioussp.spartanshields.crafting.condition;

import com.google.gson.JsonObject;

/**
 * A recipe condition understood by this mod on every loader.
 * <p>
 * Porting note: the Forge release implemented Forge's {@code ICondition}, which Fabric cannot
 * parse at all. These conditions are evaluated by {@code mixin.MixinRecipeManager} while the data
 * pack is being loaded (identical behaviour on both loaders) and, on Forge, are additionally
 * wrapped as real {@code ICondition}s by {@code forge/compat/ForgeConditionAdapters} so Forge's
 * own CraftingHelper agrees with them.
 */
public interface SpartanCondition {
   JsonObject write();

   SpartanCondition read(JsonObject json);

   boolean test();

   static SpartanCondition fromJson(JsonObject json) {
      if (json == null || !json.has("type")) {
         return null;
      }
      String type = json.get("type").getAsString();
 SpartanCondition condition = SpartanConditions.byId(type);
      return condition == null ? null : condition.read(json);
   }
}
