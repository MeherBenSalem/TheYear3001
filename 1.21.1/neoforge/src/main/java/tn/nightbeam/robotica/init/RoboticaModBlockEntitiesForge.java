package tn.nightbeam.robotica.init;


import net.minecraft.core.registries.BuiltInRegistries;
import tn.nightbeam.robotica.block.entity.CraftingWorkstationBlockEntity;
import tn.nightbeam.robotica.RoboticaMod;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;

public class RoboticaModBlockEntitiesForge {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RoboticaMod.MOD_ID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CraftingWorkstationBlockEntity>> CRAFTING_WORKSTATION = register("crafting_workstation", RoboticaModBlocksForge.CRAFTING_WORKSTATION, CraftingWorkstationBlockEntity::new);
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	static {
		tn.nightbeam.robotica.init.RoboticaModBlockEntities.CRAFTING_WORKSTATION = CRAFTING_WORKSTATION;
	}
}