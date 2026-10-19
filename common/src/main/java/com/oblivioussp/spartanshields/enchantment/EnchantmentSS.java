package com.oblivioussp.spartanshields.enchantment;

import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class EnchantmentSS extends Enchantment {
   /**
    * Note: the Forge release built a custom
    * {@code EnchantmentCategory.create("ss_shield", item -> item instanceof ShieldBaseItem)}.
    * Vanilla 1.20.1 has no such factory, so shields' own {@code breakable} category is used and
    * {@link #canEnchant(ItemStack)} narrows it down to Spartan Shields - which covers every shield
    * individually as well (all of them are in {@code minecraft:breakable}).
    */
   public static final EnchantmentCategory TYPE_SHIELD = EnchantmentCategory.BREAKABLE;

   protected EnchantmentSS(Enchantment.Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
      super(rarityIn, typeIn, slots);
   }

   @Override
   public boolean canEnchant(ItemStack stack) {
      return stack.getItem() instanceof ShieldBaseItem;
   }

   public abstract void onUserAttacked(LivingEntity user, Entity attacker, float damage, int level);
}
