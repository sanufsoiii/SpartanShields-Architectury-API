package com.oblivioussp.spartanshields.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oblivioussp.spartanshields.compat.BotaniaCompat;
import com.oblivioussp.spartanshields.util.TierSS;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ElementiumShieldItem extends BotaniaShieldItem {
   public ElementiumShieldItem(TierSS toolMaterial, int defaultMaxDamage, boolean isTowerShieldIn, int manaPerDamage, Properties prop) {
      super(toolMaterial, defaultMaxDamage, isTowerShieldIn, manaPerDamage, prop);
   }

   /** See {@link ObsidianShieldItem#getDefaultAttributeModifiers} for why a new multimap is built. */
   @Override
   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
      Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
      Multimap<Attribute, AttributeModifier> inherited = super.getDefaultAttributeModifiers(slot);
      if (inherited != null && !inherited.isEmpty()) {
         modifiers.putAll(inherited);
      }

      if (slot == EquipmentSlot.OFFHAND || slot == EquipmentSlot.MAINHAND) {
         // Pixie spawn chance, obtained through reflection so Botania stays optional.
         Attribute pixieChance = BotaniaCompat.pixieSpawnChanceAttribute();
         if (pixieChance != null) {
            AttributeModifier modifier = BotaniaCompat.pixieModifier(slot, "Shield modifier", 0.1);
            if (modifier != null) {
               modifiers.put(pixieChance, modifier);
            }
         }
      }

      return modifiers;
   }
}
