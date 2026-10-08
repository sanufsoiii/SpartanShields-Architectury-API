package com.oblivioussp.spartanshields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oblivioussp.spartanshields.crafting.condition.SpartanConditions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeValidationTest {

   private static final File DATA_DIR = new File("src/main/resources/data/spartanshields");
   private static final File RECIPES_DIR = new File(DATA_DIR, "recipes");
   private static final File TAGS_DIR = new File(DATA_DIR, "tags/items");

   @Test
   public void testAllRecipesExistAndParse() throws Exception {
      File[] recipeFiles = RECIPES_DIR.listFiles((dir, name) -> name.endsWith(".json"));
      assertNotNull(recipeFiles, "Recipe directory must exist");
      assertEquals(93, recipeFiles.length, "All 93 mod recipes must be present");

      Set<String> referencedTags = new HashSet<>();
      List<String> invalidVanillaTags = new ArrayList<>();

      for (File file : recipeFiles) {
         try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            assertNotNull(json.get("type"), "Recipe " + file.getName() + " must have a type");

            // Collect referenced tags
            collectTags(json, referencedTags);
         }
      }

      for (String tag : referencedTags) {
         if (tag.startsWith("minecraft:ingots/") || tag.startsWith("minecraft:gems/")) {
            invalidVanillaTags.add(tag);
         }
      }

      assertTrue(invalidVanillaTags.isEmpty(),
            "Found invalid non-existent vanilla tags in recipes: " + invalidVanillaTags);
   }

   @Test
   public void testAllReferencedTagsHaveValidDefinitions() throws Exception {
      File[] recipeFiles = RECIPES_DIR.listFiles((dir, name) -> name.endsWith(".json"));
      assertNotNull(recipeFiles);

      Set<String> referencedTags = new HashSet<>();
      for (File file : recipeFiles) {
         try (FileReader reader = new FileReader(file)) {
            collectTags(JsonParser.parseReader(reader).getAsJsonObject(), referencedTags);
         }
      }

      for (String tag : referencedTags) {
         if (tag.startsWith("spartanshields:")) {
            String relativePath = tag.substring("spartanshields:".length()) + ".json";
            File tagFile = new File(TAGS_DIR, relativePath);
            assertTrue(tagFile.exists(), "Referenced tag file must exist: " + tagFile.getPath() + " for tag " + tag);
         }
      }
   }

   @Test
   public void testTagsDoNotHaveUnmarkedForeignDependencies() throws Exception {
      List<File> allTagFiles = new ArrayList<>();
      findJsonFiles(TAGS_DIR, allTagFiles);
      assertFalse(allTagFiles.isEmpty(), "Tags directory must contain tag definitions");

      List<String> errors = new ArrayList<>();

      for (File file : allTagFiles) {
         try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray values = json.getAsJsonArray("values");
            assertNotNull(values, "Tag file " + file.getName() + " must have values array");

            for (JsonElement elem : values) {
               if (elem.isJsonPrimitive()) {
                  String val = elem.getAsString();
                  // Vanilla items/tags and Spartan Shields's own items/tags are always present
                  if (!val.startsWith("minecraft:") && !val.startsWith("spartanshields:") &&
                        !val.startsWith("#spartanshields:") && !val.equals("#minecraft:planks")) {
                     errors.add("Tag " + file.getName() + " has raw unoptional foreign dependency: " + val);
                  }
               } else if (elem.isJsonObject()) {
                  JsonObject obj = elem.getAsJsonObject();
                  assertTrue(obj.has("id"), "Object entry in tag must have 'id': " + file.getName());
                  assertTrue(obj.has("required") && !obj.get("required").getAsBoolean(),
                        "Optional entry in tag must have required: false: " + obj + " in " + file.getName());
               }
            }
         }
      }

      assertTrue(errors.isEmpty(), "Found raw unoptional foreign dependencies in tags: " + errors);
   }

   @Test
   public void testVanillaMaterialsPassRecipeConditions() throws Exception {
      String[] vanillaShields = {
            "wooden_basic_shield.json", "wooden_tower_shield.json",
            "stone_basic_shield.json", "stone_tower_shield.json",
            "copper_basic_shield.json", "copper_tower_shield.json",
            "iron_basic_shield.json", "iron_tower_shield.json",
            "golden_basic_shield.json", "golden_tower_shield.json",
            "diamond_basic_shield.json", "diamond_tower_shield.json",
            "netherite_basic_shield_smithing.json", "netherite_tower_shield_smithing.json",
            "obsidian_basic_shield.json", "obsidian_tower_shield.json"
      };

      for (String name : vanillaShields) {
         File file = new File(RECIPES_DIR, name);
         assertTrue(file.exists(), "Recipe file must exist: " + name);
         try (FileReader reader = new FileReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            boolean passed = SpartanConditions.testRecipe(json);
            assertTrue(passed, "Vanilla shield recipe " + name + " must pass default conditions");
         }
      }
   }

   private void collectTags(JsonElement element, Set<String> tags) {
      if (element.isJsonObject()) {
         JsonObject obj = element.getAsJsonObject();
         if (obj.has("tag") && obj.get("tag").isJsonPrimitive()) {
            tags.add(obj.get("tag").getAsString());
         }
         for (String key : obj.keySet()) {
            collectTags(obj.get(key), tags);
         }
      } else if (element.isJsonArray()) {
         for (JsonElement item : element.getAsJsonArray()) {
            collectTags(item, tags);
         }
      }
   }

   private void findJsonFiles(File dir, List<File> list) {
      File[] files = dir.listFiles();
      if (files == null) return;
      for (File f : files) {
         if (f.isDirectory()) {
            findJsonFiles(f, list);
         } else if (f.getName().endsWith(".json")) {
            list.add(f);
         }
      }
   }
}
