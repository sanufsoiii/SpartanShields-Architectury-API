package com.oblivioussp.spartanshields.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oblivioussp.spartanshields.client.render.item.ClientRenderTransforms;
import com.oblivioussp.spartanshields.client.render.item.TowerShieldBEWLR;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hands the tower shields to their custom {@link BlockEntityWithoutLevelRenderer}.
 * <p>
 * Note: this is what {@code fabric-rendering-v1} does for
 * {@code Item#getCustomRenderer}, and what Forge does through
 * {@code IClientItemExtensions#getCustomRenderer}. Architectury 9.x exposes neither, and
 * fabric-rendering-v1 is not a dependency of a Forge-targeting Architectury project, so the hook is
 * provided once here and both loaders share it.
 * <p>
 * In 1.20.1 {@code ItemRenderer#render} takes the transform flag and the baked model as well, so the
 * injection matches that eight-argument overload.
 * <p>
 * The mixin runs <em>before</em> the model's own display transforms are applied, so this injection
 * has to reproduce the preamble that {@code ItemRenderer} executes before delegating to a custom
 * renderer - the {@code display} block of {@code tower_shield_base.json} (which is where the
 * {@code 0.65} GUI scale lives) plus the {@code translate(-0.5, -0.5, -0.5)} centring. Skipping it
 * renders every tower shield about 1.5x too large and off-centre in the inventory.
 */
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
   @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;"
         + "ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;"
         + "IILnet/minecraft/client/resources/model/BakedModel;)V",
         at = @At("HEAD"), cancellable = true)
   private void spartanshields$renderTowerShield(ItemStack stack, ItemDisplayContext displayContext, boolean leftHand,
                                                 PoseStack pose, MultiBufferSource buffers, int packedLight,
                                                 int packedOverlay, BakedModel model, CallbackInfo callback) {
      if (!(stack.getItem() instanceof ShieldBaseItem shield) || !shield.isTowerShield()) {
         return;
      }

      pose.pushPose();
      try {
         ClientRenderTransforms.apply(model, displayContext, pose, leftHand);
         pose.translate(-0.5F, -0.5F, -0.5F);

         BlockEntityWithoutLevelRenderer renderer = TowerShieldBEWLR.INSTANCE;
         renderer.renderByItem(stack, displayContext, pose, buffers, packedLight, packedOverlay);
      } finally {
         pose.popPose();
      }

      callback.cancel();
   }
}
