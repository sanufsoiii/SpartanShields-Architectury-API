package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.util.Constants;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ObsidianShieldItem extends BasicShieldItem {
   public ObsidianShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop);
   }

   public ObsidianShieldItem(TierSS toolMaterial, int defaultMaxDamage, Properties prop) {
      this(toolMaterial, defaultMaxDamage, false, prop);
   }

   @Override
   public ItemAttributeModifiers getDefaultAttributeModifiers() {
      return ItemAttributeModifiers.builder()
            .add(Attributes.MOVEMENT_SPEED,
                  new AttributeModifier(Constants.SHIELD_MOVE_SPEED_UUID, "shield_move_speed", -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                  EquipmentSlotGroup.HAND)
            .add(Attributes.KNOCKBACK_RESISTANCE,
                  new AttributeModifier(Constants.SHIELD_KNOCKBACK_UUID, "shield_knockback", 0.5, AttributeModifier.Operation.ADD_VALUE),
                  EquipmentSlotGroup.HAND)
            .build();
   }
}
