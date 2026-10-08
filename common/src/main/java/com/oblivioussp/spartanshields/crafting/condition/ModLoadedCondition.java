package com.oblivioussp.spartanshields.crafting.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oblivioussp.spartanshields.SpartanShields;
import dev.architectury.platform.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * `spartanshields:mod_loaded` - true when the named mod is loaded.
 * Replaces Forge's {@code forge:mod_loaded}.
 */
public class ModLoadedCondition implements SpartanCondition {
   public static final String ID = SpartanShields.ID + ":mod_loaded";

   private String modid = "";

   public ModLoadedCondition() {
   }

   public ModLoadedCondition(String modid) {
      this.modid = modid;
   }

   @Override
   public JsonObject write() {
      JsonObject json = new JsonObject();
      json.addProperty("type", ID);
      json.addProperty("modid", this.modid);
      return json;
   }

   @Override
   public ModLoadedCondition read(JsonObject json) {
      this.modid = json.has("modid") ? json.get("modid").getAsString()
            : (json.has("mod") ? json.get("mod").getAsString() : "");
      return this;
   }

   @Override
   public boolean test() {
      return Platform.isModLoaded(this.modid);
   }
}
