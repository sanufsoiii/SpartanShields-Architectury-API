package com.oblivioussp.spartanshields.item;

import com.oblivioussp.spartanshields.SpartanShields;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ObsidianShieldItem extends BasicShieldItem {
   private static final Identifier MOVE_SPEED_ID = Identifier.fromNamespaceAndPath(SpartanShields.ID, "shield_move_speed");
   private static final Identifier KNOCKBACK_ID = Identifier.fromNamespaceAndPath(SpartanShields.ID, "shield_knockback");

   public ObsidianShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop.attributes(createAttributes()));
   }

   public ObsidianShieldItem(TierSS toolMaterial, int defaultMaxDamage, Properties prop) {
      this(toolMaterial, defaultMaxDamage, false, prop);
   }

   private static ItemAttributeModifiers createAttributes() {
      return ItemAttributeModifiers.builder()
            .add(Attributes.MOVEMENT_SPEED,
                  new AttributeModifier(MOVE_SPEED_ID, -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                  EquipmentSlotGroup.HAND)
            .add(Attributes.KNOCKBACK_RESISTANCE,
                  new AttributeModifier(KNOCKBACK_ID, 0.5, AttributeModifier.Operation.ADD_VALUE),
                  EquipmentSlotGroup.HAND)
            .build();
   }
}
