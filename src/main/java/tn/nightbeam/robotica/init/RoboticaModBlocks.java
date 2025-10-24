
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
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

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class RoboticaModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, RoboticaMod.MODID);
	public static final RegistryObject<Block> OIL = REGISTRY.register("oil", () -> new OilBlock());
	public static final RegistryObject<Block> SCRAP_BLOCK = REGISTRY.register("scrap_block", () -> new ScrapBlockBlock());
	public static final RegistryObject<Block> CRAFTING_WORKSTATION = REGISTRY.register("crafting_workstation", () -> new CraftingWorkstationBlock());
	public static final RegistryObject<Block> SCRAP_SLAB = REGISTRY.register("scrap_slab", () -> new ScrapSlabBlock());
	public static final RegistryObject<Block> SCRAP_STAIRS_BLOCK = REGISTRY.register("scrap_stairs_block", () -> new ScrapStairsBlockBlock());
	public static final RegistryObject<Block> SCRAP_DEBRIS = REGISTRY.register("scrap_debris", () -> new ScrapDebrisBlock());
	public static final RegistryObject<Block> TITANIUM_ORE = REGISTRY.register("titanium_ore", () -> new TitaniumOreBlock());
	public static final RegistryObject<Block> COBALT_ORE = REGISTRY.register("cobalt_ore", () -> new CobaltOreBlock());
	public static final RegistryObject<Block> ORICHALCUM_ORE = REGISTRY.register("orichalcum_ore", () -> new OrichalcumOreBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
