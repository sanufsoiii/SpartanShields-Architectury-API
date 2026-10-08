package com.oblivioussp.spartanshields.crafting.condition;

import com.google.gson.JsonObject;
import com.oblivioussp.spartanshields.SpartanShields;

/** `spartanshields:not` - negation of a nested condition. Replaces Forge's {@code forge:not}. */
public class NotCondition implements SpartanCondition {
   public static final String ID = SpartanShields.ID + ":not";

   private SpartanCondition inner;

   public NotCondition() {
   }

   public NotCondition(SpartanCondition inner) {
      this.inner = inner;
   }

   public SpartanCondition getInner() {
      return this.inner;
   }

   @Override
   public JsonObject write() {
      JsonObject json = new JsonObject();
      json.addProperty("type", ID);
      json.add("value", this.inner == null ? new JsonObject() : this.inner.write());
      return json;
   }

   @Override
   public NotCondition read(JsonObject json) {
      if (json.has("value") && json.get("value").isJsonObject()) {
         this.inner = SpartanCondition.fromJson(json.getAsJsonObject("value"));
      }
      return this;
   }

   @Override
   public boolean test() {
      return this.inner == null || !this.inner.test();
   }
}
