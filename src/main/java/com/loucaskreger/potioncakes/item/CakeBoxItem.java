package com.loucaskreger.potioncakes.item;

import java.util.List;

import javax.annotation.Nullable;

import com.loucaskreger.potioncakes.block.PotionCakeBlock;
import com.loucaskreger.potioncakes.capability.CakeHolderCapability;
import com.loucaskreger.potioncakes.capability.ICakeHolder;
import com.loucaskreger.potioncakes.init.ModBlocks;
import com.loucaskreger.potioncakes.tileentity.PotionCakeTileEntity;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class CakeBoxItem extends ModItem {

	public CakeBoxItem() {
		super(new Item.Properties().maxStackSize(1));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new CakeHolderCapability();
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		LazyOptional<ICakeHolder> cap = stack.getCapability(CakeHolderCapability.CAKE_HOLDER_CAPABILITY);
		ICakeHolder handler = cap.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));

		World worldIn = context.getWorld();

		BlockPos pos = context.getPos();
		BlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		if (handler.getSlices() == -1) {
			if (!worldIn.isRemote) {

				if (block instanceof CakeBlock) {
					if (block instanceof PotionCakeBlock) {
						PotionCakeTileEntity tile = ((PotionCakeTileEntity) worldIn.getTileEntity(pos));
						handler.setEffects(tile.getEffects());
						PotionUtils.appendEffects(stack, tile.getEffects());
					}
					handler.setSlices(
							((CakeBlock) block).getShape(state, worldIn, pos, null).getStart(Direction.Axis.X));
					worldIn.removeBlock(pos, false);
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		ItemStack stack = context.getItem();
		LazyOptional<ICakeHolder> cap = stack.getCapability(CakeHolderCapability.CAKE_HOLDER_CAPABILITY);
		ICakeHolder handler = cap.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));

		if (handler.getSlices() > -1) {
			int slices = handler.getSlices();
			ActionResultType type = tryPlace(context, slices, handler.getEffects());
			if (type == ActionResultType.SUCCESS) {
				stack.removeChildTag("CustomPotionEffects");
				handler.setSlices(-1);
				handler.clearEffects();
			} else {
				handler.setSlices(slices);
			}
			return type;
		}
		return ActionResultType.PASS;
	}

	public ActionResultType tryPlace(ItemUseContext context, int numSlices, List<EffectInstance> effects) {
		BlockItemUseContext blockitemusecontext = new BlockItemUseContext(context);
		if (!blockitemusecontext.canPlace()) {
			return ActionResultType.FAIL;
		} else {
			BlockState blockstate = this.getStateForPlacement(blockitemusecontext, effects);
			if (blockstate == null) {
				return ActionResultType.FAIL;
			} else if (!this.placeBlock(blockitemusecontext,
					blockstate.with(BlockStateProperties.BITES_0_6, numSlices))) {
				return ActionResultType.FAIL;
			} else {
				BlockPos blockpos = blockitemusecontext.getPos();
				World world = blockitemusecontext.getWorld();
				PlayerEntity playerentity = blockitemusecontext.getPlayer();
				ItemStack itemstack = blockitemusecontext.getItem();
				BlockState blockstate1 = world.getBlockState(blockpos);
				Block block = blockstate1.getBlock();
				if (block == blockstate.getBlock()) {
					blockstate1 = this.func_219985_a(blockpos, world, itemstack, blockstate1);
					this.onBlockPlaced(blockpos, world, playerentity, itemstack, blockstate1);
					block.onBlockPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
					if (playerentity instanceof ServerPlayerEntity) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerentity, blockpos, itemstack);
					}
				}

				SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
				world.playSound(playerentity, blockpos,
						this.getPlaceSound(blockstate1, world, blockpos, context.getPlayer()), SoundCategory.BLOCKS,
						(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				return ActionResultType.SUCCESS;
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//		ItemStack originalStack = stack.copy();
		ITextComponent text = new TranslationTextComponent("press.shift").applyTextStyle(TextFormatting.GRAY);
		tooltip.add(text);
		if (Screen.hasShiftDown()) {
			tooltip.remove(text);
			LazyOptional<ICakeHolder> cap = stack.getCapability(CakeHolderCapability.CAKE_HOLDER_CAPABILITY);
			ICakeHolder handler = cap.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
			int slices = handler.getSlices();
			if (slices < 0) {
				tooltip.add(new StringTextComponent("Empty").applyTextStyle(TextFormatting.GRAY));
			} else {
				tooltip.add(
						new StringTextComponent("Slices: " + Math.abs(slices - 7)).applyTextStyle(TextFormatting.GRAY));
				PotionUtils.addPotionTooltip(stack, tooltip, 1.0f);
			}

		}

	}

	protected SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
		return state.getSoundType(world, pos, entity).getPlaceSound();
	}

	protected BlockState getStateForPlacement(BlockItemUseContext context, List<EffectInstance> effects) {
		BlockState blockstate = !effects.isEmpty() ? ModBlocks.POTION_CAKE_BLOCK.get().getStateForPlacement(context)
				: Blocks.CAKE.getStateForPlacement(context);
		return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
	}

	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		return context.getWorld().setBlockState(context.getPos(), state, 11);
	}

	protected boolean canPlace(BlockItemUseContext context, BlockState blockStateIn) {
		PlayerEntity playerentity = context.getPlayer();
		ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.dummy()
				: ISelectionContext.forEntity(playerentity);
		return (!true || blockStateIn.isValidPosition(context.getWorld(), context.getPos()))
				&& context.getWorld().placedBlockWouldCollide(blockStateIn, context.getPos(), iselectioncontext);
	}

	private BlockState func_219985_a(BlockPos p_219985_1_, World p_219985_2_, ItemStack p_219985_3_,
			BlockState p_219985_4_) {
		BlockState blockstate = p_219985_4_;
		CompoundNBT compoundnbt = p_219985_3_.getTag();
		if (compoundnbt != null) {
			CompoundNBT compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
			StateContainer<Block, BlockState> statecontainer = p_219985_4_.getBlock().getStateContainer();

			for (String s : compoundnbt1.keySet()) {
				IProperty<?> iproperty = statecontainer.getProperty(s);
				if (iproperty != null) {
					String s1 = compoundnbt1.get(s).getString();
					blockstate = func_219988_a(blockstate, iproperty, s1);
				}
			}
		}

		if (blockstate != p_219985_4_) {
			p_219985_2_.setBlockState(p_219985_1_, blockstate, 2);
		}

		return blockstate;
	}

	private static <T extends Comparable<T>> BlockState func_219988_a(BlockState p_219988_0_, IProperty<T> p_219988_1_,
			String p_219988_2_) {
		return p_219988_1_.parseValue(p_219988_2_).map((p_219986_2_) -> {
			return p_219988_0_.with(p_219988_1_, p_219986_2_);
		}).orElse(p_219988_0_);
	}

	protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack,
			BlockState state) {
		return setTileEntityNBT(worldIn, player, pos, stack);
	}

	public static boolean setTileEntityNBT(World worldIn, @Nullable PlayerEntity player, BlockPos pos,
			ItemStack stackIn) {
		MinecraftServer minecraftserver = worldIn.getServer();
		if (minecraftserver == null) {
			return false;
		} else {
			CompoundNBT compoundnbt = stackIn.getChildTag("BlockEntityTag");
			if (compoundnbt != null) {
				TileEntity tileentity = worldIn.getTileEntity(pos);
				if (tileentity != null) {
					if (!worldIn.isRemote && tileentity.onlyOpsCanSetNbt()
							&& (player == null || !player.canUseCommandBlock())) {
						return false;
					}

					CompoundNBT compoundnbt1 = tileentity.write(new CompoundNBT());
					CompoundNBT compoundnbt2 = compoundnbt1.copy();
					compoundnbt1.merge(compoundnbt);
					compoundnbt1.putInt("x", pos.getX());
					compoundnbt1.putInt("y", pos.getY());
					compoundnbt1.putInt("z", pos.getZ());
					if (!compoundnbt1.equals(compoundnbt2)) {
						tileentity.read(compoundnbt1);
						tileentity.markDirty();
						return true;
					}
				}
			}

			return false;
		}
	}
}
