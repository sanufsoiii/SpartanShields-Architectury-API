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
import com.oblivioussp.spartanshields.item.IItemPoweredFE;
import com.oblivioussp.spartanshields.network.NetworkHandler;
import com.oblivioussp.spartanshields.util.Log;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.energy.EnergyStorage;

/**
 * NeoForge entrypoint (MC 1.20.4, NeoForge 20.4.x line). All shared logic lives in :common; this
 * class only wires the Architectury registries into NeoForge's lifecycle, bridges the FE charge of
 * the powered shields into NeoForge's per-item energy capability and forwards the client-side
 * registrations. Recipe conditions are handled by {@code mixin.MixinRecipeManager} (NeoForge does
 * not parse foreign {@code conditions} blocks)
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

      //  NeoForge freezes vanilla registries before mod CONSTRUCT, so the
      // custom stat goes through RegisterEvent and network init happens at construct (architectury
      // defers payload registration itself, same as the Fabric entrypoint); only applyDurabilities()
      // (which resolves registered items) and the eager Stat instance stay in the setup queue.
      NetworkHandler.init();

      modBus.addListener(this::onSetup);
      modBus.addListener(this::onRegister);
      modBus.addListener(this::registerCapabilities);
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
    * Mekanism/EnderIO/Thermal pipes charge powered shields through NeoForge's energy capability,
    * exactly like on the Forge side; the charge itself stays in the stack NBT behind
    * {@code IItemPoweredFE}.
    */
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

   /**
    * Item colours, model layers and key bindings have to come from NeoForge's own client events -
    * the timing semantics are identical to the Forge side (verified with dev runs there).
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
