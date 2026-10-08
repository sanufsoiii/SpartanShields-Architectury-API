package com.oblivioussp.spartanshields.fabric;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.client.ClientHelper;
import com.oblivioussp.spartanshields.event.ClientEventHandler;
import com.oblivioussp.spartanshields.util.Log;
import net.fabricmc.api.ClientModInitializer;

public class SpartanShieldsFabricClient implements ClientModInitializer {
   @Override
   public void onInitializeClient() {
      Log.info("Setting up client for " + SpartanShields.NAME);
      ClientHelper.registerShared();
      ClientHelper.registerArchitecturyClient();
      ClientEventHandler.register();
   }
}
