
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.block.TitaniumOreBlock;
import tn.nightbeam.nextmillennium.block.ScrapStairsBlockBlock;
import tn.nightbeam.nextmillennium.block.ScrapSlabBlock;
import tn.nightbeam.nextmillennium.block.ScrapDebrisBlock;
import tn.nightbeam.nextmillennium.block.ScrapBlockBlock;
import tn.nightbeam.nextmillennium.block.OrichalcumOreBlock;
import tn.nightbeam.nextmillennium.block.OilBlock;
import tn.nightbeam.nextmillennium.block.CraftingWorkstationBlock;
import tn.nightbeam.nextmillennium.block.CobaltOreBlock;
import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class Theyear3001ModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Theyear3001Mod.MODID);
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
