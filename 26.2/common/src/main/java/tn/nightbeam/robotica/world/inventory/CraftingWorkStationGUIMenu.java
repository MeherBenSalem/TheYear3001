package tn.nightbeam.robotica.world.inventory;

import tn.nightbeam.robotica.block.entity.CraftingWorkstationBlockEntity;
import tn.nightbeam.robotica.init.MenuAccessor;
import tn.nightbeam.robotica.init.RoboticaModMenus;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class CraftingWorkStationGUIMenu extends AbstractContainerMenu implements MenuAccessor {
	public final Map<String, Object> menuState = new HashMap<>() {
		@Override
		public Object put(String key, Object value) {
			if (!this.containsKey(key) && this.size() >= 24)
				return null;
			return super.put(key, value);
		}
	};
	public final Level world;
	public final Player entity;
	public int x, y, z;
	private ContainerLevelAccess access = ContainerLevelAccess.NULL;
	private Container internal;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private BlockEntity boundBlockEntity = null;

	public CraftingWorkStationGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super((MenuType) RoboticaModMenus.CRAFTING_WORK_STATION_GUI.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.internal = new SimpleContainer(21);
		BlockPos pos = null;
		if (extraData != null) {
			pos = extraData.readBlockPos();
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}
		initFromPosition(pos, extraData);
		addContainerSlots();
		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 0 + 8 + sj * 18, 0 + 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 0 + 8 + si * 18, 0 + 142));
	}

	public CraftingWorkStationGUIMenu(int id, Inventory inv, BlockPos pos) {
		super((MenuType) RoboticaModMenus.CRAFTING_WORK_STATION_GUI.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.internal = new SimpleContainer(21);
		if (pos != null) {
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}
		initFromPosition(pos, null);
		addContainerSlots();
		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 0 + 8 + sj * 18, 0 + 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 0 + 8 + si * 18, 0 + 142));
	}

	private void initFromPosition(BlockPos pos, FriendlyByteBuf extraData) {
		if (pos != null && extraData != null) {
			if (extraData.readableBytes() == 1) {
				byte hand = extraData.readByte();
				ItemStack itemstack = hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem();
				this.boundItemMatcher = () -> itemstack == (hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem());
			} else if (extraData.readableBytes() > 1) {
				extraData.readByte();
				boundEntity = world.getEntity(extraData.readVarInt());
				if (boundEntity instanceof Container container) {
					this.internal = container;
					this.bound = true;
				}
			} else {
				boundBlockEntity = this.world.getBlockEntity(pos);
				if (boundBlockEntity instanceof CraftingWorkstationBlockEntity be) {
					this.internal = be;
					this.bound = true;
				}
			}
		} else if (pos != null) {
			boundBlockEntity = this.world.getBlockEntity(pos);
			if (boundBlockEntity instanceof CraftingWorkstationBlockEntity be) {
				this.internal = be;
				this.bound = true;
			}
		}
	}

	private void addContainerSlots() {
		int[][] positions = {
				{0, 8, 7}, {1, 26, 6}, {2, 44, 7}, {3, 62, 7}, {4, 80, 7},
				{5, 8, 24}, {6, 26, 24}, {7, 44, 24}, {8, 62, 24}, {9, 80, 24},
				{10, 8, 42}, {11, 26, 42}, {12, 44, 42}, {13, 62, 42}, {14, 79, 42},
				{15, 8, 60}, {16, 26, 60}, {17, 44, 60}, {18, 62, 60}, {19, 80, 60},
				{20, 134, 34}
		};
		for (int[] pos : positions) {
			int slotIndex = pos[0];
			Slot slot = new Slot(internal, slotIndex, pos[1], pos[2]) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return slotIndex != 20;
				}
			};
			this.customSlots.put(slotIndex, this.addSlot(slot));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.bound) {
			if (this.boundItemMatcher != null)
				return this.boundItemMatcher.get();
			if (this.boundBlockEntity != null)
				return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
			if (this.boundEntity != null)
				return this.boundEntity.isAlive();
		}
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 21) {
				if (!this.moveItemStackTo(itemstack1, 21, this.slots.size(), true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (!this.moveItemStackTo(itemstack1, 0, 21, false)) {
				if (index < 21 + 27) {
					if (!this.moveItemStackTo(itemstack1, 21 + 27, this.slots.size(), true))
						return ItemStack.EMPTY;
				} else if (!this.moveItemStackTo(itemstack1, 21, 21 + 27, false)) {
					return ItemStack.EMPTY;
				}
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		if (!bound && playerIn instanceof ServerPlayer serverPlayer) {
			if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
				for (int j = 0; j < internal.getContainerSize(); ++j) {
					playerIn.drop(internal.getItem(j), false);
					internal.setItem(j, ItemStack.EMPTY);
				}
			} else {
				for (int i = 0; i < internal.getContainerSize(); ++i) {
					playerIn.getInventory().placeItemBackInInventory(internal.getItem(i));
					internal.setItem(i, ItemStack.EMPTY);
				}
			}
		}
	}

	@Override
	public Map<Integer, Slot> getSlots() {
		return Collections.unmodifiableMap(customSlots);
	}

	@Override
	public Map<String, Object> getMenuState() {
		return menuState;
	}
}
