package com.loucaskreger.potioncakes.block;

import java.util.Random;
import java.util.function.BiPredicate;
import com.loucaskreger.potioncakes.init.ModTileEntityTypes;
import com.loucaskreger.potioncakes.tileentity.OvenItemHandler;
import com.loucaskreger.potioncakes.tileentity.OvenTileEntity;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class OvenBlock extends Block {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty HAS_CAKE = BooleanProperty.create("cake");
	public static final BooleanProperty LIT = BooleanProperty.create("lit");

	public OvenBlock() {
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(4.0f, 8.0f).harvestLevel(2));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(HAS_CAKE, false)
				.with(LIT, false));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new OvenTileEntity();
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, HAS_CAKE, LIT);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getLightValue(BlockState state) {
		return state.get(LIT) ? super.getLightValue(state) : 0;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return Block.makeCuboidShape(1.0F, 0.0D, 1.0F, 16.0D, 15.5D, 16.0D);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof OvenTileEntity) {

			if (stack.hasDisplayName()) {
				((OvenTileEntity) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Override
	// onlyIn dist.client?
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.animateTick(stateIn, worldIn, pos, rand);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote && worldIn != null) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			System.out.println("Here");
			System.out.println("Pos: " + pos.toString());
			System.out.println("Tile: " + tileEntity.toString());
			if (tileEntity instanceof OvenTileEntity) {
				System.out.println("Here2");
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof OvenTileEntity) {
			OvenTileEntity oven = (OvenTileEntity) tileEntity;
			((OvenItemHandler) oven.getInventory()).toNonNullList().forEach(item -> {
				ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), item);
				worldIn.addEntity(itemEntity);
			});

		}
		if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
			worldIn.removeTileEntity(pos);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static TileEntityMerger.ICallback<OvenTileEntity, Float2FloatFunction> getLid(final IChestLid p_226917_0_) {
		return new TileEntityMerger.ICallback<OvenTileEntity, Float2FloatFunction>() {
			@Override
			public Float2FloatFunction func_225539_a_(OvenTileEntity p_225539_1_, OvenTileEntity p_225539_2_) {
				return (p_226921_2_) -> {
					return Math.max(p_225539_1_.getLidAngle(p_226921_2_), p_225539_2_.getLidAngle(p_226921_2_));
				};
			}

			@Override
			public Float2FloatFunction func_225538_a_(OvenTileEntity p_225538_1_) {
				return p_225538_1_::getLidAngle;
			}

			@Override
			public Float2FloatFunction func_225537_b_() {
				return p_226917_0_::getLidAngle;
			}
		};
	}

	private static boolean isBlocked(IWorld iWorld, BlockPos blockPos) {
		BlockPos blockpos = blockPos.up();
		return iWorld.getBlockState(blockpos).isNormalCube(iWorld, blockpos);
	}

	public TileEntityMerger.ICallbackWrapper<? extends OvenTileEntity> getWrapper(BlockState blockState, World world,
			BlockPos blockPos, boolean p_225536_4_) {
		BiPredicate<IWorld, BlockPos> biPredicate;
		if (p_225536_4_) {
			biPredicate = (p_226918_0_, p_226918_1_) -> false;
		} else {
			biPredicate = OvenBlock::isBlocked;
		}

		return TileEntityMerger.func_226924_a_(ModTileEntityTypes.OVEN.get(), OvenBlock::getMergerType,
				OvenBlock::getDirectionToAttached, FACING, blockState, world, blockPos, biPredicate);
	}

	public static TileEntityMerger.Type getMergerType(BlockState blockState) {
		return TileEntityMerger.Type.SINGLE;
	}

	public static Direction getDirectionToAttached(BlockState state) {
		Direction direction = state.get(FACING);
		return direction.rotateYCCW();
	}

//	@Override
//	public TileEntity createNewTileEntity(IBlockReader worldIn) {
//		return new OvenTileEntity();
//	}

}
