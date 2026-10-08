package com.oblivioussp.spartanshields.client.render.item;

import com.oblivioussp.spartanshields.item.FEPoweredShieldItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class DarkSteelTowerShieldRenderInfo extends TowerShieldRenderInfo {
   protected final float r;
   protected final float g;
   protected final float b;
   protected static final RenderType LIGHTS_ON = RenderType.eyes(
         ResourceLocation.fromNamespaceAndPath("spartanshields", "textures/entity/enderio/dark_steel_tower_shield_lights_on.png"));
   protected static final RenderType LIGHTS_OFF = RenderType.entityTranslucent(
         ResourceLocation.fromNamespaceAndPath("spartanshields", "textures/entity/enderio/dark_steel_tower_shield_lights_off.png"));

   public DarkSteelTowerShieldRenderInfo(float rIn, float gIn, float bIn) {
      super(ResourceLocation.fromNamespaceAndPath("spartanshields", "entity/enderio/dark_steel_tower_shield_nopattern"),
            ResourceLocation.fromNamespaceAndPath("spartanshields", "entity/enderio/dark_steel_tower_shield_pattern"));
      this.r = rIn;
      this.g = gIn;
      this.b = bIn;
   }

   @Override
   public boolean hasLayers() {
      return true;
   }

   @Override
   public RenderType getLayerRenderType(ItemStack stack) {
      boolean isPowered = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(FEPoweredShieldItem.NBT_ENERGY, 0) != 0;
      return isPowered ? LIGHTS_ON : LIGHTS_OFF;
   }

   @Override
   public float getColourRed() {
      return this.r;
   }

   @Override
   public float getColourGreen() {
      return this.g;
   }

   @Override
   public float getColourBlue() {
      return this.b;
   }
}
