package com.loucaskreger.potioncakes.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class InfusedMilkItem extends ModItem {

	public InfusedMilkItem() {
		super(new Item.Properties().maxStackSize(1));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.setActiveHand(handIn);
		return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
		if (playerentity instanceof ServerPlayerEntity) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) playerentity, stack);
		}

		if (!worldIn.isRemote) {
			for (EffectInstance effectinstance : /* handler.getEffects() */ PotionUtils.getEffectsFromStack(stack)) {
				if (effectinstance.getPotion().isInstant()) {
					effectinstance.getPotion().affectEntity(playerentity, playerentity, entityLiving,
							effectinstance.getAmplifier(), 1.0D);
				} else {
					entityLiving.addPotionEffect(new EffectInstance(effectinstance));
				}
			}
		}

		if (playerentity != null) {
			playerentity.addStat(Stats.ITEM_USED.get(this));
			if (!playerentity.abilities.isCreativeMode) {
				stack.shrink(1);
			}
		}

		if (playerentity == null || !playerentity.abilities.isCreativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.BUCKET);
			}

			if (playerentity != null) {
				playerentity.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
			}
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
//		ITextComponent text = new TranslationTextComponent("press.shift").applyTextStyle(TextFormatting.GRAY);
//		tooltip.add(text);
//		if (Screen.hasShiftDown()) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
//			tooltip.remove(text);
//		}
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			for (Potion potion : Registry.POTION) {
				if (potion != Potions.EMPTY) {
					items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potion));
				}
			}
		}

	}
}
