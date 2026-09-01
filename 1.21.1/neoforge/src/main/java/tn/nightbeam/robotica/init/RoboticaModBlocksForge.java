package tn.nightbeam.robotica.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.block.CobaltOreBlock;
import tn.nightbeam.robotica.block.CraftingWorkstationBlock;
import tn.nightbeam.robotica.block.OilBlock;
import tn.nightbeam.robotica.block.OrichalcumOreBlock;
import tn.nightbeam.robotica.block.ScrapBlockBlock;
import tn.nightbeam.robotica.block.ScrapDebrisBlock;
import tn.nightbeam.robotica.block.ScrapSlabBlock;
import tn.nightbeam.robotica.block.ScrapStairsBlockBlock;
import tn.nightbeam.robotica.block.TitaniumOreBlock;

public class RoboticaModBlocksForge {
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
		RoboticaModBlocks.OIL = OIL;
		RoboticaModBlocks.SCRAP_BLOCK = SCRAP_BLOCK;
		RoboticaModBlocks.CRAFTING_WORKSTATION = CRAFTING_WORKSTATION;
		RoboticaModBlocks.SCRAP_SLAB = SCRAP_SLAB;
		RoboticaModBlocks.SCRAP_STAIRS_BLOCK = SCRAP_STAIRS_BLOCK;
		RoboticaModBlocks.SCRAP_DEBRIS = SCRAP_DEBRIS;
		RoboticaModBlocks.TITANIUM_ORE = TITANIUM_ORE;
		RoboticaModBlocks.COBALT_ORE = COBALT_ORE;
		RoboticaModBlocks.ORICHALCUM_ORE = ORICHALCUM_ORE;
	}
}
