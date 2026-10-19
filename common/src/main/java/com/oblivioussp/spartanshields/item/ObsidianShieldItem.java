package com.oblivioussp.spartanshields.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oblivioussp.spartanshields.util.Constants;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;

public class ObsidianShieldItem extends BasicShieldItem {
   public ObsidianShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, prop);
   }

   public ObsidianShieldItem(TierSS toolMaterial, int defaultMaxDamage, Properties prop) {
      this(toolMaterial, defaultMaxDamage, false, prop);
   }

   /**
    * Note: the Forge release overrode Forge-only
    * {@code Item#getAttributeModifiers(EquipmentSlot, ItemStack)}. Vanilla only offers
    * {@link #getDefaultAttributeModifiers(EquipmentSlot)}, which is what is overridden here, so the
    * movement-speed penalty and knockback resistance now also apply on Fabric.
    * <p>
    * The result is a fresh {@link HashMultimap} instead of the one returned by {@code super}:
    * Forge's {@code IForgeItem#getAttributeModifiers} hands out an {@code ImmutableMultimap}, so
    * writing into it throws {@code UnsupportedOperationException} as soon as a tooltip asks for the
    * modifiers (which is why the shield has to build its own, exactly as the Forge release did).
    */
   @Override
   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
      Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
      Multimap<Attribute, AttributeModifier> inherited = super.getDefaultAttributeModifiers(slot);
      if (inherited != null && !inherited.isEmpty()) {
         modifiers.putAll(inherited);
      }

      if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
         modifiers.put(Attributes.MOVEMENT_SPEED,
               new AttributeModifier(Constants.SHIELD_MOVE_SPEED_UUID, "Shield modifier", -0.3, Operation.MULTIPLY_TOTAL));
         modifiers.put(Attributes.KNOCKBACK_RESISTANCE,
               new AttributeModifier(Constants.SHIELD_KNOCKBACK_UUID, "Shield modifier", 0.5, Operation.ADDITION));
      }

      return modifiers;
   }
}
