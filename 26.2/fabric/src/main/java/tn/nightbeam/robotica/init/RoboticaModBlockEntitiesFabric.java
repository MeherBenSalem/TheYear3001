package tn.nightbeam.robotica.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.block.entity.CraftingWorkstationBlockEntity;

import java.util.Set;

public final class RoboticaModBlockEntitiesFabric {
	private RoboticaModBlockEntitiesFabric() {
	}

	public static void register() {
		BlockEntityType<CraftingWorkstationBlockEntity> craftingWorkstation = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "crafting_workstation"),
				new BlockEntityType<>(CraftingWorkstationBlockEntity::new, Set.of(RoboticaModBlocks.CRAFTING_WORKSTATION.get())));
		RoboticaModBlockEntities.CRAFTING_WORKSTATION = () -> craftingWorkstation;
	}
}
