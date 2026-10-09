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
import net.minecraft.client.renderer.rendertype.RenderType;

/**
 * Geometry ported verbatim from the Forge release: the model only touches vanilla client
 * classes (Model / ModelPart / LayerDefinition), so the identical code renders on both
 * loaders.
 */
public class MekanismTowerShieldModel extends ShieldBaseModel {
   protected static final String PART_PLATE_EXTRA = "plate_extra";
   protected static final String PART_UPPER = "plate_upper";
   protected static final String PART_LOWER = "plate_lower";
   public ModelPart plateExtra;
   public ModelPart upper;
   public ModelPart lower;
   private final ImmutableList<ModelPart> extraParts;

   public MekanismTowerShieldModel(ModelPart rootModel) {
      super(net.minecraft.client.renderer.rendertype.RenderTypes::entityTranslucent, rootModel);
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
         PartPose.offsetAndRotation(0.0F, 10.0F, -2.0F, 0.5236F, 0.0F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_extra",
         CubeListBuilder.create()
            .texOffs(59, 16)
            .addBox(7.5F, -6.5F, -1.0F, 1.0F, 15.0F, 1.0F)
            .texOffs(50, 0)
            .addBox(5.5F, -7.0F, -2.5F, 1.0F, 16.0F, 3.0F)
            .texOffs(59, 0)
            .addBox(-8.5F, -6.5F, -1.0F, 1.0F, 15.0F, 1.0F)
            .texOffs(42, 0)
            .addBox(-6.5F, -7.0F, -2.5F, 1.0F, 16.0F, 3.0F),
         PartPose.ZERO
      );
      part.addOrReplaceChild(
         "plate_upper",
         CubeListBuilder.create()
            .texOffs(0, 29)
            .addBox(-9.0F, -3.0F, -0.5F, 4.0F, 3.0F, 2.0F)
            .texOffs(38, 29)
            .addBox(5.0F, -3.0F, -0.5F, 4.0F, 3.0F, 2.0F)
            .texOffs(0, 34)
            .addBox(-9.0F, -2.0F, 1.5F, 4.0F, 2.0F, 2.0F)
            .texOffs(38, 34)
            .addBox(5.0F, -2.0F, 1.5F, 4.0F, 2.0F, 2.0F)
            .texOffs(2, 24)
            .addBox(-8.0F, -6.0F, -0.5F, 2.0F, 3.0F, 2.0F)
            .texOffs(40, 24)
            .addBox(6.0F, -6.0F, -0.5F, 2.0F, 3.0F, 2.0F)
            .texOffs(12, 24)
            .addBox(-6.0F, -6.0F, 0.0F, 12.0F, 6.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, -7.0F, -2.0F, -0.5236F, 0.0F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_lower",
         CubeListBuilder.create()
            .texOffs(0, 42)
            .addBox(-9.0F, -1.0F, -0.5F, 4.0F, 3.0F, 2.0F)
            .texOffs(0, 38)
            .addBox(-9.0F, -1.0F, 1.5F, 4.0F, 2.0F, 2.0F)
            .texOffs(2, 47)
            .addBox(-8.0F, 2.0F, -0.5F, 3.0F, 4.0F, 2.0F)
            .texOffs(38, 47)
            .addBox(5.0F, 2.0F, -0.5F, 3.0F, 4.0F, 2.0F)
            .texOffs(38, 38)
            .addBox(5.0F, -1.0F, 1.5F, 4.0F, 2.0F, 2.0F)
            .texOffs(38, 42)
            .addBox(5.0F, -1.0F, -0.5F, 4.0F, 3.0F, 2.0F),
         PartPose.offsetAndRotation(0.0F, 10.0F, -2.0F, 0.5236F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderExtraParts(
      PoseStack mStack, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, int color
   ) {
      super.renderExtraParts(mStack, vertexBuilder, packedLightIn, packedOverlayIn, color);
      this.extraParts.forEach(model -> model.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, color));
   }

   @Override
   public void renderLayers(
      PoseStack mStack,
      MultiBufferSource bufferIn,
      RenderType renderTypeIn,
      int packedLightIn,
      int packedOverlayIn,
      int color
   ) {
      VertexConsumer consumer = bufferIn.getBuffer(renderTypeIn);
      this.root.render(mStack, consumer, packedLightIn, packedOverlayIn, color);
   }
}
