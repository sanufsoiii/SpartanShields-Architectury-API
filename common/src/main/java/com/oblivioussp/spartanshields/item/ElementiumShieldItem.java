package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.compat.BotaniaCompat;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ElementiumShieldItem extends BotaniaShieldItem {
   private static final ResourceLocation PIXIE_SPAWN_ID = ResourceLocation.fromNamespaceAndPath(SpartanShields.ID, "shield_pixie_spawn");

   public ElementiumShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, int manaPerDamage, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, manaPerDamage, prop.attributes(createAttributes()));
   }

   private static ItemAttributeModifiers createAttributes() {
      ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
      Holder<Attribute> pixieChance = BotaniaCompat.pixieSpawnChanceHolder();
      if (pixieChance != null) {
         builder.add(pixieChance,
               new AttributeModifier(PIXIE_SPAWN_ID, 0.1, AttributeModifier.Operation.ADD_VALUE),
               EquipmentSlotGroup.HAND);
      }
      return builder.build();
   }
}
