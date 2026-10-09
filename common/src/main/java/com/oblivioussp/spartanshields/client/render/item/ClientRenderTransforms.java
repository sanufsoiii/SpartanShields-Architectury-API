package com.oblivioussp.spartanshields.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Applies an item model's {@code display} transforms before handing the item to a custom renderer.
 * <p>
 * Porting note: the tower shields use {@code "parent": "builtin/entity"} with
 * {@code "gui": {"scale": 0.65}} in their model json, so the whole size in the inventory comes from
 * those transforms - {@code ItemRenderer} applies them and then does
 * {@code translate(-0.5, -0.5, -0.5)} <em>before</em> delegating to a
 * {@code BlockEntityWithoutLevelRenderer}. Since the port intercepts the render earlier
 * ({@code mixin.MixinItemRenderer}), that preamble has to be reproduced by hand; without it the
 * tower shields render at 1:1 instead of 0.65, i.e. far too large.
 * <p>
 * Architectury 9.x (the only line published for MC 1.20.1) has no {@code @ExpectPlatform}, so the
 * loader-specific part is injected with a plain setter: vanilla's
 * {@code BakedModel#applyTransform} is the default, and Forge installs
 * {@code ForgeHooksClient::handleCameraTransforms}, which is the exact call Forge's patched
 * {@code ItemRenderer} makes (display transforms plus the first-person equip/swing animation).
 */
public class ClientRenderTransforms {
   /** Loader-specific part of {@code ItemRenderer#render}'s preamble. */
   public interface Applier {
      void apply(BakedModel model, ItemDisplayContext displayContext, PoseStack pose, boolean leftHand);
   }

   // In 1.20.1 BakedModel exposes the transforms rather than applying them, so vanilla's
   // ItemRenderer reads ItemTransforms#getTransform(ctx).apply(leftHand, pose).
   private static final Applier VANILLA = (model, displayContext, pose, leftHand) ->
         model.getTransforms().getTransform(displayContext).apply(leftHand, pose);

   private static Applier applier = VANILLA;

   public static void setApplier(Applier newApplier) {
      applier = newApplier == null ? VANILLA : newApplier;
   }

   public static void apply(BakedModel model, ItemDisplayContext displayContext, PoseStack pose, boolean leftHand) {
      applier.apply(model, displayContext, pose, leftHand);
   }

   private ClientRenderTransforms() {
   }
}
