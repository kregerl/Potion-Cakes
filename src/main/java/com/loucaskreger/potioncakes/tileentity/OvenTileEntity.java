package com.loucaskreger.potioncakes.tileentity;

import java.util.Collections;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.loucaskreger.potioncakes.PotionCakes;
import com.loucaskreger.potioncakes.block.OvenBlock;
import com.loucaskreger.potioncakes.container.OvenContainer;
import com.loucaskreger.potioncakes.init.ModBlocks;
import com.loucaskreger.potioncakes.init.ModRecipeSerializers;
import com.loucaskreger.potioncakes.init.ModTileEntityTypes;
import com.loucaskreger.potioncakes.recipe.CookingRecipe;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.BeaconContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class OvenTileEntity extends LockableLootTileEntity
		implements ITickableTileEntity, INamedContainerProvider, IChestLid {

	private ITextComponent customName;
	public int smeltTime;
	private final int maxSmeltTime = 100;
	private OvenItemHandler inventory;

	protected float lidAngle;
	protected float prevLidAngle;
	protected int numPlayersUsing;
	private int ticksSinceSync;

	public OvenTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		this.inventory = new OvenItemHandler(11);
	}

	public OvenTileEntity() {
		this(ModTileEntityTypes.OVEN.get());
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity playerIn) {
		return new OvenContainer(windowId, playerInv, this);
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new OvenContainer(id, player, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.getName();
	}

	@Override
	public ITextComponent getName() {
		return this.customName != null ? customName : this.getDefaultName();
	}

	@Override
	public ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + PotionCakes.MOD_ID + "oven");
	}

	public void setCustomName(ITextComponent customName) {
		this.customName = customName;
	}

	@Nullable
	public ITextComponent getCustomName() {
		return this.customName;
	}

	@Override
	public void tick() {
		boolean dirty = false;
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		++this.ticksSinceSync;
		this.numPlayersUsing = calculatePlayersUsing(this.world, this, i, j, k);
		this.prevLidAngle = this.lidAngle;
		float f = 0.1F;
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
			this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float f1 = this.lidAngle;
			if (this.numPlayersUsing > 0) {
				this.lidAngle += 0.1F;
			} else {
				this.lidAngle -= 0.1F;
			}

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}

			float f2 = 0.5F;
			if (this.lidAngle < 0.5F && f1 >= 0.5F) {
				this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
		if (this.world != null && !this.world.isRemote) {
			if (this.world.isBlockPowered(this.getPos())) {
				if (this.getRecipe(this.inventory.getStackInSlot(0)) != null) {
					if (this.smeltTime != this.maxSmeltTime) {
						this.world.setBlockState(this.getPos(), this.getBlockState().with(OvenBlock.LIT, true));
						this.smeltTime++;
						dirty = true;
					} else {
						this.world.setBlockState(this.getPos(), this.getBlockState().with(OvenBlock.LIT, false));
						this.smeltTime = 0;
						ItemStack output = this.getRecipe(this.inventory.getStackInSlot(0)).getRecipeOutput();
						this.inventory.insertItem(1, output.copy(), false);
						this.inventory.decrStackSize(0, 1);
						dirty = true;
					}
				}
			}

		}
		if (dirty) {
			this.markDirty();
		}
	}

	private void playSound(SoundEvent soundIn) {
		double d0 = (double) this.pos.getX() + 0.5D;
		double d1 = (double) this.pos.getY() + 0.5D;
		double d2 = (double) this.pos.getZ() + 0.5D;

		this.world.playSound((PlayerEntity) null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F,
				this.world.rand.nextFloat() * 0.1F + 0.9F);
	}

	public static int calculatePlayersUsingSync(World p_213977_0_, LockableTileEntity p_213977_1_, int p_213977_2_,
			int p_213977_3_, int p_213977_4_, int p_213977_5_, int p_213977_6_) {
		if (!p_213977_0_.isRemote && p_213977_6_ != 0
				&& (p_213977_2_ + p_213977_3_ + p_213977_4_ + p_213977_5_) % 200 == 0) {
			p_213977_6_ = calculatePlayersUsing(p_213977_0_, p_213977_1_, p_213977_3_, p_213977_4_, p_213977_5_);
		}

		return p_213977_6_;
	}

	public static int calculatePlayersUsing(World world, LockableTileEntity p_213976_1_, int p_213976_2_,
			int p_213976_3_, int p_213976_4_) {
		int i = 0;
		float f = 5.0F;

		for (PlayerEntity playerentity : world.getEntitiesWithinAABB(PlayerEntity.class,
				new AxisAlignedBB((double) ((float) p_213976_2_ - 5.0F), (double) ((float) p_213976_3_ - 5.0F),
						(double) ((float) p_213976_4_ - 5.0F), (double) ((float) (p_213976_2_ + 1) + 5.0F),
						(double) ((float) (p_213976_3_ + 1) + 5.0F), (double) ((float) (p_213976_4_ + 1) + 5.0F)))) {
			if (playerentity.openContainer instanceof OvenContainer) {
				++i;
			}
		}

		return i;
	}

	@Nullable
	private CookingRecipe getRecipe(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		Set<IRecipe<?>> recipes = findRecipesByType(ModRecipeSerializers.COOKING_TYPE, this.world);
		for (IRecipe<?> iRecipe : recipes) {
			CookingRecipe recipe = (CookingRecipe) iRecipe;
			if (recipe.matches(new RecipeWrapper(this.inventory), this.world))
				return recipe;

		}
		return null;
	}

	private static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> cookingType, World world) {
		if (world != null) {
			return world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == cookingType)
					.collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

	private static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> cookingType) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.world != null) {
			return mc.world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == cookingType)
					.collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

	public static Set<ItemStack> getRecipeInputs(IRecipeType<?> cookingType, World world) {
		Set<ItemStack> recipeInputs = new HashSet<ItemStack>();
		Set<IRecipe<?>> recipes = findRecipesByType(cookingType, world);
		for (IRecipe<?> recipe : recipes) {
			NonNullList<Ingredient> ingredients = recipe.getIngredients();
			ingredients.forEach(ingredient -> {
				for (ItemStack stack : ingredient.getMatchingStacks()) {
					recipeInputs.add(stack);
				}
			});
		}
		return recipeInputs;
	}

	public float getLidAngle(float partialTicks) {
		return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
	}

	public final IItemHandlerModifiable getInventory() {
		return this.inventory;
	}

	public Block getBlockToUse() {
		return ModBlocks.OVEN_BLOCK.get();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		return new SUpdateTileEntityPacket(this.pos, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (compound.contains("customName", Constants.NBT.TAG_STRING)) {
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("customName"));
		}

		NonNullList<ItemStack> inventory = NonNullList.withSize(this.inventory.getSlots(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, inventory);
		this.inventory.setNonNullList(inventory);
		this.smeltTime = compound.getInt("smeltTime");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (this.customName != null) {
			compound.putString("customName", ITextComponent.Serializer.toJson(this.customName));
		}
		ItemStackHelper.saveAllItems(compound, this.inventory.toNonNullList());
		compound.putInt("smeltTime", this.smeltTime);

		return compound;
	}

	@Override
	public int getSizeInventory() {
		return 11;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory.toNonNullList();
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		this.inventory.setNonNullList(itemsIn);

	}

}
