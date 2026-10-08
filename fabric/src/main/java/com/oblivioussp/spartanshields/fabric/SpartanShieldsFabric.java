package com.oblivioussp.spartanshields.fabric;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.event.CommonEventHandler;
import com.oblivioussp.spartanshields.init.ModCreativeTabs;
import com.oblivioussp.spartanshields.init.ModEnchantments;
import com.oblivioussp.spartanshields.init.ModItems;
import com.oblivioussp.spartanshields.init.ModRecipes;
import com.oblivioussp.spartanshields.init.ModSounds;
import com.oblivioussp.spartanshields.init.ModStats;
import com.oblivioussp.spartanshields.network.NetworkHandler;
import com.oblivioussp.spartanshields.util.Log;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric entrypoint. Mirrors the Forge one; everything below the platform boundary is shared code
 * from :common.
 */
public class SpartanShieldsFabric implements ModInitializer {
   @Override
   public void onInitialize() {
      Log.info("Constructing Mod: " + SpartanShields.NAME);

      ModItems.register();
      ModSounds.register();
      ModEnchantments.register();
      ModRecipes.register();
      ModCreativeTabs.register();

      NetworkHandler.init();
      ModStats.register();
      Config.INSTANCE.applyDurabilities();

      CommonEventHandler.register();
   }
}
