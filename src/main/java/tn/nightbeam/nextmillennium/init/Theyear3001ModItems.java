
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.item.TitaniumSwordItem;
import tn.nightbeam.nextmillennium.item.TitaniumShovelItem;
import tn.nightbeam.nextmillennium.item.TitaniumRodItem;
import tn.nightbeam.nextmillennium.item.TitaniumPickaxeItem;
import tn.nightbeam.nextmillennium.item.TitaniumIngotItem;
import tn.nightbeam.nextmillennium.item.TitaniumHoeItem;
import tn.nightbeam.nextmillennium.item.TitaniumAxeItem;
import tn.nightbeam.nextmillennium.item.TitaniumArmorItem;
import tn.nightbeam.nextmillennium.item.SpeedUpgradeCardItem;
import tn.nightbeam.nextmillennium.item.ScrapSwordItem;
import tn.nightbeam.nextmillennium.item.ScrapStickItem;
import tn.nightbeam.nextmillennium.item.ScrapShovelItem;
import tn.nightbeam.nextmillennium.item.ScrapPickaxeItem;
import tn.nightbeam.nextmillennium.item.ScrapItem;
import tn.nightbeam.nextmillennium.item.ScrapHoeItem;
import tn.nightbeam.nextmillennium.item.ScrapAxeItem;
import tn.nightbeam.nextmillennium.item.ScrapArmorItem;
import tn.nightbeam.nextmillennium.item.RepairCardItem;
import tn.nightbeam.nextmillennium.item.RawTitaniumItem;
import tn.nightbeam.nextmillennium.item.RawOrichalcumItem;
import tn.nightbeam.nextmillennium.item.RawCobaltItem;
import tn.nightbeam.nextmillennium.item.PrintedCircuitBoardItem;
import tn.nightbeam.nextmillennium.item.OrichalcumSwordItem;
import tn.nightbeam.nextmillennium.item.OrichalcumShovelItem;
import tn.nightbeam.nextmillennium.item.OrichalcumPickaxeItem;
import tn.nightbeam.nextmillennium.item.OrichalcumIngotItem;
import tn.nightbeam.nextmillennium.item.OrichalcumHoeItem;
import tn.nightbeam.nextmillennium.item.OrichalcumAxeItem;
import tn.nightbeam.nextmillennium.item.OrichalcumArmorItem;
import tn.nightbeam.nextmillennium.item.OilItem;
import tn.nightbeam.nextmillennium.item.MechMK1EggItem;
import tn.nightbeam.nextmillennium.item.FlyingCapsuleItem;
import tn.nightbeam.nextmillennium.item.EmptyCardItem;
import tn.nightbeam.nextmillennium.item.ElectricalConductorsItem;
import tn.nightbeam.nextmillennium.item.DefenseUpgradeCardItem;
import tn.nightbeam.nextmillennium.item.DamageUpgradeCardItem;
import tn.nightbeam.nextmillennium.item.CobaltSwordItem;
import tn.nightbeam.nextmillennium.item.CobaltShovelItem;
import tn.nightbeam.nextmillennium.item.CobaltPickaxeItem;
import tn.nightbeam.nextmillennium.item.CobaltIngotItem;
import tn.nightbeam.nextmillennium.item.CobaltHoeItem;
import tn.nightbeam.nextmillennium.item.CobaltAxeItem;
import tn.nightbeam.nextmillennium.item.CobaltArmorItem;
import tn.nightbeam.nextmillennium.item.CentralProcessingUnitItem;
import tn.nightbeam.nextmillennium.item.BulkUpgradeCardItem;
import tn.nightbeam.nextmillennium.item.BoggieMK1EggItem;
import tn.nightbeam.nextmillennium.item.BlankPrintedCircuitBoardItem;
import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

public class Theyear3001ModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Theyear3001Mod.MODID);
	public static final RegistryObject<Item> DAMAGE_UPGRADE_CARD = REGISTRY.register("damage_upgrade_card", () -> new DamageUpgradeCardItem());
	public static final RegistryObject<Item> OIL_BUCKET = REGISTRY.register("oil_bucket", () -> new OilItem());
	public static final RegistryObject<Item> BULK_UPGRADE_CARD = REGISTRY.register("bulk_upgrade_card", () -> new BulkUpgradeCardItem());
	public static final RegistryObject<Item> SPEED_UPGRADE_CARD = REGISTRY.register("speed_upgrade_card", () -> new SpeedUpgradeCardItem());
	public static final RegistryObject<Item> B_1928_SPAWN_EGG = REGISTRY.register("b_1928_spawn_egg", () -> new ForgeSpawnEggItem(Theyear3001ModEntities.B_1928, -16776961, -1, new Item.Properties()));
	public static final RegistryObject<Item> B_1990_SPAWN_EGG = REGISTRY.register("b_1990_spawn_egg", () -> new ForgeSpawnEggItem(Theyear3001ModEntities.B_1990, -13395712, -1, new Item.Properties()));
	public static final RegistryObject<Item> B_2050_SPAWN_EGG = REGISTRY.register("b_2050_spawn_egg", () -> new ForgeSpawnEggItem(Theyear3001ModEntities.B_2050, -6750157, -1, new Item.Properties()));
	public static final RegistryObject<Item> REPAIR_CARD = REGISTRY.register("repair_card", () -> new RepairCardItem());
	public static final RegistryObject<Item> SCRAP = REGISTRY.register("scrap", () -> new ScrapItem());
	public static final RegistryObject<Item> SCRAP_BLOCK = block(Theyear3001ModBlocks.SCRAP_BLOCK);
	public static final RegistryObject<Item> CRAFTING_WORKSTATION = block(Theyear3001ModBlocks.CRAFTING_WORKSTATION);
	public static final RegistryObject<Item> EMPTY_CARD = REGISTRY.register("empty_card", () -> new EmptyCardItem());
	public static final RegistryObject<Item> MECH_MK_1_EGG = REGISTRY.register("mech_mk_1_egg", () -> new MechMK1EggItem());
	public static final RegistryObject<Item> BOGGIE_MK_1_EGG = REGISTRY.register("boggie_mk_1_egg", () -> new BoggieMK1EggItem());
	public static final RegistryObject<Item> PRINTED_CIRCUIT_BOARD = REGISTRY.register("printed_circuit_board", () -> new PrintedCircuitBoardItem());
	public static final RegistryObject<Item> BLANK_PRINTED_CIRCUIT_BOARD = REGISTRY.register("blank_printed_circuit_board", () -> new BlankPrintedCircuitBoardItem());
	public static final RegistryObject<Item> CENTRAL_PROCESSING_UNIT = REGISTRY.register("central_processing_unit", () -> new CentralProcessingUnitItem());
	public static final RegistryObject<Item> ELECTRICAL_CONDUCTORS = REGISTRY.register("electrical_conductors", () -> new ElectricalConductorsItem());
	public static final RegistryObject<Item> DEFENSE_UPGRADE_CARD = REGISTRY.register("defense_upgrade_card", () -> new DefenseUpgradeCardItem());
	public static final RegistryObject<Item> SENTINEL_MK_1_SPAWN_EGG = REGISTRY.register("sentinel_mk_1_spawn_egg", () -> new ForgeSpawnEggItem(Theyear3001ModEntities.SENTINEL_MK_1, -13421773, -6710887, new Item.Properties()));
	public static final RegistryObject<Item> SCRAP_STICK = REGISTRY.register("scrap_stick", () -> new ScrapStickItem());
	public static final RegistryObject<Item> SCRAP_AXE = REGISTRY.register("scrap_axe", () -> new ScrapAxeItem());
	public static final RegistryObject<Item> SCRAP_SWORD = REGISTRY.register("scrap_sword", () -> new ScrapSwordItem());
	public static final RegistryObject<Item> SCRAP_SHOVEL = REGISTRY.register("scrap_shovel", () -> new ScrapShovelItem());
	public static final RegistryObject<Item> SCRAP_HOE = REGISTRY.register("scrap_hoe", () -> new ScrapHoeItem());
	public static final RegistryObject<Item> SCRAP_PICKAXE = REGISTRY.register("scrap_pickaxe", () -> new ScrapPickaxeItem());
	public static final RegistryObject<Item> SCRAP_SLAB = block(Theyear3001ModBlocks.SCRAP_SLAB);
	public static final RegistryObject<Item> SCRAP_STAIRS_BLOCK = block(Theyear3001ModBlocks.SCRAP_STAIRS_BLOCK);
	public static final RegistryObject<Item> FLYING_CAPSULE_MECH_SPAWN_EGG = REGISTRY.register("flying_capsule_mech_spawn_egg", () -> new ForgeSpawnEggItem(Theyear3001ModEntities.FLYING_CAPSULE_MECH, -1, -3407719, new Item.Properties()));
	public static final RegistryObject<Item> FLYING_CAPSULE = REGISTRY.register("flying_capsule", () -> new FlyingCapsuleItem());
	public static final RegistryObject<Item> SCRAP_DEBRIS = block(Theyear3001ModBlocks.SCRAP_DEBRIS);
	public static final RegistryObject<Item> TITANIUM_ORE = block(Theyear3001ModBlocks.TITANIUM_ORE);
	public static final RegistryObject<Item> TITANIUM_INGOT = REGISTRY.register("titanium_ingot", () -> new TitaniumIngotItem());
	public static final RegistryObject<Item> ORICHALCUM_INGOT = REGISTRY.register("orichalcum_ingot", () -> new OrichalcumIngotItem());
	public static final RegistryObject<Item> COBALT_INGOT = REGISTRY.register("cobalt_ingot", () -> new CobaltIngotItem());
	public static final RegistryObject<Item> RAW_TITANIUM = REGISTRY.register("raw_titanium", () -> new RawTitaniumItem());
	public static final RegistryObject<Item> RAW_COBALT = REGISTRY.register("raw_cobalt", () -> new RawCobaltItem());
	public static final RegistryObject<Item> RAW_ORICHALCUM = REGISTRY.register("raw_orichalcum", () -> new RawOrichalcumItem());
	public static final RegistryObject<Item> COBALT_ORE = block(Theyear3001ModBlocks.COBALT_ORE);
	public static final RegistryObject<Item> ORICHALCUM_ORE = block(Theyear3001ModBlocks.ORICHALCUM_ORE);
	public static final RegistryObject<Item> TITANIUM_ROD = REGISTRY.register("titanium_rod", () -> new TitaniumRodItem());
	public static final RegistryObject<Item> TITANIUM_AXE = REGISTRY.register("titanium_axe", () -> new TitaniumAxeItem());
	public static final RegistryObject<Item> TITANIUM_SWORD = REGISTRY.register("titanium_sword", () -> new TitaniumSwordItem());
	public static final RegistryObject<Item> TITANIUM_SHOVEL = REGISTRY.register("titanium_shovel", () -> new TitaniumShovelItem());
	public static final RegistryObject<Item> TITANIUM_HOE = REGISTRY.register("titanium_hoe", () -> new TitaniumHoeItem());
	public static final RegistryObject<Item> TITANIUM_PICKAXE = REGISTRY.register("titanium_pickaxe", () -> new TitaniumPickaxeItem());
	public static final RegistryObject<Item> SCRAP_ARMOR_HELMET = REGISTRY.register("scrap_armor_helmet", () -> new ScrapArmorItem.Helmet());
	public static final RegistryObject<Item> SCRAP_ARMOR_CHESTPLATE = REGISTRY.register("scrap_armor_chestplate", () -> new ScrapArmorItem.Chestplate());
	public static final RegistryObject<Item> SCRAP_ARMOR_LEGGINGS = REGISTRY.register("scrap_armor_leggings", () -> new ScrapArmorItem.Leggings());
	public static final RegistryObject<Item> SCRAP_ARMOR_BOOTS = REGISTRY.register("scrap_armor_boots", () -> new ScrapArmorItem.Boots());
	public static final RegistryObject<Item> ORICHALCUM_ARMOR_HELMET = REGISTRY.register("orichalcum_armor_helmet", () -> new OrichalcumArmorItem.Helmet());
	public static final RegistryObject<Item> ORICHALCUM_ARMOR_CHESTPLATE = REGISTRY.register("orichalcum_armor_chestplate", () -> new OrichalcumArmorItem.Chestplate());
	public static final RegistryObject<Item> ORICHALCUM_ARMOR_LEGGINGS = REGISTRY.register("orichalcum_armor_leggings", () -> new OrichalcumArmorItem.Leggings());
	public static final RegistryObject<Item> ORICHALCUM_ARMOR_BOOTS = REGISTRY.register("orichalcum_armor_boots", () -> new OrichalcumArmorItem.Boots());
	public static final RegistryObject<Item> TITANIUM_ARMOR_HELMET = REGISTRY.register("titanium_armor_helmet", () -> new TitaniumArmorItem.Helmet());
	public static final RegistryObject<Item> TITANIUM_ARMOR_CHESTPLATE = REGISTRY.register("titanium_armor_chestplate", () -> new TitaniumArmorItem.Chestplate());
	public static final RegistryObject<Item> TITANIUM_ARMOR_LEGGINGS = REGISTRY.register("titanium_armor_leggings", () -> new TitaniumArmorItem.Leggings());
	public static final RegistryObject<Item> TITANIUM_ARMOR_BOOTS = REGISTRY.register("titanium_armor_boots", () -> new TitaniumArmorItem.Boots());
	public static final RegistryObject<Item> COBALT_ARMOR_HELMET = REGISTRY.register("cobalt_armor_helmet", () -> new CobaltArmorItem.Helmet());
	public static final RegistryObject<Item> COBALT_ARMOR_CHESTPLATE = REGISTRY.register("cobalt_armor_chestplate", () -> new CobaltArmorItem.Chestplate());
	public static final RegistryObject<Item> COBALT_ARMOR_LEGGINGS = REGISTRY.register("cobalt_armor_leggings", () -> new CobaltArmorItem.Leggings());
	public static final RegistryObject<Item> COBALT_ARMOR_BOOTS = REGISTRY.register("cobalt_armor_boots", () -> new CobaltArmorItem.Boots());
	public static final RegistryObject<Item> COBALT_AXE = REGISTRY.register("cobalt_axe", () -> new CobaltAxeItem());
	public static final RegistryObject<Item> COBALT_SWORD = REGISTRY.register("cobalt_sword", () -> new CobaltSwordItem());
	public static final RegistryObject<Item> COBALT_SHOVEL = REGISTRY.register("cobalt_shovel", () -> new CobaltShovelItem());
	public static final RegistryObject<Item> COBALT_HOE = REGISTRY.register("cobalt_hoe", () -> new CobaltHoeItem());
	public static final RegistryObject<Item> COBALT_PICKAXE = REGISTRY.register("cobalt_pickaxe", () -> new CobaltPickaxeItem());
	public static final RegistryObject<Item> ORICHALCUM_AXE = REGISTRY.register("orichalcum_axe", () -> new OrichalcumAxeItem());
	public static final RegistryObject<Item> ORICHALCUM_SWORD = REGISTRY.register("orichalcum_sword", () -> new OrichalcumSwordItem());
	public static final RegistryObject<Item> ORICHALCUM_SHOVEL = REGISTRY.register("orichalcum_shovel", () -> new OrichalcumShovelItem());
	public static final RegistryObject<Item> ORICHALCUM_HOE = REGISTRY.register("orichalcum_hoe", () -> new OrichalcumHoeItem());
	public static final RegistryObject<Item> ORICHALCUM_PICKAXE = REGISTRY.register("orichalcum_pickaxe", () -> new OrichalcumPickaxeItem());

	// Start of user code block custom items
	// End of user code block custom items
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
