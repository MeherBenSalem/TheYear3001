
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.block.entity.CraftingWorkstationBlockEntity;
import tn.nightbeam.robotica.RoboticaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

public class RoboticaModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RoboticaMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> CRAFTING_WORKSTATION = register("crafting_workstation", RoboticaModBlocks.CRAFTING_WORKSTATION, CraftingWorkstationBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
