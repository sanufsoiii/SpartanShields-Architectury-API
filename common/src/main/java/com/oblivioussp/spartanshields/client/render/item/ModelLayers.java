package com.oblivioussp.spartanshields.client.render.item;

import com.oblivioussp.spartanshields.SpartanShields;
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
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Porting note: the Forge release declared {@code ModelLayerLocation} constants and registered
 * {@code LayerDefinition} factories through {@code EntityRenderersEvent.RegisterLayerDefinitions}.
 * Architectury's {@link EntityModelLayerRegistry} takes the same two ingredients, so the
 * registration is unchanged apart from the API.
 */
public class ModelLayers {
   public static final ModelLayerLocation BASE_SHIELD = createMainModelLayer("base_shield");
   public static final ModelLayerLocation KITE_SHIELD = createMainModelLayer("kite_shield");
   public static final ModelLayerLocation TOWER_SHIELD = createMainModelLayer("tower_shield");
   public static final ModelLayerLocation LUMIUM_SHIELD = createMainModelLayer("lumium_shield");
   public static final ModelLayerLocation ENDERIUM_SHIELD = createMainModelLayer("enderium_shield");
   public static final ModelLayerLocation MANASTEEL_SHIELD = createMainModelLayer("manasteel_shield");
   public static final ModelLayerLocation TERRASTEEL_SHIELD = createMainModelLayer("terrasteel_shield");
   public static final ModelLayerLocation ELEMENTIUM_SHIELD = createMainModelLayer("elementium_shield");
   public static final ModelLayerLocation MEKANISM_SHIELD = createMainModelLayer("mekanism_shield");
   public static final ModelLayerLocation DARK_STEEL_SHIELD = createMainModelLayer("dark_steel_shield");

   public static ModelLayerLocation createMainModelLayer(String location) {
      return new ModelLayerLocation(new ResourceLocation(SpartanShields.ID, location), "main");
   }

   public static void register() {
      EntityModelLayerRegistry.register(BASE_SHIELD, ShieldBaseModel::createLayer);
      EntityModelLayerRegistry.register(KITE_SHIELD, KiteShieldModel::createLayer);
      EntityModelLayerRegistry.register(TOWER_SHIELD, TowerShieldModel::createLayer);
      EntityModelLayerRegistry.register(LUMIUM_SHIELD, LumiumShieldModel::createLayer);
      EntityModelLayerRegistry.register(ENDERIUM_SHIELD, EnderiumShieldModel::createLayer);
      EntityModelLayerRegistry.register(MANASTEEL_SHIELD, ManasteelTowerShieldModel::createLayer);
      EntityModelLayerRegistry.register(TERRASTEEL_SHIELD, TerrasteelTowerShieldModel::createLayer);
      EntityModelLayerRegistry.register(ELEMENTIUM_SHIELD, ElementiumTowerShieldModel::createLayer);
      EntityModelLayerRegistry.register(MEKANISM_SHIELD, MekanismTowerShieldModel::createLayer);
      EntityModelLayerRegistry.register(DARK_STEEL_SHIELD, DarkSteelTowerShieldModel::createLayer);
   }

   private ModelLayers() {
   }
}
