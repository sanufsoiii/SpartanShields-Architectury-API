package com.oblivioussp.spartanshields.compat.jei;

import com.oblivioussp.spartanshields.SpartanShields;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;

/**
 * No-op JEI plugin kept for parity with the Forge release (it registers nothing, it only tells
 * JEI that the mod exists). JEI is an optional dependency: the class is annotated with
 * {@code @JeiPlugin} and is only ever loaded by JEI's own plugin scanner.
 */
@JeiPlugin
public class SpartanShieldsPlugin implements IModPlugin {
   private static final Identifier PLUGIN_UID = Identifier.fromNamespaceAndPath(SpartanShields.ID, "jei_plugin");

   @Override
   public Identifier getPluginUid() {
      return PLUGIN_UID;
   }

   @Override
   public void registerCategories(IRecipeCategoryRegistration reg) {
   }

   @Override
   public void registerRecipes(IRecipeRegistration reg) {
   }

   @Override
   public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
   }
}
