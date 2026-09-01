package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.block.entity.CraftingWorkstationBlockEntity;
import tn.nightbeam.robotica.RoboticaMod;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class RoboticaModBlockEntitiesNeoForge {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RoboticaMod.MOD_ID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CraftingWorkstationBlockEntity>> CRAFTING_WORKSTATION = register("crafting_workstation", RoboticaModBlocksNeoForge.CRAFTING_WORKSTATION, CraftingWorkstationBlockEntity::new);

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> new BlockEntityType<>(supplier, Set.of(block.get())));
	}

	static {
		RoboticaModBlockEntities.CRAFTING_WORKSTATION = CRAFTING_WORKSTATION;
	}
}
