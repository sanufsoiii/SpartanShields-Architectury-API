package com.oblivioussp.spartanshields.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

/**
 * Geometry ported verbatim from the Forge release: the model only touches vanilla client
 * classes (Model / ModelPart / LayerDefinition), so the identical code renders on both
 * loaders.
 */
public class DarkSteelTowerShieldModel extends ShieldBaseModel {
   protected static final String PART_PLATE_EXTRA = "plate_extra";
   protected static final String PART_UPPER = "plate_upper";
   protected static final String PART_LOWER = "plate_lower";
   public ModelPart plateExtra;
   public ModelPart upper;
   public ModelPart lower;
   private final ImmutableList<ModelPart> extraParts;

   public DarkSteelTowerShieldModel(ModelPart rootModel) {
      super(RenderType::entityTranslucent, rootModel);
      this.plateExtra = rootModel.getChild("plate_extra");
      this.upper = rootModel.getChild("plate_upper");
      this.lower = rootModel.getChild("plate_lower");
      this.extraParts = ImmutableList.of(this.plateExtra, this.upper, this.lower);
   }

   public static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition part = mesh.getRoot();
      PartDefinition platePart = part.addOrReplaceChild(
         "plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -7.0F, -2.0F, 12.0F, 17.0F, 1.0F), PartPose.ZERO
      );
      part.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
      platePart.addOrReplaceChild(
         "plate_lower_main",
         CubeListBuilder.create().texOffs(0, 17).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 5.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, 10.0F, -2.0F, 0.3491F, 0.0F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_extra",
         CubeListBuilder.create().texOffs(50, 9).addBox(6.0F, -7.0F, -2.5F, 2.0F, 16.0F, 2.0F).texOffs(42, 9).addBox(-8.0F, -7.0F, -2.5F, 2.0F, 16.0F, 2.0F),
         PartPose.ZERO
      );
      part.addOrReplaceChild(
         "plate_upper",
         CubeListBuilder.create()
            .texOffs(16, 24)
            .addBox(-4.0F, -7.0F, 0.0F, 8.0F, 1.0F, 1.0F)
            .texOffs(0, 24)
            .addBox(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 2.0F)
            .texOffs(42, 0)
            .addBox(-7.9F, -7.0F, -0.5F, 2.0F, 7.0F, 2.0F)
            .texOffs(50, 0)
            .addBox(5.9F, -7.0F, -0.5F, 2.0F, 7.0F, 2.0F)
            .texOffs(12, 26)
            .addBox(-6.0F, -6.0F, 0.0F, 12.0F, 6.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, -7.0F, -2.0F, -0.3491F, 0.0F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_lower",
         CubeListBuilder.create().texOffs(42, 27).addBox(-7.9F, -1.0F, -0.1F, 2.0F, 5.0F, 2.0F).texOffs(50, 27).addBox(5.9F, -1.0F, -0.1F, 2.0F, 5.0F, 2.0F),
         PartPose.offsetAndRotation(0.0F, 10.0F, -2.0F, 0.3491F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderExtraParts(
      PoseStack mStack, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha
   ) {
      super.renderExtraParts(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      this.extraParts.forEach(model -> model.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha));
   }

   @Override
   public void renderLayers(
      PoseStack mStack,
      MultiBufferSource bufferIn,
      RenderType renderTypeIn,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      VertexConsumer consumer = bufferIn.getBuffer(renderTypeIn);
      this.root.render(mStack, consumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
   }
}
