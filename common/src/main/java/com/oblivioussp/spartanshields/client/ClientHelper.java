package com.oblivioussp.spartanshields.client;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.client.render.item.ModelLayers;
import com.oblivioussp.spartanshields.client.render.item.TowerShieldBEWLR;
import com.oblivioussp.spartanshields.init.ModItems;
import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import com.oblivioussp.spartanshields.util.Log;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Map;

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
   private static final ResourceLocation BLOCKING_PROPERTY = new ResourceLocation("blocking");
   private static final ResourceLocation DISABLED_PROPERTY = new ResourceLocation("disabled");

   /** Loader-agnostic registrations. Safe to call from any client setup phase. */
   public static void registerShared() {
      Log.info("Registering Spartan Shields client data");

      for (var entry : ModItems.REGISTER) {
         if (entry.get() instanceof ShieldBaseItem) {
            ItemPropertiesRegistry.register(entry.get(), BLOCKING_PROPERTY,
                  (stack, world, living, value) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);
         }

         if (entry.get() instanceof FEPoweredShieldItem) {
            ItemPropertiesRegistry.register(entry.get(), DISABLED_PROPERTY,
                  (stack, world, living, value) -> stack.getOrCreateTag().getInt(FEPoweredShieldItem.NBT_ENERGY) <= 0 ? 1.0F : 0.0F);
         }
      }

      Log.info("Spartan Shields client data registered");

   }

   /**
    * Colour handlers, model layers and key bindings through the Architectury registries. Used by the
    * Fabric entrypoint; on Forge these must come from Forge's own events (see
    * {@code forge/SpartanShieldsForge.ClientModEvents}) because Architectury's Forge implementations
    * of all three only forward to events that have already been fired by the time client setup runs.
    * <p>
    * Porting note: the Forge release also registered a {@code RegisterClientReloadListenersEvent}
    * listener to re-bake the tower shield models. Architectury's Forge implementation of
    * {@code ReloadListenerRegistry} only adds the listener while that event is firing (i.e. never, from
    * client setup) and on Fabric its {@code PackType.CLIENT_RESOURCES} overload stalls the resource
    * reload altogether, so re-baking is instead driven by
    * {@link com.oblivioussp.spartanshields.client.render.item.TowerShieldBEWLR}, which notices a
    * changed {@code ResourceManager} by itself.
    */
   public static void registerArchitecturyClient() {
      ModelLayers.register();
      ModKeyBinds.register();
      registerItemColours();
   }

   public static void registerItemColours() {
      for (var entry : itemColours()) {
         ColorHandlerRegistry.registerItemColors(entry.getKey(), entry.getValue());
      }
   }

   /**
    * The powered-shield colour overrides as (colour handler, items) pairs, so both loaders register
    * the exact same set from their own event/API.
    */
   public static List<Map.Entry<ItemColor, ItemLike[]>> itemColours() {
      return List.of(
            Map.entry(layerColour(7925891), new ItemLike[]{ModItems.BASIC_MEKANISTS_BASIC_SHIELD.get()}),
            Map.entry(layerColour(15759491), new ItemLike[]{ModItems.ADVANCED_MEKANISTS_BASIC_SHIELD.get()}),
            Map.entry(layerColour(7898096), new ItemLike[]{ModItems.ELITE_MEKANISTS_BASIC_SHIELD.get()}),
            Map.entry(layerColour(15762416), new ItemLike[]{ModItems.ULTIMATE_MEKANISTS_BASIC_SHIELD.get()}),
            Map.entry(layerColour(8454048), new ItemLike[]{ModItems.DARK_STEEL_RIOT_BASIC_SHIELD.get()}));
   }

   private static ItemColor layerColour(int layerOneColour) {
      return (stack, layer) -> layer == 1 ? layerOneColour : 16777215;
   }

   private ClientHelper() {
   }
}
