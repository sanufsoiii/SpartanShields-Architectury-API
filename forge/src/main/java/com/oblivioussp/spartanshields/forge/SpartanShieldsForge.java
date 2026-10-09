package com.oblivioussp.spartanshields.forge;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.client.ClientHelper;
import com.oblivioussp.spartanshields.client.ModKeyBinds;
import com.oblivioussp.spartanshields.client.model.DarkSteelTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.ElementiumTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.EnderiumShieldModel;
import com.oblivioussp.spartanshields.client.model.KiteShieldModel;
import com.oblivioussp.spartanshields.client.model.LumiumShieldModel;
import com.oblivioussp.spartanshields.client.model.ManasteelTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.MekanismTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.ShieldBaseModel;
import com.oblivioussp.spartanshields.client.model.TerrasteelTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.TowerShieldModel;
import com.oblivioussp.spartanshields.client.render.item.ClientRenderTransforms;
import com.oblivioussp.spartanshields.client.render.item.ModelLayers;
import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.event.ClientEventHandler;
import com.oblivioussp.spartanshields.event.CommonEventHandler;
import com.oblivioussp.spartanshields.forge.compat.ForgeConditionAdapters;
import com.oblivioussp.spartanshields.init.ModCreativeTabs;
import com.oblivioussp.spartanshields.init.ModEnchantments;
import com.oblivioussp.spartanshields.init.ModItems;
import com.oblivioussp.spartanshields.init.ModRecipes;
import com.oblivioussp.spartanshields.init.ModSounds;
import com.oblivioussp.spartanshields.init.ModStats;
import com.oblivioussp.spartanshields.network.NetworkHandler;
import com.oblivioussp.spartanshields.util.Log;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Forge entrypoint. All shared logic lives in :common; this class only wires the Architectury
 * registries into Forge's lifecycle and forwards the loader-specific extras (energy capability,
 * Forge condition adapters, client events).
 */
@Mod(SpartanShields.ID)
public class SpartanShieldsForge {
   public SpartanShieldsForge() {
      Log.info("Constructing Mod: " + SpartanShields.NAME);

      // Architectury's Forge registrar manager looks the mod event bus up in its own registry, which
      // has to be fed once per mod. Without it DeferredRegister.register() fails with
      // "Can't get event bus for mod ... because it was not registered!".
      EventBuses.registerModEventBus(SpartanShields.ID, FMLJavaModLoadingContext.get().getModEventBus());

      ModItems.register();
      ModSounds.register();
      ModEnchantments.register();
      ModRecipes.register();
      ModCreativeTabs.register();

      ForgeConditionAdapters.register();

      CommonEventHandler.register();

      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetup);
   }

   private void onSetup(FMLCommonSetupEvent event) {
      Log.info("Setting up " + SpartanShields.NAME);
      event.enqueueWork(() -> {
         NetworkHandler.init();
         ModStats.register();
         Config.INSTANCE.applyDurabilities();
      });
   }

   /**
    * Porting note: item colours, model layers and key bindings have to come from Forge's own client
    * events. Architectury's Forge implementations of the matching registries only forward to those
    * events, and all three fire before {@link FMLClientSetupEvent} - calling them from client setup
    * is either a no-op or, for key mappings, an outright
    * "Key mapping ... registered after event" failure (verified with a dev run).
    */
   @Mod.EventBusSubscriber(modid = SpartanShields.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
   public static final class ClientModEvents {
      @SubscribeEvent
      public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
         event.registerLayerDefinition(ModelLayers.BASE_SHIELD, ShieldBaseModel::createLayer);
         event.registerLayerDefinition(ModelLayers.KITE_SHIELD, KiteShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.TOWER_SHIELD, TowerShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.LUMIUM_SHIELD, LumiumShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.ENDERIUM_SHIELD, EnderiumShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.MANASTEEL_SHIELD, ManasteelTowerShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.TERRASTEEL_SHIELD, TerrasteelTowerShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.ELEMENTIUM_SHIELD, ElementiumTowerShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.MEKANISM_SHIELD, MekanismTowerShieldModel::createLayer);
         event.registerLayerDefinition(ModelLayers.DARK_STEEL_SHIELD, DarkSteelTowerShieldModel::createLayer);
      }

      @SubscribeEvent
      public static void registerItemColours(RegisterColorHandlersEvent.Item event) {
         for (var entry : ClientHelper.itemColours()) {
            event.register(entry.getKey(), entry.getValue());
         }
      }

      @SubscribeEvent
      public static void registerKeyMappings(net.minecraftforge.client.event.RegisterKeyMappingsEvent event) {
         event.register(ModKeyBinds.KEY_ALT_SHIELD_BASH);
      }

      @SubscribeEvent
      public static void onClientSetup(FMLClientSetupEvent event) {
         Log.info("Setting up client for " + SpartanShields.NAME);
         ClientHelper.registerShared();
         ClientEventHandler.register();
         ClientRenderTransforms.setApplier((model, displayContext, pose, leftHand) ->
               net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(pose, model, displayContext, leftHand));
      }




   }
}
