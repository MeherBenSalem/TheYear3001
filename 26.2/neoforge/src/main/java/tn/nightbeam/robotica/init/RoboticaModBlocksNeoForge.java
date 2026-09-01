package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.block.TitaniumOreBlock;
import tn.nightbeam.robotica.block.ScrapStairsBlockBlock;
import tn.nightbeam.robotica.block.ScrapSlabBlock;
import tn.nightbeam.robotica.block.ScrapDebrisBlock;
import tn.nightbeam.robotica.block.ScrapBlockBlock;
import tn.nightbeam.robotica.block.OrichalcumOreBlock;
import tn.nightbeam.robotica.block.OilBlock;
import tn.nightbeam.robotica.block.CraftingWorkstationBlock;
import tn.nightbeam.robotica.block.CobaltOreBlock;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.RoboticaModNeoForge;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class RoboticaModBlocksNeoForge {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK, RoboticaMod.MOD_ID);
	public static final DeferredHolder<Block, Block> OIL = REGISTRY.register("oil", () -> new OilBlock());
	public static final DeferredHolder<Block, Block> SCRAP_BLOCK = REGISTRY.register("scrap_block", () -> new ScrapBlockBlock());
	public static final DeferredHolder<Block, Block> CRAFTING_WORKSTATION = REGISTRY.register("crafting_workstation", () -> new CraftingWorkstationBlock());
	public static final DeferredHolder<Block, Block> SCRAP_SLAB = REGISTRY.register("scrap_slab", () -> new ScrapSlabBlock());
	public static final DeferredHolder<Block, Block> SCRAP_STAIRS_BLOCK = REGISTRY.register("scrap_stairs_block", () -> new ScrapStairsBlockBlock());
	public static final DeferredHolder<Block, Block> SCRAP_DEBRIS = REGISTRY.register("scrap_debris", () -> new ScrapDebrisBlock());
	public static final DeferredHolder<Block, Block> TITANIUM_ORE = REGISTRY.register("titanium_ore", () -> new TitaniumOreBlock());
	public static final DeferredHolder<Block, Block> COBALT_ORE = REGISTRY.register("cobalt_ore", () -> new CobaltOreBlock());
	public static final DeferredHolder<Block, Block> ORICHALCUM_ORE = REGISTRY.register("orichalcum_ore", () -> new OrichalcumOreBlock());


	static {
		tn.nightbeam.robotica.init.RoboticaModBlocks.OIL = OIL;
		tn.nightbeam.robotica.init.RoboticaModBlocks.SCRAP_BLOCK = SCRAP_BLOCK;
		tn.nightbeam.robotica.init.RoboticaModBlocks.CRAFTING_WORKSTATION = CRAFTING_WORKSTATION;
		tn.nightbeam.robotica.init.RoboticaModBlocks.SCRAP_SLAB = SCRAP_SLAB;
		tn.nightbeam.robotica.init.RoboticaModBlocks.SCRAP_STAIRS_BLOCK = SCRAP_STAIRS_BLOCK;
		tn.nightbeam.robotica.init.RoboticaModBlocks.SCRAP_DEBRIS = SCRAP_DEBRIS;
		tn.nightbeam.robotica.init.RoboticaModBlocks.TITANIUM_ORE = TITANIUM_ORE;
		tn.nightbeam.robotica.init.RoboticaModBlocks.COBALT_ORE = COBALT_ORE;
		tn.nightbeam.robotica.init.RoboticaModBlocks.ORICHALCUM_ORE = ORICHALCUM_ORE;
	}
}
