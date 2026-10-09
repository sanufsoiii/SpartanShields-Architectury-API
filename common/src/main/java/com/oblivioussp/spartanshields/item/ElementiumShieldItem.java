package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.compat.BotaniaCompat;
import com.oblivioussp.spartanshields.util.TierSS;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ElementiumShieldItem extends BotaniaShieldItem {
   private static final UUID PIXIE_SPAWN_UUID = UUID.fromString("0a6476b7-a3aa-448c-905e-f7823f5b7410");

   public ElementiumShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, int manaPerDamage, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, manaPerDamage, prop);
   }

   @Override
   public ItemAttributeModifiers getDefaultAttributeModifiers() {
      ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
      Holder<Attribute> pixieChance = BotaniaCompat.pixieSpawnChanceHolder();
      if (pixieChance != null) {
         builder.add(pixieChance,
               new AttributeModifier(PIXIE_SPAWN_UUID, "shield_pixie_spawn", 0.1, AttributeModifier.Operation.ADD_VALUE),
               EquipmentSlotGroup.HAND);
      }
      return builder.build();
   }
}
