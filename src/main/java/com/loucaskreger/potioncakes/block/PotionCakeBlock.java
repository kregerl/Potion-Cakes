package com.loucaskreger.potioncakes.block;

import java.util.List;
import javax.annotation.Nullable;
import com.loucaskreger.potioncakes.tileentity.PotionCakeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PotionCakeBlock extends CakeBlock {

	public PotionCakeBlock() {
		super(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH).notSolid());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PotionCakeTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (player.canEat(false)) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof PotionCakeTileEntity) {
				for (EffectInstance e : ((PotionCakeTileEntity) te).getEffects()) {
					player.addPotionEffect(e);
				}
			}
		}
		return super.onBlockActivated(state, world, pos, player, handIn, hit);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof PotionCakeTileEntity) {
			((PotionCakeTileEntity) te).setEffectsAndColor(PotionUtils.getEffectsFromStack(stack));

		}
	}

}
