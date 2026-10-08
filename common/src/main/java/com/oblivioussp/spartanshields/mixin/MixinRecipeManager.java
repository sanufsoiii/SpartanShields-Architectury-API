package com.oblivioussp.spartanshields.mixin;

import com.google.gson.JsonElement;
import com.oblivioussp.spartanshields.crafting.condition.SpartanConditions;
import com.oblivioussp.spartanshields.util.Log;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

/**
 * Drops recipes whose {@code conditions} array fails, using this mod's own condition types.
 * <p>
 * Porting note: Forge processed recipe conditions inside {@code CraftingHelper}, which is
 * reachable from Forge's patched recipe serializers only - Fabric has no equivalent, so vanilla
 * simply ignores an unknown {@code conditions} block (and every recipe would stay craftable). This
 * mixin runs at the single point both loaders share, {@code RecipeManager#apply}, i.e. while the
 * data pack is parsed. On Forge the same conditions are additionally registered as real
 * {@code ICondition}s, so both layers agree.
 */
@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager {
   @Inject(method = "apply", at = @At("HEAD"))
   private void spartanshields$filterByConditions(Map<ResourceLocation, JsonElement> recipes, ResourceManager resourceManager,
                                                  ProfilerFiller profiler, CallbackInfo callback) {
      if (recipes.isEmpty()) {
         return;
      }

      Map<ResourceLocation, JsonElement> filtered = new Object2ObjectOpenHashMap<>(recipes);
      Iterator<Map.Entry<ResourceLocation, JsonElement>> iterator = filtered.entrySet().iterator();
      int removed = 0;

      while (iterator.hasNext()) {
         Map.Entry<ResourceLocation, JsonElement> entry = iterator.next();
         if (!entry.getValue().isJsonObject()) {
            continue;
         }

         if (!SpartanConditions.testRecipe(entry.getValue().getAsJsonObject())) {
            iterator.remove();
            removed++;
         }
      }

      if (removed > 0) {
         Log.debug("Disabled " + removed + " recipe(s) by config/missing materials");
      }

      recipes.clear();
      recipes.putAll(filtered);
   }
}
