package com.oblivioussp.spartanshields.crafting.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oblivioussp.spartanshields.SpartanShields;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Recipe condition driven by the config: `{"type": "spartanshields:type_disabled",
 * "disabled": ["modded", "tin"]}` - the recipe is dropped when any of the listed types is
 * disabled in the config.
 */
public class TypeDisabledCondition implements SpartanCondition {
   public static final String ID = SpartanShields.ID + ":type_disabled";

   public static final String MODDED = "modded";
   public static final String OBSIDIAN = "obsidian";
   public static final String COPPER = "copper";
   public static final String TIN = "tin";
   public static final String BRONZE = "bronze";
   public static final String STEEL = "steel";
   public static final String SILVER = "silver";
   public static final String ELECTRUM = "electrum";
   public static final String LEAD = "lead";
   public static final String NICKEL = "nickel";
   public static final String INVAR = "invar";
   public static final String CONSTANTAN = "constantan";
   public static final String PLATINUM = "platinum";
   public static final String ALUMINUM = "aluminum";
   public static final String SIGNALUM = "signalum";
   public static final String LUMIUM = "lumium";
   public static final String ENDERIUM = "enderium";

   private static final Set<String> DISABLED_RECIPE_TYPES = new HashSet<>();

   private List<String> types;

   public TypeDisabledCondition() {
   }

   public TypeDisabledCondition(List<String> types) {
      this.types = new ArrayList<>(types);
   }

   @Override
   public JsonObject write() {
      JsonObject json = new JsonObject();
      json.addProperty("type", ID);
      JsonArray array = new JsonArray();
      for (String type : this.types) {
         array.add(type);
      }
      json.add("disabled", array);
      return json;
   }

   @Override
   public TypeDisabledCondition read(JsonObject json) {
      List<String> typeList = new ArrayList<>();
      if (json.has("disabled") && json.get("disabled").isJsonArray()) {
         for (var element : json.getAsJsonArray("disabled")) {
            typeList.add(element.getAsString());
         }
      }
      this.types = typeList;
      return this;
   }

   @Override
   public boolean test() {
      for (String type : this.types) {
         if (DISABLED_RECIPE_TYPES.contains(type)) {
            return false;
         }
      }

      return true;
   }

   public static void clear() {
      DISABLED_RECIPE_TYPES.clear();
   }

   public static void updateType(String type, boolean disabled) {
      if (disabled) {
         DISABLED_RECIPE_TYPES.add(type);
      } else {
         DISABLED_RECIPE_TYPES.remove(type);
      }
   }
}
