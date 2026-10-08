package com.oblivioussp.spartanshields.neoforge;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.client.ClientHelper;
import com.oblivioussp.spartanshields.client.ModKeyBinds;
import com.oblivioussp.spartanshields.client.model.*;
import com.oblivioussp.spartanshields.client.render.item.ModelLayers;
import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.event.ClientEventHandler;
import com.oblivioussp.spartanshields.event.CommonEventHandler;
import com.oblivioussp.spartanshields.init.*;
import com.oblivioussp.spartanshields.item.IItemPoweredFE;
import com.oblivioussp.spartanshields.network.NetworkHandler;
import com.oblivioussp.spartanshields.util.Log;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.energy.EnergyStorage;

@Mod(SpartanShields.ID)
public class SpartanShieldsNeoForge {
   public SpartanShieldsNeoForge(IEventBus modBus) {
      Log.info("Constructing Mod: " + SpartanShields.NAME + " (NeoForge)");

      ModItems.register();
      ModSounds.register();
      ModRecipes.register();
      ModCreativeTabs.register();

      CommonEventHandler.register();

      modBus.addListener(this::onSetup);
      modBus.addListener(this::registerCapabilities);
   }

   private void onSetup(FMLCommonSetupEvent event) {
      Log.info("Setting up " + SpartanShields.NAME);
      event.enqueueWork(() -> {
         NetworkHandler.init();
         ModStats.register();
         Config.INSTANCE.applyDurabilities();
      });
   }

   private void registerCapabilities(RegisterCapabilitiesEvent event) {
      for (var supplier : ModItems.REGISTER) {
         Item item = supplier.get();
         if (item instanceof IItemPoweredFE feItem) {
            event.registerItem(
                  Capabilities.EnergyStorage.ITEM,
                  (stack, ctx) -> new EnergyStorage(feItem.getFECapacity(stack)) {
                     @Override
                     public int receiveEnergy(int maxReceive, boolean simulate) {
                        return feItem.receiveFE(stack, maxReceive, simulate);
                     }

                     @Override
                     public int extractEnergy(int maxExtract, boolean simulate) {
                        return feItem.extractFE(stack, maxExtract, simulate);
                     }

                     @Override
                     public int getEnergyStored() {
                        return feItem.getFEStored(stack);
                     }

                     @Override
                     public int getMaxEnergyStored() {
                        return feItem.getFECapacity(stack);
                     }

                     @Override
                     public boolean canExtract() {
                        return feItem.canExtractFE(stack);
                     }

                     @Override
                     public boolean canReceive() {
                        return feItem.canReceiveFE(stack);
                     }
                  },
                  item
            );
         }
      }
   }

   @EventBusSubscriber(modid = SpartanShields.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
      public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
         event.register(ModKeyBinds.KEY_ALT_SHIELD_BASH);
      }

      @SubscribeEvent
      public static void onClientSetup(FMLClientSetupEvent event) {
         Log.info("Setting up client for " + SpartanShields.NAME);
         ClientHelper.registerShared();
         ClientEventHandler.register();
      }
   }
}
