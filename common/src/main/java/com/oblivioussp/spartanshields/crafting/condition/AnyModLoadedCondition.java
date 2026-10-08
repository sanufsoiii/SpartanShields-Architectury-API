package com.oblivioussp.spartanshields.crafting.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oblivioussp.spartanshields.SpartanShields;
import dev.architectury.platform.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * `spartanshields:any_mod_loaded` - true when at least one of the listed mods is loaded.
 * <p>
 * Produced by tools/migrate_data.py when translating {@code forge:not + forge:tag_empty}: a
 * material recipe was previously available as soon as *any* mod put an item into Forge's common
 * ingot tag, which is the same question as "is at least one of these provider mods installed".
 */
public class AnyModLoadedCondition implements SpartanCondition {
   public static final String ID = SpartanShields.ID + ":any_mod_loaded";

   private final List<String> modids = new ArrayList<>();

   public AnyModLoadedCondition() {
   }

   public AnyModLoadedCondition(List<String> modids) {
      this.modids.addAll(modids);
   }

   @Override
   public JsonObject write() {
      JsonObject json = new JsonObject();
      json.addProperty("type", ID);
      JsonArray array = new JsonArray();
      for (String modid : this.modids) {
         array.add(modid);
      }
      json.add("modids", array);
      return json;
   }

   @Override
   public AnyModLoadedCondition read(JsonObject json) {
      this.modids.clear();
      if (json.has("modids") && json.get("modids").isJsonArray()) {
         for (var element : json.getAsJsonArray("modids")) {
            this.modids.add(element.getAsString());
         }
      }
      return this;
   }

   @Override
   public boolean test() {
      for (String modid : this.modids) {
         if (Platform.isModLoaded(modid)) {
            return true;
         }
      }

      return false;
   }
}
