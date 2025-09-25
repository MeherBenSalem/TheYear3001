
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.block.entity.CraftingWorkstationBlockEntity;
import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

public class Theyear3001ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Theyear3001Mod.MODID);
	public static final RegistryObject<BlockEntityType<?>> CRAFTING_WORKSTATION = register("crafting_workstation", Theyear3001ModBlocks.CRAFTING_WORKSTATION, CraftingWorkstationBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
