package tn.nightbeam.robotica.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.block.entity.CraftingWorkstationBlockEntity;

public class RoboticaModBlockEntitiesFabric {
	public static void register() {
		BlockEntityType<CraftingWorkstationBlockEntity> craftingWorkstation = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				new ResourceLocation(RoboticaMod.MOD_ID, "crafting_workstation"),
				BlockEntityType.Builder.of(CraftingWorkstationBlockEntity::new, RoboticaModBlocks.CRAFTING_WORKSTATION.get()).build(null));
		RoboticaModBlockEntities.CRAFTING_WORKSTATION = () -> craftingWorkstation;
	}
}
