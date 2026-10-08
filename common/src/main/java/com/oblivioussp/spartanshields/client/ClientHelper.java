package com.oblivioussp.spartanshields.client;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.client.render.item.ModelLayers;
import com.oblivioussp.spartanshields.util.Log;

/**
 * Client-only registrations.
 * <p>
 * Porting note: the Forge release registered everything from Forge client events
 * ({@code RegisterColorHandlersEvent.Item}, {@code EntityRenderersEvent.RegisterLayerDefinitions},
 * {@code RegisterClientReloadListenersEvent}, {@code RegisterKeyMappingsEvent}, {@code ItemProperties}).
 * Only some of those have an Architectury equivalent that is safe to call late: item colours, model
 * layers and key mappings all go through Forge events that fire *before* {@code FMLClientSetupEvent},
 * so on Forge they are registered by {@code forge/SpartanShieldsForge.ClientModEvents} from the mod
 * event bus instead. What is left here is the part that is timing-independent on both loaders:
 * <ul>
 *   <li>{@link #registerShared()} - item model properties and the BEWLR reload listener</li>
 *   <li>{@link #registerArchitecturyClient()} - colours, model layers and key bindings through the
 *       Architectury registries, used by the Fabric entrypoint</li>
 * </ul>
 * Item model properties are also registered in one place instead of from the item constructors
 * (which is what the {@code FMLEnvironment.dist.isClient()} guards in the Forge release did).
 */
public class ClientHelper {
   /** Loader-agnostic registrations. Safe to call from any client setup phase. */
   public static void registerShared() {
      Log.info("Registering Spartan Shields client data");
      Log.info("Spartan Shields client data registered");
   }

   public static void registerArchitecturyClient() {
      ModelLayers.register();
      ModKeyBinds.register();
   }

   private ClientHelper() {
   }
}
