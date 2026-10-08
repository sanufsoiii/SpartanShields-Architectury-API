package com.oblivioussp.spartanshields.mixin;

import com.oblivioussp.spartanshields.client.render.item.TowerShieldBEWLR;
import com.oblivioussp.spartanshields.item.ShieldBaseItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public abstract class MixinItemModelResolver {
   @Inject(method = "appendItemLayers", at = @At("TAIL"))
   private void spartanshields$setupTowerShield(
         ItemStackRenderState state, ItemStack stack, ItemDisplayContext displayContext,
         Level level, LivingEntity entity, int seed, CallbackInfo ci) {
      if (stack.getItem() instanceof ShieldBaseItem shield && shield.isTowerShield()) {
         ItemStackRenderStateAccessor stateAccessor = (ItemStackRenderStateAccessor) (Object) state;
         int layerCount = stateAccessor.spartanshields$getActiveLayerCount();
         ItemStackRenderState.LayerRenderState[] layers = stateAccessor.spartanshields$getLayers();
         for (int i = 0; i < layerCount; i++) {
            ItemStackRenderState.LayerRenderState layer = layers[i];
            LayerRenderStateAccessor layerAccessor = (LayerRenderStateAccessor) (Object) layer;
            BakedModel model = layerAccessor.spartanshields$getModel();
            layer.setupSpecialModel(TowerShieldBEWLR.INSTANCE, stack, model);
         }
      }
   }
}
