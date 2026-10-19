package com.oblivioussp.spartanshields.neoforge;

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
import com.oblivioussp.spartanshields.init.ModCreativeTabs;
import com.oblivioussp.spartanshields.init.ModEnchantments;
import com.oblivioussp.spartanshields.init.ModItems;
import com.oblivioussp.spartanshields.init.ModRecipes;
import com.oblivioussp.spartanshields.init.ModSounds;
import com.oblivioussp.spartanshields.init.ModStats;
import com.oblivioussp.spartanshields.network.NetworkHandler;
import com.oblivioussp.spartanshields.util.Log;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * NeoForge entrypoint (MC 1.20.2, NeoForge 20.2.x line). All shared logic lives in :common; this
 * class only wires the Architectury registries into NeoForge's lifecycle and forwards the
 * client-side registrations. Recipe conditions are handled by {@code mixin.MixinRecipeManager}
 * (NeoForge does not parse foreign {@code conditions} blocks), and the FE charge lives in the
 * stack NBT behind {@code IItemPoweredFE} - see PORTING.md for both details.
 */
@Mod(SpartanShields.ID)
public class SpartanShieldsNeoForge {
   public SpartanShieldsNeoForge(IEventBus modBus) {
      Log.info("Constructing Mod: " + SpartanShields.NAME + " (NeoForge)");

      ModItems.register();
      ModSounds.register();
      ModEnchantments.register();
      ModRecipes.register();
      ModCreativeTabs.register();

      CommonEventHandler.register();

      // See the porting notes: NeoForge freezes vanilla registries before mod CONSTRUCT (FML
      // 1.0.16/2.0.17, unlike Forge where enqueueWork still ran pre-freeze), so the custom stat
      // goes through RegisterEvent and network init happens at construct (architectury defers
      // payload registration itself, same as the Fabric entrypoint).
      NetworkHandler.init();
      modBus.addListener(this::onSetup);
      modBus.addListener(this::onRegister);
   }

   private void onRegister(RegisterEvent event) {
      if (event.getRegistryKey().equals(net.minecraft.core.registries.Registries.CUSTOM_STAT)) {
         event.register(net.minecraft.core.registries.Registries.CUSTOM_STAT, helper ->
               helper.register(ModStats.SHIELD_BASH_HITS, ModStats.SHIELD_BASH_HITS));
      }
   }

   private void onSetup(FMLCommonSetupEvent event) {
      Log.info("Setting up " + SpartanShields.NAME);
      event.enqueueWork(() -> {
         net.minecraft.stats.Stats.CUSTOM.get(ModStats.SHIELD_BASH_HITS);
         Config.INSTANCE.applyDurabilities();
      });
   }

   /**
    * Item colours, model layers and key bindings have to come from NeoForge's own client events.
    * Architectury's NeoForge implementations of the matching registries only forward to those
    * events, and all three fire before {@link FMLClientSetupEvent} - calling them from client
    * setup is either a no-op or, for key mappings, an outright "Key mapping ... registered after
    * event" failure (semantics identical to the Forge side, verified with dev runs there).
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
      public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
         event.register(ModKeyBinds.KEY_ALT_SHIELD_BASH);
      }

      @SubscribeEvent
      public static void onClientSetup(FMLClientSetupEvent event) {
         Log.info("Setting up client for " + SpartanShields.NAME);
         ClientHelper.registerShared();
         ClientEventHandler.register();
         ClientRenderTransforms.setApplier((model, displayContext, pose, leftHand) ->
               net.neoforged.neoforge.client.ClientHooks.handleCameraTransforms(pose, model, displayContext, leftHand));
      }
   }
}
