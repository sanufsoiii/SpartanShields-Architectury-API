package com.oblivioussp.spartanshields.client.render.item;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class TowerShieldRenderInfo {
   protected final Identifier textureNoPattern;
   protected final Identifier texturePattern;
   protected final Material materialNoPattern;
   protected final Material materialPattern;

   public TowerShieldRenderInfo(Identifier texNoPattern, Identifier texPattern) {
      this.textureNoPattern = texNoPattern;
      this.texturePattern = texPattern;
      this.materialNoPattern = new Material(Sheets.SHIELD_SHEET, this.textureNoPattern);
      this.materialPattern = new Material(Sheets.SHIELD_SHEET, this.texturePattern);
   }

   public Material getMaterial(boolean isPatterned) {
      return isPatterned ? this.materialPattern : this.materialNoPattern;
   }

   public boolean hasLayers() {
      return false;
   }

   public RenderType getLayerRenderType(ItemStack stack) {
      return null;
   }

   public float getColourRed() {
      return 1.0F;
   }

   public float getColourGreen() {
      return 1.0F;
   }

   public float getColourBlue() {
      return 1.0F;
   }
}
