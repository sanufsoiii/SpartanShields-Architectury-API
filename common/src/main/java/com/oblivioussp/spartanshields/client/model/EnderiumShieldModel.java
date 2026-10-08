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
public class EnderiumShieldModel extends ShieldBaseModel {
   protected static final String PART_PLATE_LEFT = "plate_left";
   protected static final String PART_PLATE_RIGHT = "plate_right";
   protected static final String PART_PLATE_UP_LEFT = "plate_up_left";
   protected static final String PART_PLATE_UP_RIGHT = "plate_up_right";
   protected static final String PART_PLATE_DOWN = "plate_down";
   public ModelPart plateLeft;
   public ModelPart plateRight;
   public ModelPart plateUpLeft;
   public ModelPart plateUpRight;
   public ModelPart plateDown;

   public EnderiumShieldModel(ModelPart rootModel) {
      super(rootModel);
      this.plateLeft = rootModel.getChild("plate_left");
      this.plateRight = rootModel.getChild("plate_right");
      this.plateUpLeft = rootModel.getChild("plate_up_left");
      this.plateUpRight = rootModel.getChild("plate_up_right");
      this.plateDown = rootModel.getChild("plate_down");
   }

   public static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition part = mesh.getRoot();
      part.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 1.0F), PartPose.ZERO);
      part.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
      part.addOrReplaceChild(
         "plate_left",
         CubeListBuilder.create().texOffs(42, 0).addBox(-2.0F, -11.0F, 0.0F, 2.0F, 22.0F, 1.0F),
         PartPose.offsetAndRotation(-6.0F, 0.0F, -2.0F, 0.0F, 0.523599F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_right",
         CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -11.0F, 0.0F, 2.0F, 22.0F, 1.0F),
         PartPose.offsetAndRotation(6.0F, 0.0F, -2.0F, 0.0F, -0.523599F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_up_left",
         CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 2.0F, 1.0F),
         PartPose.offsetAndRotation(-4.0F, -11.0F, -2.0F, -0.52359F, 0.0F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_up_right",
         CubeListBuilder.create().texOffs(54, 3).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 2.0F, 1.0F),
         PartPose.offsetAndRotation(4.0F, -11.0F, -2.0F, -0.52359F, 0.0F, 0.0F)
      );
      part.addOrReplaceChild(
         "plate_down",
         CubeListBuilder.create().texOffs(26, 12).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, 11.0F, -2.0F, 0.523599F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void renderExtraParts(
      PoseStack mStack, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha
   ) {
      super.renderExtraParts(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      this.plateLeft.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      this.plateRight.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      this.plateUpLeft.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      this.plateUpRight.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      this.plateDown.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
   }
}
