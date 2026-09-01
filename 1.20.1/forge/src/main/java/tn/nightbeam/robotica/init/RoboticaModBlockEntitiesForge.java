package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.block.entity.CraftingWorkstationBlockEntity;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.RoboticaModForge;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;

public class RoboticaModBlockEntitiesForge {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RoboticaMod.MOD_ID);
	public static final RegistryObject<BlockEntityType<CraftingWorkstationBlockEntity>> CRAFTING_WORKSTATION = register("crafting_workstation", RoboticaModBlocksForge.CRAFTING_WORKSTATION, CraftingWorkstationBlockEntity::new);
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	static {
		tn.nightbeam.robotica.init.RoboticaModBlockEntities.CRAFTING_WORKSTATION = CRAFTING_WORKSTATION;
	}
}