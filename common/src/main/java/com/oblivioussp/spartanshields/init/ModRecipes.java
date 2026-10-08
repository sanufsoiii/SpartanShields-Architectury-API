package com.oblivioussp.spartanshields.init;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.crafting.condition.SpartanConditions;
import com.oblivioussp.spartanshields.item.crafting.PoweredShieldUpgradeRecipe;
import com.oblivioussp.spartanshields.item.crafting.ShieldBannerRecipe;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {
   public static final DeferredRegister<RecipeSerializer<?>> REGISTER =
         DeferredRegister.create(SpartanShields.ID, Registries.RECIPE_SERIALIZER);

   public static final RegistrySupplier<ShieldBannerRecipe.Serializer> SHIELD_BANNER =
         REGISTER.register("apply_banner", ShieldBannerRecipe.Serializer::new);
   public static final RegistrySupplier<PoweredShieldUpgradeRecipe.Serializer> POWERED_SHIELD_UPGRADE =
         REGISTER.register("upgrade_powered_shield", PoweredShieldUpgradeRecipe.Serializer::new);

   public static void register() {
      REGISTER.register();
      SpartanConditions.bootstrap();
   }

   private ModRecipes() {
   }
}
