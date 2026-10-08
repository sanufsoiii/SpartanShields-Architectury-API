package com.oblivioussp.spartanshields.network;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.init.ModEnchantments;
import com.oblivioussp.spartanshields.init.ModSounds;
import com.oblivioussp.spartanshields.init.ModStats;
import com.oblivioussp.spartanshields.item.PaybackDamage;
import com.oblivioussp.spartanshields.tags.ModItemTags;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * Client -> server: the player performed a shield bash.
 * <p>
 * Porting note: Forge's {@code ToolActions.SHIELD_BLOCK} check (a Forge item capability that tells
 * the game an item can raise a shield) has no Fabric equivalent. Vanilla already routes blocking
 * through {@code ShieldItem}, and every item in the {@code shields_with_bash} tag is one of ours, so
 * the check is now {@code item instanceof ShieldItem}, which is what vanilla's blocking code
 * itself tests.
 */
public class ShieldBashPacket extends BaseC2SMessage {
   /**
    * Porting note: the Forge release used {@code player.getEntityReach()}, a Forge
    * {@code IForgePlayer} extension. Vanilla 1.20.1 has no reach attribute (it arrived in 1.20.2),
    * so the tower shield sweep radius falls back to Forge's default block reach.
    */
   public static final double TOWER_BASH_REACH = 4.5D;

   public static final MessageType TYPE =
         NetworkHandler.NETWORK_MANAGER.registerC2S("shield_bash", ShieldBashPacket::decode);

   protected final InteractionHand hand;
   protected final int entityId;
   protected final boolean attackEntity;

   public ShieldBashPacket(InteractionHand handIn, int entityIdIn, boolean attackEntityIn) {
      this.hand = handIn;
      this.entityId = entityIdIn;
      this.attackEntity = attackEntityIn;
   }

   public InteractionHand getHand() {
      return this.hand;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public boolean isAttackEntity() {
      return this.attackEntity;
   }

   @Override
   public MessageType getType() {
      return TYPE;
   }

   @Override
   public void write(RegistryFriendlyByteBuf buf) {
      buf.writeEnum(this.hand);
      buf.writeInt(this.entityId);
      buf.writeBoolean(this.attackEntity);
   }

   /**
    * Porting note: {@code ItemStack#getEnchantmentLevel} is a Forge extension
    * ({@code IForgeItemStack}) and 1.20.1's EnchantmentHelper has no ItemStack overload, so the
    * level comes from the stack's enchantment map.
    */
   private static int enchantmentLevel(ItemStack stack, net.minecraft.resources.ResourceKey<Enchantment> enchantment) {
      return ModEnchantments.getEnchantmentLevel(stack, enchantment);
   }

   public static ShieldBashPacket decode(RegistryFriendlyByteBuf buf) {
      return new ShieldBashPacket(buf.readEnum(InteractionHand.class), buf.readInt(), buf.readBoolean());
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      context.queue(() -> {
         if (!(context.getPlayer() instanceof ServerPlayer player)) {
            return;
         }

         Entity victim = player.level().getEntity(this.entityId);
         if (!player.isBlocking()) {
            return;
         }

         ItemStack shieldStack = player.getItemInHand(this.hand);
         boolean isTowerShield = shieldStack.is(ModItemTags.TOWER_SHIELDS);
         if (shieldStack.isEmpty()
               || player.getCooldowns().isOnCooldown(shieldStack.getItem())
               || !player.getUseItem().is(ModItemTags.SHIELDS_WITH_BASH)) {
            return;
         }

         if (this.attackEntity && victim instanceof LivingEntity) {
            int knockLvl = enchantmentLevel(shieldStack, Enchantments.KNOCKBACK);
            if (isTowerShield) {
               double reach = TOWER_BASH_REACH;

               for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class,
                     victim.getBoundingBox().inflate(1.0, 0.25, 1.0),
                     target -> target != player
                           && target != victim
                           && !target.isAlliedTo(player)
                           && (!(target instanceof ArmorStand) || !((ArmorStand) target).isMarker())
                           && player.distanceToSqr(target) < reach * reach)) {
                  bashEntity(entity, player, shieldStack, knockLvl, this.hand);
               }
            }

            boolean powerfulBash = bashEntity(victim, player, shieldStack, knockLvl, this.hand);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                  ModSounds.SHIELD_BASH_HIT.get(), player.getSoundSource(), 1.0F, powerfulBash ? 2.0F : 1.0F);
            player.crit(victim);
            player.awardStat(ModStats.SHIELD_BASH_HITS);
         } else {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                  ModSounds.SHIELD_BASH_MISS.get(), player.getSoundSource(), 0.5F, 0.01F);
         }

         player.stopUsingItem();
         player.getCooldowns().addCooldown(shieldStack.getItem(),
               isTowerShield ? Config.INSTANCE.cooldownTowerShieldBash.get() : Config.INSTANCE.cooldownShieldBash.get());
      });
   }

   protected static boolean bashEntity(Entity targetEntity, Player player, ItemStack shieldStack, int knockbackLevel, InteractionHand hand) {
      targetEntity.invulnerableTime = 0;
      ((LivingEntity) targetEntity).knockback(
            1.0F + knockbackLevel,
            Mth.sin(player.getYRot() * ((float) Math.PI / 180.0F)),
            -Mth.cos(player.getYRot() * ((float) Math.PI / 180.0F)));
      float bashDamage = 1.0F;
      if (enchantmentLevel(shieldStack, ModEnchantments.PAYBACK) != 0) {
         net.minecraft.world.item.component.CustomData customData = shieldStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
         bashDamage += customData.copyTag().getFloat(PaybackDamage.NBT_PAYBACK_DMG);
         net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, shieldStack, tag -> tag.putFloat(PaybackDamage.NBT_PAYBACK_DMG, 0.0F));
      }

      targetEntity.hurt(player.damageSources().playerAttack(player), bashDamage);
      shieldStack.hurtAndBreak(5, player, hand == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
      int firebrandLvl = enchantmentLevel(shieldStack, ModEnchantments.FIREBRAND);
      if (firebrandLvl != 0) {
         targetEntity.igniteForSeconds(firebrandLvl * 5.0F);
      }

      return bashDamage > 1.0F;
   }
}
