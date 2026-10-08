package com.oblivioussp.spartanshields.crafting.condition;

import com.google.gson.JsonObject;
import com.oblivioussp.spartanshields.SpartanShields;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * `spartanshields:tag_empty` - true when the referenced item tag resolves to no items.
 * Replaces Forge's {@code forge:tag_empty}, which queried Forge's {@code ITagManager}.
 * <p>
 * Vanilla exposes no public tag registry query on 1.20.1, so the check goes through
 * {@code Ingredient#test} against a probe item: a tag that does not exist, is empty, or contains
 * no obtainable item fails the match. Uninstalled provider mods contribute nothing to the tag,
 * which is exactly the semantics the Forge version had.
 */
public class TagEmptyCondition implements SpartanCondition {
   public static final String ID = SpartanShields.ID + ":tag_empty";

   private ResourceLocation tagLocation;

   public TagEmptyCondition() {
   }

   public TagEmptyCondition(ResourceLocation tagLocation) {
      this.tagLocation = tagLocation;
   }

   @Override
   public JsonObject write() {
      JsonObject json = new JsonObject();
      json.addProperty("type", ID);
      json.addProperty("tag", this.tagLocation == null ? "" : this.tagLocation.toString());
      return json;
   }

   @Override
   public TagEmptyCondition read(JsonObject json) {
      this.tagLocation = ResourceLocation.tryParse(json.get("tag").getAsString());
      return this;
   }

   @Override
   public boolean test() {
      if (this.tagLocation == null) {
         return true;
      }

      TagKey<Item> key = TagKey.create(Registries.ITEM, this.tagLocation);
      Ingredient ingredient = Ingredient.of(key);
      return ingredient.getItems().length == 0;
   }
}
