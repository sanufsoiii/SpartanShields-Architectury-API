package com.oblivioussp.spartanshields.client.render.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.oblivioussp.spartanshields.client.model.DarkSteelTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.ElementiumTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.EnderiumShieldModel;
import com.oblivioussp.spartanshields.client.model.KiteShieldModel;
import com.oblivioussp.spartanshields.client.model.LumiumShieldModel;
import com.oblivioussp.spartanshields.client.model.ManasteelTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.MekanismTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.ShieldBaseModel;
import com.oblivioussp.spartanshields.client.model.TerrasteelTowerShieldModel;
import com.oblivioussp.spartanshields.client.model.TowerShieldModel;
import com.oblivioussp.spartanshields.init.ModItems;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

/**
 * Custom renderer for every tower shield.
 * <p>
 * Note: the Forge release registered this as an {@code IClientItemExtensions} renderer and
 * refreshed its models through {@code RegisterClientReloadListenersEvent}. Architectury 9.x has no
 * cross-loader item-renderer registry, so the renderer is invoked from
 * {@code mixin.MixinItemRenderer} and the models are (re)baked lazily through the vanilla
 * {@code EntityModelLoader}, which makes the class correct on both loaders regardless of when the
 * resource reload happens relative to layer registration.
 */
public class TowerShieldBEWLR extends BlockEntityWithoutLevelRenderer {
   public static final TowerShieldBEWLR INSTANCE = new TowerShieldBEWLR();
   private ShieldBaseModel baseShield;
   private ShieldBaseModel kiteShield;
   private ShieldBaseModel towerShield;
   private ShieldBaseModel lumiumShield;
   private ShieldBaseModel enderiumShield;
   private ShieldBaseModel manasteelShield;
   private ShieldBaseModel terrasteelShield;
   private ShieldBaseModel elementiumShield;
   private ShieldBaseModel mekanismShield;
   private ShieldBaseModel darkSteelShield;
   private Map<Item, ShieldBaseModel> modelMap;
   private Map<Item, TowerShieldRenderInfo> renderInfoMap;
   private ResourceManager bakedAgainst;

   private TowerShieldBEWLR() {
      super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
      Builder<Item, TowerShieldRenderInfo> renderInfoMapBuilder = ImmutableMap.builder();
      renderInfoMapBuilder.put((Item)ModItems.WOODEN_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_WOODEN_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.STONE_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_STONE_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.COPPER_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_COPPER_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.IRON_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_IRON_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.GOLDEN_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_GOLDEN_TOWER_SHIELD_);
      renderInfoMapBuilder.put((Item)ModItems.DIAMOND_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_DIAMOND_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.NETHERITE_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_NETHERITE_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.OBSIDIAN_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_OBSIDIAN_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.TIN_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_TIN_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.BRONZE_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_BRONZE_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.STEEL_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_STEEL_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.SILVER_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_SILVER_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.ELECTRUM_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_ELECTRUM_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.LEAD_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_LEAD_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.NICKEL_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_NICKEL_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.INVAR_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_INVAR_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.CONSTANTAN_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_CONSTANTAN_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.PLATINUM_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_PLATINUM_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.ALUMINUM_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_ALUMINUM_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.SIGNALUM_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_SIGNALUM_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.LUMIUM_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_LUMIUM_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.ENDERIUM_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_ENDERIUM_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.MANASTEEL_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_MANASTEEL_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.TERRASTEEL_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_TERRASTEEL_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.ELEMENTIUM_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_ELEMENTIUM_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.BASIC_MEKANISTS_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_BASIC_MEKANISTS_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.ADVANCED_MEKANISTS_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_ADVANCED_MEKANISTS_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.ELITE_MEKANISTS_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_ELITE_MEKANISTS_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.ULTIMATE_MEKANISTS_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_ULTIMATE_MEKANISTS_TOWER_SHIELD);
      renderInfoMapBuilder.put((Item)ModItems.DARK_STEEL_RIOT_TOWER_SHIELD.get(), TextureStitcher.RENDER_INFO_DARK_STEEL_TOWER_SHIELD);
      this.renderInfoMap = renderInfoMapBuilder.build();
   }

   private void bakeModels() {
      Minecraft mc = Minecraft.getInstance();
      this.baseShield = new ShieldBaseModel(mc.getEntityModels().bakeLayer(ModelLayers.BASE_SHIELD));
      this.kiteShield = new KiteShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.KITE_SHIELD));
      this.towerShield = new TowerShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.TOWER_SHIELD));
      this.lumiumShield = new LumiumShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.LUMIUM_SHIELD));
      this.enderiumShield = new EnderiumShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.ENDERIUM_SHIELD));
      this.manasteelShield = new ManasteelTowerShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.MANASTEEL_SHIELD));
      this.terrasteelShield = new TerrasteelTowerShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.TERRASTEEL_SHIELD));
      this.elementiumShield = new ElementiumTowerShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.ELEMENTIUM_SHIELD));
      this.mekanismShield = new MekanismTowerShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.MEKANISM_SHIELD));
      this.darkSteelShield = new DarkSteelTowerShieldModel(mc.getEntityModels().bakeLayer(ModelLayers.DARK_STEEL_SHIELD));
      Builder<Item, ShieldBaseModel> modelMapBuilder = ImmutableMap.builder();
      modelMapBuilder.put((Item)ModItems.WOODEN_TOWER_SHIELD.get(), this.baseShield);
      modelMapBuilder.put((Item)ModItems.STONE_TOWER_SHIELD.get(), this.baseShield);
      modelMapBuilder.put((Item)ModItems.COPPER_TOWER_SHIELD.get(), this.baseShield);
      modelMapBuilder.put((Item)ModItems.IRON_TOWER_SHIELD.get(), this.kiteShield);
      modelMapBuilder.put((Item)ModItems.GOLDEN_TOWER_SHIELD.get(), this.towerShield);
      modelMapBuilder.put((Item)ModItems.DIAMOND_TOWER_SHIELD.get(), this.kiteShield);
      modelMapBuilder.put((Item)ModItems.NETHERITE_TOWER_SHIELD.get(), this.kiteShield);
      modelMapBuilder.put((Item)ModItems.OBSIDIAN_TOWER_SHIELD.get(), this.baseShield);
      modelMapBuilder.put((Item)ModItems.TIN_TOWER_SHIELD.get(), this.baseShield);
      modelMapBuilder.put((Item)ModItems.BRONZE_TOWER_SHIELD.get(), this.kiteShield);
      modelMapBuilder.put((Item)ModItems.STEEL_TOWER_SHIELD.get(), this.kiteShield);
      modelMapBuilder.put((Item)ModItems.SILVER_TOWER_SHIELD.get(), this.towerShield);
      modelMapBuilder.put((Item)ModItems.ELECTRUM_TOWER_SHIELD.get(), this.towerShield);
      modelMapBuilder.put((Item)ModItems.LEAD_TOWER_SHIELD.get(), this.kiteShield);
      modelMapBuilder.put((Item)ModItems.NICKEL_TOWER_SHIELD.get(), this.baseShield);
      modelMapBuilder.put((Item)ModItems.INVAR_TOWER_SHIELD.get(), this.towerShield);
      modelMapBuilder.put((Item)ModItems.CONSTANTAN_TOWER_SHIELD.get(), this.towerShield);
      modelMapBuilder.put((Item)ModItems.PLATINUM_TOWER_SHIELD.get(), this.kiteShield);
      modelMapBuilder.put((Item)ModItems.ALUMINUM_TOWER_SHIELD.get(), this.baseShield);
      modelMapBuilder.put((Item)ModItems.SIGNALUM_TOWER_SHIELD.get(), this.towerShield);
      modelMapBuilder.put((Item)ModItems.LUMIUM_TOWER_SHIELD.get(), this.lumiumShield);
      modelMapBuilder.put((Item)ModItems.ENDERIUM_TOWER_SHIELD.get(), this.enderiumShield);
      modelMapBuilder.put((Item)ModItems.MANASTEEL_TOWER_SHIELD.get(), this.manasteelShield);
      modelMapBuilder.put((Item)ModItems.TERRASTEEL_TOWER_SHIELD.get(), this.terrasteelShield);
      modelMapBuilder.put((Item)ModItems.ELEMENTIUM_TOWER_SHIELD.get(), this.elementiumShield);
      modelMapBuilder.put((Item)ModItems.BASIC_MEKANISTS_TOWER_SHIELD.get(), this.mekanismShield);
      modelMapBuilder.put((Item)ModItems.ADVANCED_MEKANISTS_TOWER_SHIELD.get(), this.mekanismShield);
      modelMapBuilder.put((Item)ModItems.ELITE_MEKANISTS_TOWER_SHIELD.get(), this.mekanismShield);
      modelMapBuilder.put((Item)ModItems.ULTIMATE_MEKANISTS_TOWER_SHIELD.get(), this.mekanismShield);
      modelMapBuilder.put((Item)ModItems.DARK_STEEL_RIOT_TOWER_SHIELD.get(), this.darkSteelShield);
      this.modelMap = modelMapBuilder.build();
   }

   private Map<Item, ShieldBaseModel> modelMap() {
      // Vanilla swaps the ResourceManager instance on every resource reload, so comparing it is a
      // loader-agnostic way to notice that the baked layers went stale. This replaces the Forge
      // release's RegisterClientReloadListenersEvent listener.
      ResourceManager current = Minecraft.getInstance().getResourceManager();
      if (this.modelMap == null || this.bakedAgainst != current) {
         this.bakedAgainst = current;
         this.bakeModels();
      }

      return this.modelMap;
   }

   public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack mStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      ShieldBaseModel model = this.modelMap().get(stack.getItem());
      TowerShieldRenderInfo renderInfo = this.renderInfoMap.get(stack.getItem());
      if (model != null && renderInfo != null) {
         CompoundTag blockEntityTag = stack.getTagElement("BlockEntityTag");
         boolean isBannered = blockEntityTag != null && !blockEntityTag.isEmpty();
         mStack.pushPose();
         mStack.scale(1.0F, -1.0F, -1.0F);
         Material material = renderInfo.getMaterial(isBannered);
         VertexConsumer consumer = material.sprite()
            .wrap(ItemRenderer.getFoilBufferDirect(buffer, model.renderType(material.atlasLocation()), true, stack.hasFoil()));
         model.renderExtraParts(mStack, consumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
         if (isBannered) {
            List<Pair<Holder<BannerPattern>, DyeColor>> bannerPatterns = BannerBlockEntity.createPatterns(
               ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack)
            );
            BannerRenderer.renderPatterns(mStack, buffer, packedLight, packedOverlay, model.getPlate(), material, false, bannerPatterns, stack.hasFoil());
         } else {
            model.getPlate().render(mStack, consumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
         }

         if (renderInfo.hasLayers()) {
            model.renderLayers(
               mStack,
               buffer,
               renderInfo.getLayerRenderType(stack),
               packedLight,
               packedOverlay,
               renderInfo.getColourRed(),
               renderInfo.getColourGreen(),
               renderInfo.getColourBlue(),
               1.0F
            );
         }

         mStack.popPose();
      }
   }
}
