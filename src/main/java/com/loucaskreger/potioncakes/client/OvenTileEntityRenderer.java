package com.loucaskreger.potioncakes.client;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.block.OvenBlock;

import com.loucaskreger.potioncakes.tileentity.OvenTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class OvenTileEntityRenderer<T extends TileEntity & IChestLid> extends TileEntityRenderer<T> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(PotionCakes.MOD_ID, "model/oven_block");
	private final ModelRenderer oven;
	private final ModelRenderer door;
	private final ModelRenderer doorOutline;
	private final ModelRenderer stovetop;
	private final ModelRenderer body;
	private final ModelRenderer rack;

	public OvenTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		int textureWidth = 80;
		int textureHeight = 80;

		oven = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		oven.setRotationPoint(0.0F, 16.0F, 0.0F);

		door = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		door.setRotationPoint(0.0F, 6.5F, -6.0F);
		oven.addChild(door);
		door.setTextureOffset(46, 0).addBox(-5.0F, -8.5F, -1.0F, 10.0F, 7.0F, 1.0F, 0.0F, false);
		door.setTextureOffset(54, 43).addBox(-5.0F, -10.5F, -2.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);

		doorOutline = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		doorOutline.setRotationPoint(0.0F, -0.5F, 0.3333F);
		door.addChild(doorOutline);
		doorOutline.setTextureOffset(46, 8).addBox(-6.0F, -11.0F, -1.3333F, 12.0F, 3.0F, 1.0F, 0.0F, false);
		doorOutline.setTextureOffset(4, 4).addBox(5.0F, -8.0F, -1.3333F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		doorOutline.setTextureOffset(46, 12).addBox(-6.0F, -1.0F, -1.3333F, 12.0F, 1.0F, 1.0F, 0.0F, false);
		doorOutline.setTextureOffset(0, 0).addBox(-6.0F, -8.0F, -1.3333F, 1.0F, 7.0F, 1.0F, 0.0F, false);

		stovetop = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		stovetop.setRotationPoint(0.0F, 0.0F, 0.0F);
		oven.addChild(stovetop);
		stovetop.setTextureOffset(54, 45).addBox(2.0F, -7.5F, -4.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		stovetop.setTextureOffset(0, 53).addBox(2.0F, -7.5F, 2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		stovetop.setTextureOffset(0, 48).addBox(-6.0F, -7.5F, -4.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		stovetop.setTextureOffset(0, 43).addBox(-6.0F, -7.5F, 2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

		body = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		oven.addChild(body);
		body.setTextureOffset(0, 0).addBox(-8.0F, 7.0F, -6.0F, 16.0F, 1.0F, 14.0F, 0.0F, false);
		body.setTextureOffset(20, 29).addBox(5.0F, -7.0F, -6.0F, 3.0F, 14.0F, 14.0F, 0.0F, false);
		body.setTextureOffset(0, 15).addBox(-8.0F, -7.0F, -6.0F, 3.0F, 14.0F, 14.0F, 0.0F, false);
		body.setTextureOffset(52, 15).addBox(-5.0F, -7.0F, -6.0F, 10.0F, 2.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(40, 28).addBox(-5.0F, -7.0F, 7.0F, 10.0F, 14.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(20, 15).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 1.0F, 12.0F, 0.0F, false);
		body.setTextureOffset(54, 54).addBox(-5.0F, 6.0F, -6.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);

		rack = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		rack.setRotationPoint(-12.0F, -2.0F, 12.0F);
		oven.addChild(rack);
		rack.setTextureOffset(52, 24).addBox(7.0F, 7.0F, -14.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		rack.setTextureOffset(52, 22).addBox(7.0F, 7.0F, -17.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		rack.setTextureOffset(52, 20).addBox(7.0F, 7.0F, -11.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		rack.setTextureOffset(52, 18).addBox(7.0F, 7.0F, -8.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
//		super(rendererDispatcherIn);
//		int textureWidth = 80;
//		int textureHeight = 80;
//
//		oven = new ModelRenderer(textureWidth, textureHeight, 0, 0);
//		oven.setRotationPoint(0.0F, 16.0F, 0.0F);
//		
//
//		door = new ModelRenderer(textureWidth, textureHeight, 0, 0);
//		door.setRotationPoint(0.0F, 6.5F, -6.0F);
//		oven.addChild(door);
//		door.setTextureOffset(46, 0).addBox(-5.0F, -8.5F, -1.0F, 10.0F, 7.0F, 1.0F, 0.0F, false);
//		door.setTextureOffset(54, 43).addBox(-5.0F, -10.5F, -2.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
//
//		doorOutline = new ModelRenderer(textureWidth, textureHeight, 0, 0);
//		doorOutline.setRotationPoint(0.0F, -0.5F, 0.3333F);
//		door.addChild(doorOutline);
//		doorOutline.setTextureOffset(46, 8).addBox(-6.0F, -11.0F, -1.3333F, 12.0F, 3.0F, 1.0F, 0.0F, false);
//		doorOutline.setTextureOffset(0, 15).addBox(5.0F, -8.0F, -1.3333F, 1.0F, 7.0F, 1.0F, 0.0F, false);
//		doorOutline.setTextureOffset(46, 12).addBox(-6.0F, -1.0F, -1.3333F, 12.0F, 1.0F, 1.0F, 0.0F, false);
//		doorOutline.setTextureOffset(0, 0).addBox(-6.0F, -8.0F, -1.3333F, 1.0F, 7.0F, 1.0F, 0.0F, false);
//
//		stovetop = new ModelRenderer(textureWidth, textureHeight, 0, 0);
//		stovetop.setRotationPoint(0.0F, 0.0F, 0.0F);
//		oven.addChild(stovetop);
//		stovetop.setTextureOffset(0, 15).addBox(2.0F, -7.5F, -4.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
//		stovetop.setTextureOffset(0, 8).addBox(2.0F, -7.5F, 2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
//		stovetop.setTextureOffset(0, 4).addBox(-6.0F, -7.5F, -4.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
//		stovetop.setTextureOffset(0, 0).addBox(-6.0F, -7.5F, 2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
//
//		body = new ModelRenderer(textureWidth, textureHeight, 0, 0);
//		body.setRotationPoint(0.0F, 0.0F, 0.0F);
//		oven.addChild(body);
//		body.setTextureOffset(0, 0).addBox(-8.0F, 7.0F, -6.0F, 16.0F, 1.0F, 14.0F, 0.0F, false);
//		body.setTextureOffset(20, 29).addBox(5.0F, -7.0F, -6.0F, 3.0F, 14.0F, 14.0F, 0.0F, false);
//		body.setTextureOffset(0, 15).addBox(-8.0F, -7.0F, -6.0F, 3.0F, 14.0F, 14.0F, 0.0F, false);
//		body.setTextureOffset(52, 15).addBox(-5.0F, -7.0F, -6.0F, 10.0F, 2.0F, 1.0F, 0.0F, false);
//		body.setTextureOffset(40, 28).addBox(-5.0F, -7.0F, 7.0F, 10.0F, 14.0F, 1.0F, 0.0F, false);
//		body.setTextureOffset(20, 15).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 1.0F, 12.0F, 0.0F, false);
//		body.setTextureOffset(54, 54).addBox(-5.0F, 6.0F, -6.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
//
//		rack = new ModelRenderer(textureWidth, textureHeight, 0, 0);
//		rack.setRotationPoint(-12.0F, -2.0F, 12.0F);
//		oven.addChild(rack);
//		rack.setTextureOffset(52, 24).addBox(7.0F, 7.0F, -14.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
//		rack.setTextureOffset(52, 22).addBox(7.0F, 7.0F, -17.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
//		rack.setTextureOffset(52, 20).addBox(7.0F, 7.0F, -11.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
//		rack.setTextureOffset(52, 18).addBox(7.0F, 7.0F, -8.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {

		OvenTileEntity tileEntity = (OvenTileEntity) tileEntityIn;

		World world = tileEntity.getWorld();
		boolean flag = world != null;

		BlockState blockstate = flag ? tileEntity.getBlockState()
				: (BlockState) tileEntity.getBlockToUse().getDefaultState().with(OvenBlock.FACING,
						Direction.SOUTH);
		Block block = blockstate.getBlock();
//		IronChestsTypes chestType = IronChestsTypes.IRON;
//		IronChestsTypes actualType = GenericIronChestBlock.getTypeFromBlock(block);
//
//		if (actualType != null) {
//			chestType = actualType;
//		}

		if (block instanceof OvenBlock) {
			OvenBlock ironChestBlock = (OvenBlock) block;

			matrixStackIn.push();
			float f = blockstate.get(OvenBlock.FACING).getHorizontalAngle();
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
			matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

			TileEntityMerger.ICallbackWrapper<? extends OvenTileEntity> iCallbackWrapper;
			if (flag) {
				iCallbackWrapper = ironChestBlock.getWrapper(blockstate, world, tileEntity.getPos(), true);
			} else {
				iCallbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
			}

			float f1 = iCallbackWrapper.apply(OvenBlock.getLid((IChestLid) tileEntity)).get(partialTicks);
			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			int i = iCallbackWrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);

			Material material = new Material(Atlases.CHEST_ATLAS, TEXTURE);
			IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);

			this.handleModelRender(matrixStackIn, ivertexbuilder, this.door, this.body, this.rack, f1,
					i, combinedOverlayIn);

			matrixStackIn.pop();
		}

//		OvenTileEntity tileEntity = (OvenTileEntity) tileEntityIn;
//
//		World world = tileEntity.getWorld();
//		boolean flag = world != null;
//
//		BlockState blockstate = flag ? tileEntity.getBlockState()
//				: (BlockState) tileEntity.getBlockToUse().getDefaultState().with(OvenBlock.FACING,
//						Direction.SOUTH);
//		Block block = blockstate.getBlock();
//
//		if (block instanceof OvenBlock) {
//			OvenBlock ironChestBlock = (OvenBlock) block;
//
//			matrixStackIn.push();
//			float f = blockstate.get(OvenBlock.FACING).getHorizontalAngle();
//			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
//			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
//			matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
//
//			TileEntityMerger.ICallbackWrapper<? extends OvenTileEntity> iCallbackWrapper;
//			if (flag) {
//				iCallbackWrapper = ironChestBlock.getWrapper(blockstate, world, tileEntity.getPos(), true);
//			} else {
//				iCallbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
//			}
//
//			float f1 = iCallbackWrapper.apply(OvenBlock.getLid((IChestLid) tileEntity)).get(partialTicks);
//			f1 = 1.0F - f1;
//			f1 = 1.0F - f1 * f1 * f1;
//			int i = iCallbackWrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);
//
//			Material material = new Material(Atlases.CHEST_ATLAS, TEXTURE);
//			IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);
//
//			this.handleModelRender(matrixStackIn, ivertexbuilder,this.door, this.body, this.rack, this.stovetop, f1,
//					i, combinedOverlayIn);
//
//			matrixStackIn.pop();
//		}

		/**
		 * if (actualType != null) { chestType = actualType; }
		 * 
		 * if (block instanceof GenericIronChestBlock) { GenericIronChestBlock
		 * ironChestBlock = (GenericIronChestBlock) block;
		 * 
		 * matrixStackIn.push(); float f =
		 * blockstate.get(GenericIronChestBlock.FACING).getHorizontalAngle();
		 * matrixStackIn.translate(0.5D, 0.5D, 0.5D);
		 * matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
		 * matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
		 * TileEntityMerger.ICallbackWrapper<? extends GenericIronChestTileEntity>
		 * iCallbackWrapper; if (flag) { iCallbackWrapper =
		 * ironChestBlock.getWrapper(blockstate, world, tileEntity.getPos(), true); }
		 * else { iCallbackWrapper = TileEntityMerger.ICallback::func_225537_b_; }
		 * 
		 * float f1 = iCallbackWrapper.apply(GenericIronChestBlock.getLid((IChestLid)
		 * tileEntity)).get(partialTicks); f1 = 1.0F - f1; f1 = 1.0F - f1 * f1 * f1; int
		 * i = iCallbackWrapper.apply(new
		 * DualBrightnessCallback<>()).applyAsInt(combinedLightIn);
		 * 
		 * RenderMaterial material = new RenderMaterial(Atlases.CHEST_ATLAS,
		 * IronChestsModels.chooseChestTexture(chestType)); IVertexBuilder
		 * ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);
		 * 
		 * this.handleModelRender(matrixStackIn, ivertexbuilder, this.door, this.body,
		 * this.rack, this.stovetop, f1, i, combinedOverlayIn);
		 * 
		 * matrixStackIn.pop();
		 **/
//		OvenTileEntity tileEntity = (OvenTileEntity) tileEntityIn;
//
//		World world = tileEntity.getWorld();
//		boolean flag = world != null;
//
//		BlockState blockstate = flag ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().getDefaultState().with(GenericIronChestBlock.FACING,
//						Direction.SOUTH);
//		Block block = blockstate.getBlock();
////		IronChestsTypes chestType = IronChestsTypes.IRON;
////		IronChestsTypes actualType = GenericIronChestBlock.getTypeFromBlock(block);
//
//		if (actualType != null) {
//			chestType = actualType;
//		}
//
//		if (block instanceof GenericIronChestBlock) {
//			GenericIronChestBlock ironChestBlock = (GenericIronChestBlock) block;
//
//			matrixStackIn.push();
//			float f = blockstate.get(GenericIronChestBlock.FACING).getHorizontalAngle();
//			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
//			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
//			matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
//			TileEntityMerger.ICallbackWrapper<? extends OvenTileEntity> iCallbackWrapper;
//			if (flag) {
//				iCallbackWrapper = ironChestBlock.getWrapper(blockstate, world, tileEntity.getPos(), true);
//			} else {
//				iCallbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
//			}
//
//			float f1 = iCallbackWrapper.apply(GenericIronChestBlock.getLid((IChestLid) tileEntity)).get(partialTicks);
//			f1 = 1.0F - f1;
//			f1 = 1.0F - f1 * f1 * f1;
//			int i = iCallbackWrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);
//
//			RenderMaterial material = new RenderMaterial(Atlases.CHEST_ATLAS,
//					IronChestsModels.chooseChestTexture(chestType));
//			IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);
//
//			this.handleModelRender(matrixStackIn, ivertexbuilder, this.door, this.body, this.rack, this.stovetop, f1, i,
//					combinedOverlayIn);
//
//			matrixStackIn.pop();
//		}
//	}

	}

	private void handleModelRender(MatrixStack matrixStackIn, IVertexBuilder iVertexBuilder, ModelRenderer firstModel,
			ModelRenderer secondModel, ModelRenderer thirdModel, float f1, int p_228871_7_,
			int p_228871_8_) {
		firstModel.rotateAngleX = (f1 * ((float) Math.PI / 2F));
		matrixStackIn.translate(0.5F, 0.5F, 0.5F);
		matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
		firstModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
		secondModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
		thirdModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
//		fourthModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
	}
}
