package com.oblivioussp.spartanshields.event;

import com.oblivioussp.spartanshields.config.Config;
import com.oblivioussp.spartanshields.network.ShieldBashPacket;
import com.oblivioussp.spartanshields.tags.ModItemTags;
import com.oblivioussp.spartanshields.util.Log;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ClientEventHandler {
   public static void register() {
      ClientRawInputEvent.MOUSE_CLICKED_POST.register((mc, button, action, modifiers) -> {
         checkForShieldBash();
         return EventResult.pass();
      });
      ClientRawInputEvent.KEY_PRESSED.register((mc, key, scancode, modifiers, action) -> {
         checkForShieldBash();
         return EventResult.pass();
      });
      ClientTooltipEvent.ITEM.register(ClientEventHandler::onItemTooltip);
   }

   private static void checkForShieldBash() {
      Minecraft mc = Minecraft.getInstance();
      Player player = mc.player;
      if (Config.INSTANCE.disableShieldBash.get() || mc.level == null || mc.screen != null || mc.isPaused() || player == null) {
         return;
      }

      if (!player.isBlocking()) {
         return;
      }

      ItemStack usedItem = player.getUseItem();
      if (!usedItem.is(ModItemTags.SHIELDS_WITH_BASH) || !(usedItem.getItem() instanceof ShieldItem)) {
         return;
      }

      KeyMapping bashKey = clientModKeyBinds();
      if (bashKey.isUnbound() ? !mc.options.keyAttack.consumeClick() : !bashKey.isDown()) {
         return;
      }

      ItemStack shieldStack = player.getUseItem();
      InteractionHand shieldHand = player.getUsedItemHand();
      if (player.getCooldowns().isOnCooldown(shieldStack.getItem())) {
         return;
      }

      float reach = (float) player.entityInteractionRange();
      HitResult result = getEntityMouseOverExtended(reach);
      if (result == null) {
         return;
      }

      int entId = -1;
      boolean attackEntity = true;
      if (result instanceof EntityHitResult entityRayTrace && entityRayTrace.getEntity() != null && entityRayTrace.getEntity() != player) {
         Log.debug("Hit Entity with Shield Bash! - " + entityRayTrace.getEntity());
         entId = entityRayTrace.getEntity().getId();
      }

      if (entId == -1) {
         entId = 0;
         attackEntity = false;
      }

      player.swing(shieldHand, true);
      new ShieldBashPacket(shieldHand, entId, attackEntity).sendToServer();
   }

   private static void onItemTooltip(ItemStack stack, List<Component> tooltip, Item.TooltipContext context, TooltipFlag flag) {
      if (Config.INSTANCE.disableShieldBash.get()
            || stack.isEmpty()
            || !stack.is(ModItemTags.SHIELDS_WITH_BASH)
            || !(stack.getItem() instanceof ShieldItem)) {
         return;
      }

      KeyMapping boundKey = clientModKeyBinds();
      Component line = Component.translatable("tooltip.spartanshields.shield_bash",
            Component.translatable("tooltip.spartanshields.shield_bash.value",
                  Component.translatable(boundKey.getTranslatedKeyMessage().getString().toUpperCase())
                        .withStyle(ChatFormatting.AQUA))
                  .withStyle(ChatFormatting.GRAY))
            .withStyle(ChatFormatting.GOLD);

      try {
         tooltip.add(1, line);
      } catch (UnsupportedOperationException e) {
         tooltip.add(line);
      }
   }

   private static KeyMapping clientModKeyBinds() {
      return com.oblivioussp.spartanshields.client.ModKeyBinds.KEY_ALT_SHIELD_BASH;
   }

   private static HitResult getEntityMouseOverExtended(float reach) {
      HitResult result = null;
      Minecraft mc = Minecraft.getInstance();
      Entity viewEntity = mc.getCameraEntity();
      if (viewEntity != null && mc.level != null) {
         double d0 = reach;
         HitResult rayTrace = viewEntity.pick(d0, 0.0F, false);
         Vec3 eyePos = viewEntity.getEyePosition(0.0F);
         double d1 = d0 * d0;
         if (rayTrace != null) {
            d1 = rayTrace.getLocation().distanceToSqr(eyePos);
         }

         Vec3 lookVec = viewEntity.getViewVector(1.0F);
         Vec3 attackVec = eyePos.add(lookVec.x * d0, lookVec.y * d0, lookVec.z * d0);
         AABB expBounds = viewEntity.getBoundingBox().expandTowards(lookVec.scale(d0)).inflate(1.0, 1.0, 1.0);
         EntityHitResult entityRayTrace = ProjectileUtil.getEntityHitResult(viewEntity, eyePos, attackVec, expBounds,
               entity -> !entity.isSpectator() && entity.isPickable(), d1);
         if (entityRayTrace != null) {
            Vec3 hitVec = entityRayTrace.getLocation();
            double d2 = eyePos.distanceToSqr(hitVec);
            if (d2 < d1 || result == null) {
               result = entityRayTrace;
            }
         } else {
            result = BlockHitResult.miss(attackVec, Direction.getNearest(lookVec.x, lookVec.y, lookVec.z), BlockPos.containing(attackVec));
         }
      }

      return result;
   }

   private ClientEventHandler() {
   }
}
