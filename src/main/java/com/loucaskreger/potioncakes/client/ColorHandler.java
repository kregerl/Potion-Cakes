package com.loucaskreger.potioncakes.client;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.init.ModBlocks;
import com.loucaskreger.potioncakes.tileentity.PotionCakeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PotionCakes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorHandler {

	@SubscribeEvent
	public static void registerBlockColor(ColorHandlerEvent.Block event) {
		final IBlockColor blockColor = (state, access, pos, tintIndex) -> {
			if (tintIndex == 1) {
				Minecraft mc = Minecraft.getInstance();
				TileEntity te = mc.world.getTileEntity(pos);
				if (te instanceof PotionCakeTileEntity) {
					return ((PotionCakeTileEntity) te).getColor();
				}
			}
			return 0xFFFFFF;

		};
		event.getBlockColors().register(blockColor, ModBlocks.POTION_CAKE_BLOCK.get());
	}

}
