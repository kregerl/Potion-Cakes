package com.loucaskreger.potioncakes.effect;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RenderHelper {

	public static final RenderState.TransparencyState NO_TRANSPARENCY;

	public static final RenderState.LayerState PROJECTION_LAYERING;

	public static final RenderType NO_DEPTH_LINE;

	static {
		NO_TRANSPARENCY = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228510_b_");

		PROJECTION_LAYERING = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228500_J_");

		final boolean ENABLE_DEPTH_WRITING = true;
		final boolean ENABLE_COLOUR_COMPONENTS_WRITING = true;
		final RenderState.WriteMaskState WRITE_TO_DEPTH_AND_COLOR = new RenderState.WriteMaskState(ENABLE_DEPTH_WRITING,
				ENABLE_COLOUR_COMPONENTS_WRITING);

		final RenderState.DepthTestState NO_DEPTH_TEST = new RenderState.DepthTestState(GL11.GL_ALWAYS);

		final int INITIAL_BUFFER_SIZE = 128;
		final boolean AFFECTS_OUTLINE = false;

		RenderType.State renderState;
		
		renderState = RenderType.State.getBuilder().line(new RenderState.LineState(OptionalDouble.of(3.5)))
				.layer(PROJECTION_LAYERING).transparency(NO_TRANSPARENCY).writeMask(WRITE_TO_DEPTH_AND_COLOR)
				.depthTest(NO_DEPTH_TEST).build(AFFECTS_OUTLINE);
		
		
		NO_DEPTH_LINE = RenderType.makeType("no_depth_line", DefaultVertexFormats.POSITION_COLOR,
				GL11.GL_LINES, INITIAL_BUFFER_SIZE, renderState);
	}
	

}
