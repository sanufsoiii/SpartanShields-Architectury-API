package com.oblivioussp.spartanshields.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ShieldBaseModel extends Model {
   protected static final String PART_PLATE = "plate";
   protected static final String PART_HANDLE = "handle";
   protected ModelPart root;
   protected ModelPart plate;
   protected ModelPart handle;

   public ShieldBaseModel(Function<ResourceLocation, RenderType> renderType, ModelPart rootModel) {
      super(renderType);
      this.root = rootModel;
      this.plate = rootModel.getChild("plate");
      this.handle = rootModel.getChild("handle");
   }

   public ShieldBaseModel(ModelPart rootModel) {
      this(RenderType::entitySolid, rootModel);
   }

   public static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition part = mesh.getRoot();
      part.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 1.0F), PartPose.ZERO);
      part.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
      return LayerDefinition.create(mesh, 64, 64);
   }

   public ModelPart getPlate() {
      return this.plate;
   }

   public void renderExtraParts(
      PoseStack mStack, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, int color
   ) {
      this.handle.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, color);
   }

   public void renderLayers(
      PoseStack mStack,
      MultiBufferSource bufferIn,
      RenderType renderTypeIn,
      int packedLightIn,
      int packedOverlayIn,
      int color
   ) {
   }

   @Override
   public void renderToBuffer(
      PoseStack mStack, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, int color
   ) {
      this.root.render(mStack, vertexBuilder, packedLightIn, packedOverlayIn, color);
   }
}
