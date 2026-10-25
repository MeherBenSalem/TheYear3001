package tn.nightbeam.chronofall.block;

import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.entity.FluxHolder;
import tn.nightbeam.chronofall.item.FluxItem;
import tn.nightbeam.chronofall.menu.FluxChargerMenu;
import tn.nightbeam.chronofall.util.Fx;

/**
 * Burns fuel (or a Rift Shard) into Flux and pushes it, without cables, into the device in its charge slot, the flux
 * gear worn or held by players within 4 blocks, and any drone, mech or skiff within 8 blocks (with a visible beam).
 */
public class FluxChargerBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    public static final int FUEL = 0;
    public static final int CHARGE = 1;
    /** Flux made per tick while burning. */
    public static final int BURN_RATE = 32;

    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private int stored;
    private int burnLeft;
    private int burnTotal;
    private boolean charging;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> stored / 10;
                case 1 -> burnTotal == 0 ? 0 : burnLeft * 100 / burnTotal;
                case 2 -> charging ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public FluxChargerBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.FLUX_CHARGER_ENTITY.get(), pos, state);
    }

    public int stored() {
        return stored;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FluxChargerBlockEntity charger) {
        boolean changed = charger.burn(level);
        charger.charging = false;
        if (charger.stored > 0) {
            changed |= charger.chargeSlot();
            changed |= charger.chargePlayers(level, pos);
            changed |= charger.chargeRobots(level, pos);
        }
        boolean lit = charger.burnLeft > 0 || charger.charging;
        if (state.getValue(FluxChargerBlock.LIT) != lit) {
            level.setBlock(pos, state.setValue(FluxChargerBlock.LIT, lit), 3);
        }
        if (lit && level.getGameTime() % 60 == 0) {
            level.playSound(null, pos, ModContent.sound("charger_hum"), SoundSource.BLOCKS, 0.4F, 1.0F);
        }
        if (changed) {
            charger.setChanged();
        }
    }

    private boolean burn(Level level) {
        if (burnLeft > 0) {
            burnLeft--;
            stored = Flux.add(stored, BURN_RATE, Flux.CHARGER_CAPACITY);
            return true;
        }
        ItemStack fuel = items.get(FUEL);
        if (fuel.isEmpty() || stored > Flux.CHARGER_CAPACITY - BURN_RATE * 20) {
            return false;
        }
        int flux = fuel.is(ModContent.item("rift_shard")) ? Flux.PER_RIFT_SHARD : Flux.fromBurnTicks(burnTicks(level, fuel));
        if (flux <= 0) {
            return false;
        }
        burnTotal = Math.max(1, flux / BURN_RATE);
        burnLeft = burnTotal;
        ItemStack remainder = fuel.getItem().getCraftingRemainder() != null ? fuel.getItem().getCraftingRemainder().create() : ItemStack.EMPTY;
        fuel.shrink(1);
        if (fuel.isEmpty() && !remainder.isEmpty()) {
            items.set(FUEL, remainder);
        }
        return true;
    }

    private boolean chargeSlot() {
        ItemStack stack = items.get(CHARGE);
        FluxItem item = FluxItem.of(stack);
        if (item == null) {
            return false;
        }
        int moved = item.charge(stack, Math.min(stored, Flux.CHARGER_SLOT_RATE));
        stored -= moved;
        charging |= moved > 0;
        return moved > 0;
    }

    private boolean chargePlayers(Level level, BlockPos pos) {
        boolean any = false;
        for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(Flux.CHARGER_PLAYER_RANGE))) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getItemBySlot(slot);
                FluxItem item = FluxItem.of(stack);
                if (item != null && stored > 0) {
                    int moved = item.charge(stack, Math.min(stored, Flux.CHARGER_PLAYER_RATE));
                    stored -= moved;
                    if (moved > 0) {
                        any = true;
                        beam(level, pos, player);
                    }
                }
            }
        }
        charging |= any;
        return any;
    }

    private boolean chargeRobots(Level level, BlockPos pos) {
        boolean any = false;
        for (Entity entity : level.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(Flux.CHARGER_ENTITY_RANGE), e -> e instanceof FluxHolder)) {
            FluxHolder holder = (FluxHolder) entity;
            int moved = holder.charge(Math.min(stored, Flux.CHARGER_ENTITY_RATE));
            stored -= moved;
            if (moved > 0) {
                any = true;
                beam(level, pos, entity);
            }
            if (stored <= 0) {
                break;
            }
        }
        charging |= any;
        return any;
    }

    private static void beam(Level level, BlockPos pos, Entity target) {
        if (level instanceof ServerLevel server && level.getGameTime() % 4 == 0) {
            Vec3 from = Vec3.atCenterOf(pos).add(0, 0.55, 0);
            Fx.beam(server, ModContent.FLUX_PARTICLE.get(), from, target.getBoundingBox().getCenter(), 0.4, 64);
        }
    }

    public int comparatorOutput() {
        return stored <= 0 ? 0 : 1 + stored * 14 / Flux.CHARGER_CAPACITY;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.chronofall.flux_charger");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FluxChargerMenu(id, inventory, this, data);
    }

    @Override
    protected void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
        stored = tag.getIntOr("Flux", 0);
        burnLeft = tag.getIntOr("BurnLeft", 0);
        burnTotal = tag.getIntOr("BurnTotal", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
        tag.putInt("Flux", stored);
        tag.putInt("BurnLeft", burnLeft);
        tag.putInt("BurnTotal", burnTotal);
    }

    // Container ----------------------------------------------------------------------------------------------------

    public boolean isFuel(ItemStack stack) {
        return isFuel(level, stack);
    }

    /** A fuel's furnace burn time (26.3 resolves it from the item's cooking_fuel component in a loot context). */
    private int burnTicks(Level level, ItemStack fuel) {
        if (!(level instanceof ServerLevel server)) {
            return 0;
        }
        net.minecraft.world.level.storage.loot.LootContext context = new net.minecraft.world.level.storage.loot.LootContext.Builder(
                new net.minecraft.world.level.storage.loot.LootParams.Builder(server)
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE, getBlockState())
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY, this)
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, Vec3.atCenterOf(getBlockPos()))
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.CONTAINER, this)
                        .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CONTAINER_PROCESS))
                .create(java.util.Optional.empty());
        return net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.getFromItem(fuel,
                net.minecraft.core.component.DataComponents.COOKING_FUEL, net.minecraft.world.item.component.CookingFuel::burnTime, context, 0);
    }

    public static boolean isFuel(@Nullable Level level, ItemStack stack) {
        return stack.is(ModContent.item("rift_shard")) || stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack taken = ContainerHelper.removeItem(items, slot, amount);
        if (!taken.isEmpty()) {
            setChanged();
        }
        return taken;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == FUEL ? isFuel(stack) : FluxItem.of(stack) != null;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{FUEL, CHARGE};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if (slot == FUEL) {
            return !isFuel(stack);
        }
        FluxItem item = FluxItem.of(stack);
        return item == null || item.flux(stack) >= item.capacity();
    }
}
