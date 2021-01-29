package com.loucaskreger.potioncakes.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.effect.RenderHelper;
import com.loucaskreger.potioncakes.effect.SpelunkerEffect;
import com.loucaskreger.potioncakes.init.ModEffects;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PotionCakes.MOD_ID, value = Dist.CLIENT)
public class RenderWireframe {

	private static final Vec3d[] LOWER_BASE_VERTICES = { new Vec3d(0, 0, 0), new Vec3d(1, 0, 0), new Vec3d(1, 0, 1),
			new Vec3d(0, 0, 1), };

	private static final Vec3d[] BASE_VERTICES = { new Vec3d(0, 1, 0), new Vec3d(1, 1, 0), new Vec3d(1, 1, 1),
			new Vec3d(0, 1, 1), };

	private static Map<Block, SpelunkerEffect.Color> validOres = new HashMap<Block, SpelunkerEffect.Color>();
	static {
		validOres.put(Blocks.COAL_ORE, new SpelunkerEffect.Color(0.203f, 0.203f, 0.203f, 1f));
		validOres.put(Blocks.IRON_ORE, new SpelunkerEffect.Color(0.886f, 0.752f, 0.666f, 1f));
		validOres.put(Blocks.LAPIS_ORE, new SpelunkerEffect.Color(0.062f, 0.266f, 0.674f, 1f));
		validOres.put(Blocks.GOLD_ORE, new SpelunkerEffect.Color(0.972f, 0.686f, 0.168f, 1f));
		validOres.put(Blocks.REDSTONE_ORE, new SpelunkerEffect.Color(0.592f, 0.011f, 0.011f, 1f));
		validOres.put(Blocks.EMERALD_ORE, new SpelunkerEffect.Color(0.090f, 0.866f, 0.384f, 1f));
		validOres.put(Blocks.DIAMOND_ORE, new SpelunkerEffect.Color(0.396f, 0.960f, 0.890f, 1f));
		validOres.put(Blocks.NETHER_QUARTZ_ORE, new SpelunkerEffect.Color(0.921f, 0.921f, 0.921f, 1f));
		validOres = Collections.unmodifiableMap(validOres);
	}

	@SubscribeEvent
	public static void render(final RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getInstance();

		World world = mc.world;
		PlayerEntity player = mc.player;
		EffectInstance effectInstance = player.getActivePotionEffect(ModEffects.SPELUNKER_EFFECT.get());
		boolean hasEffect = effectInstance != null;
		IRenderTypeBuffer buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		IVertexBuilder builder = buffer.getBuffer(RenderHelper.NO_DEPTH_LINE);
		MatrixStack matrixStack = event.getMatrixStack();

		Vec3d projectedView = mc.gameRenderer.getActiveRenderInfo().getProjectedView();

		if (hasEffect) {
			int amp = effectInstance.getAmplifier() == 1 ? 2 : 0;
			BlockPos playerPos = player.getPosition();
			BlockPos positiveCorner = playerPos.add(5 + amp, 3, 5 + amp);
			BlockPos negativeCorner = playerPos.add(-5 - amp, -2, -5 - amp);

			Stream<BlockPos> blocks = BlockPos.getAllInBox(positiveCorner, negativeCorner)
					.filter(bPos -> validOres.containsKey(getBlockFromPos(world, bPos)));

			Iterator<BlockPos> it = blocks.iterator();
			while (it.hasNext()) {
				BlockPos pos = it.next();
				drawWireFrame(matrixStack, builder, projectedView, validOres.get(getBlockFromPos(world, pos)), pos);
			}
		}

		RenderSystem.disableDepthTest();

		((Impl) buffer).finish();

	}

	private static Block getBlockFromPos(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock();
	}

	private static void drawWireFrame(MatrixStack matrixStack, IVertexBuilder builder, Vec3d projectedView,
			SpelunkerEffect.Color color, BlockPos pos) {
		matrixStack.push();

		matrixStack.translate(-projectedView.x + pos.getX(), -projectedView.y + pos.getY(),
				-projectedView.z + pos.getZ());

		Matrix4f matrix = matrixStack.getLast().getMatrix();
		for (int i = 1; i < BASE_VERTICES.length; ++i) {
			drawLine(matrix, builder, color, BASE_VERTICES[i - 1], BASE_VERTICES[i]);
			drawLine(matrix, builder, color, LOWER_BASE_VERTICES[i - 1], LOWER_BASE_VERTICES[i]);
			drawLine(matrix, builder, color, LOWER_BASE_VERTICES[i], BASE_VERTICES[i]);
		}

		drawLine(matrix, builder, color, BASE_VERTICES[BASE_VERTICES.length - 1], BASE_VERTICES[0]);
		drawLine(matrix, builder, color, LOWER_BASE_VERTICES[LOWER_BASE_VERTICES.length - 1], LOWER_BASE_VERTICES[0]);
		drawLine(matrix, builder, color, LOWER_BASE_VERTICES[0], BASE_VERTICES[0]);

		matrixStack.pop();
	}

	private static void drawLine(Matrix4f matrixPos, IVertexBuilder renderBuffer, SpelunkerEffect.Color color,
			Vec3d startVertex, Vec3d endVertex) {

		renderBuffer.pos(matrixPos, (float) startVertex.getX(), (float) startVertex.getY(), (float) startVertex.getZ())
				.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();

		renderBuffer.pos(matrixPos, (float) endVertex.getX(), (float) endVertex.getY(), (float) endVertex.getZ())
				.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
	}

}

class DistanceComparator implements Comparator<BlockPos> {
	private BlockPos playerPos;

	public DistanceComparator(BlockPos pos) {
		this.playerPos = pos;
	}

	@Override
	public int compare(BlockPos o1, BlockPos o2) {
		return Double.compare(distanceTo(o1), distanceTo(o1));
	}

	private double distanceTo(BlockPos orePos) {
		return Math.sqrt(Math.pow((orePos.getX() - playerPos.getX()), 2)
				+ Math.pow((orePos.getY() - playerPos.getY()), 2) + Math.pow((orePos.getX() - playerPos.getX()), 2));
	}

}