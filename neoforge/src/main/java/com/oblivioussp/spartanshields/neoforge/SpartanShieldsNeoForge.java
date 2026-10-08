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

import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

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
         if (item instanceof FEPoweredShieldItem feShield) {
            event.registerItem(
                  Capabilities.Energy.ITEM,
                  (stack, ctx) -> {
                     var access = ctx != null ? ctx : ItemAccess.forStack(stack);
                     int maxReceive = feShield.canReceiveFE(stack) ? feShield.getMaxEnergyReceive() : 0;
                     int maxExtract = 0;
                     return new ItemAccessEnergyHandler(access, null, feShield.getFECapacity(stack), maxReceive, maxExtract) {
                        @Override
                        protected int getAmountFrom(ItemResource accessResource) {
                           if (!accessResource.is(validItem)) {
                              return 0;
                           }
                           ItemStack itemStack = accessResource.toStack();
                           return feShield.getFEStored(itemStack);
                        }

                        @Override
                        protected ItemResource update(ItemResource accessResource, int newAmount) {
                           ItemStack itemStack = accessResource.toStack();
                           CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.putInt(FEPoweredShieldItem.NBT_ENERGY, newAmount));
                           return ItemResource.of(itemStack);
                        }
                     };
                  },
                  item
            );
         }
      }
   }

   @EventBusSubscriber(modid = SpartanShields.ID, value = Dist.CLIENT)
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
