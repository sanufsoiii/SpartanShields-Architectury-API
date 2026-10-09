package com.oblivioussp.spartanshields.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Geometry ported verbatim from the Forge release: the model only touches vanilla client
 * classes (Model / ModelPart / LayerDefinition), so the identical code renders on both
 * loaders.
 */
public class ElementiumTowerShieldModel extends ShieldBaseModel {
   protected static final String PART_PLATE_EXTRA = "plate_extra";
   public ModelPart plateExtra;

   public ElementiumTowerShieldModel(ModelPart rootModel) {
      super(rootModel);
      this.plateExtra = rootModel.getChild("plate_extra");
   }

   public static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition part = mesh.getRoot();
      part.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 1.0F), PartPose.ZERO);
      part.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
      part.addOrReplaceChild(
         "plate_extra",
         CubeListBuilder.create()
            .texOffs(50, 0)
            .addBox(-8.0F, -13.0F, -2.0F, 2.0F, 23.0F, 1.0F)
            .texOffs(57, 1)
            .addBox(-6.0F, -12.0F, -2.0F, 2.0F, 1.0F, 1.0F)
            .texOffs(46, 2)
            .addBox(-9.0F, -11.0F, -2.0F, 1.0F, 19.0F, 1.0F)
            .texOffs(42, 5)
            .addBox(-10.0F, -8.0F, -2.0F, 1.0F, 12.0F, 1.0F)
            .texOffs(48, 24)
            .addBox(6.0F, -13.0F, -2.0F, 2.0F, 23.0F, 1.0F)
            .texOffs(42, 25)
            .addBox(4.0F, -12.0F, -2.0F, 2.0F, 1.0F, 1.0F)
            .texOffs(55, 26)
            .addBox(8.0F, -11.0F, -2.0F, 1.0F, 19.0F, 1.0F)
            .texOffs(59, 29)
            .addBox(9.0F, -8.0F, -2.0F, 1.0F, 12.0F, 1.0F),
         PartPose.ZERO
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderExtraParts(
      PoseStack mStack, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, int color
   ) {
      super.renderExtraParts(mStack, vertexBuilder, packedLightIn, packedOverlayIn, color);
      this.plateExtra.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, color);
   }
}
