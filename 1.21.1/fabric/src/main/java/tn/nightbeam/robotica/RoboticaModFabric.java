package tn.nightbeam.robotica;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import tn.nightbeam.robotica.block.*;
import tn.nightbeam.robotica.entity.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import tn.nightbeam.robotica.events.FabricRoboticaModEvents;
import tn.nightbeam.robotica.init.*;
import tn.nightbeam.robotica.item.*;
import tn.nightbeam.robotica.network.FabricMenuStateUpdatePacket;

public class RoboticaModFabric implements ModInitializer {
	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		RoboticaModFluidsFabric.register();
		registerBlocks();
		registerItems();
		registerEntities();
		RoboticaModBlockEntitiesFabric.register();
		RoboticaModSoundsFabric.register();
		RoboticaModTabsFabric.register();
		RoboticaModMenusFabric.register();
		FabricRoboticaModEvents.register();
	}

	private void registerBlocks() {
		Block scrap_block = Registry.register(BuiltInRegistries.BLOCK, id("scrap_block"), new ScrapBlockBlock());
		RoboticaModBlocks.SCRAP_BLOCK = () -> scrap_block;
		Block crafting_workstation = Registry.register(BuiltInRegistries.BLOCK, id("crafting_workstation"), new CraftingWorkstationBlock());
		RoboticaModBlocks.CRAFTING_WORKSTATION = () -> crafting_workstation;
		Block scrap_slab = Registry.register(BuiltInRegistries.BLOCK, id("scrap_slab"), new ScrapSlabBlock());
		RoboticaModBlocks.SCRAP_SLAB = () -> scrap_slab;
		Block scrap_stairs_block = Registry.register(BuiltInRegistries.BLOCK, id("scrap_stairs_block"), new ScrapStairsBlockBlock());
		RoboticaModBlocks.SCRAP_STAIRS_BLOCK = () -> scrap_stairs_block;
		Block scrap_debris = Registry.register(BuiltInRegistries.BLOCK, id("scrap_debris"), new ScrapDebrisBlock());
		RoboticaModBlocks.SCRAP_DEBRIS = () -> scrap_debris;
		Block titanium_ore = Registry.register(BuiltInRegistries.BLOCK, id("titanium_ore"), new TitaniumOreBlock());
		RoboticaModBlocks.TITANIUM_ORE = () -> titanium_ore;
		Block cobalt_ore = Registry.register(BuiltInRegistries.BLOCK, id("cobalt_ore"), new CobaltOreBlock());
		RoboticaModBlocks.COBALT_ORE = () -> cobalt_ore;
		Block orichalcum_ore = Registry.register(BuiltInRegistries.BLOCK, id("orichalcum_ore"), new OrichalcumOreBlock());
		RoboticaModBlocks.ORICHALCUM_ORE = () -> orichalcum_ore;
	}

	private void registerItems() {
		Item oil_bucket = Registry.register(BuiltInRegistries.ITEM, id("oil_bucket"), new OilItem());
		RoboticaModItems.OIL_BUCKET = () -> oil_bucket;
		Item damage_upgrade_card = Registry.register(BuiltInRegistries.ITEM, id("damage_upgrade_card"), new DamageUpgradeCardItem());
		RoboticaModItems.DAMAGE_UPGRADE_CARD = () -> damage_upgrade_card;
		Item bulk_upgrade_card = Registry.register(BuiltInRegistries.ITEM, id("bulk_upgrade_card"), new BulkUpgradeCardItem());
		RoboticaModItems.BULK_UPGRADE_CARD = () -> bulk_upgrade_card;
		Item speed_upgrade_card = Registry.register(BuiltInRegistries.ITEM, id("speed_upgrade_card"), new SpeedUpgradeCardItem());
		RoboticaModItems.SPEED_UPGRADE_CARD = () -> speed_upgrade_card;
		Item repair_card = Registry.register(BuiltInRegistries.ITEM, id("repair_card"), new RepairCardItem());
		RoboticaModItems.REPAIR_CARD = () -> repair_card;
		Item scrap = Registry.register(BuiltInRegistries.ITEM, id("scrap"), new ScrapItem());
		RoboticaModItems.SCRAP = () -> scrap;
		Item empty_card = Registry.register(BuiltInRegistries.ITEM, id("empty_card"), new EmptyCardItem());
		RoboticaModItems.EMPTY_CARD = () -> empty_card;
		Item mech_mk_1_egg = Registry.register(BuiltInRegistries.ITEM, id("mech_mk_1_egg"), new MechMK1EggItem());
		RoboticaModItems.MECH_MK_1_EGG = () -> mech_mk_1_egg;
		Item boggie_mk_1_egg = Registry.register(BuiltInRegistries.ITEM, id("boggie_mk_1_egg"), new BoggieMK1EggItem());
		RoboticaModItems.BOGGIE_MK_1_EGG = () -> boggie_mk_1_egg;
		Item printed_circuit_board = Registry.register(BuiltInRegistries.ITEM, id("printed_circuit_board"), new PrintedCircuitBoardItem());
		RoboticaModItems.PRINTED_CIRCUIT_BOARD = () -> printed_circuit_board;
		Item blank_printed_circuit_board = Registry.register(BuiltInRegistries.ITEM, id("blank_printed_circuit_board"), new BlankPrintedCircuitBoardItem());
		RoboticaModItems.BLANK_PRINTED_CIRCUIT_BOARD = () -> blank_printed_circuit_board;
		Item central_processing_unit = Registry.register(BuiltInRegistries.ITEM, id("central_processing_unit"), new CentralProcessingUnitItem());
		RoboticaModItems.CENTRAL_PROCESSING_UNIT = () -> central_processing_unit;
		Item electrical_conductors = Registry.register(BuiltInRegistries.ITEM, id("electrical_conductors"), new ElectricalConductorsItem());
		RoboticaModItems.ELECTRICAL_CONDUCTORS = () -> electrical_conductors;
		Item defense_upgrade_card = Registry.register(BuiltInRegistries.ITEM, id("defense_upgrade_card"), new DefenseUpgradeCardItem());
		RoboticaModItems.DEFENSE_UPGRADE_CARD = () -> defense_upgrade_card;
		Item scrap_axe = Registry.register(BuiltInRegistries.ITEM, id("scrap_axe"), new ScrapAxeItem());
		RoboticaModItems.SCRAP_AXE = () -> scrap_axe;
		Item scrap_sword = Registry.register(BuiltInRegistries.ITEM, id("scrap_sword"), new ScrapSwordItem());
		RoboticaModItems.SCRAP_SWORD = () -> scrap_sword;
		Item scrap_shovel = Registry.register(BuiltInRegistries.ITEM, id("scrap_shovel"), new ScrapShovelItem());
		RoboticaModItems.SCRAP_SHOVEL = () -> scrap_shovel;
		Item scrap_hoe = Registry.register(BuiltInRegistries.ITEM, id("scrap_hoe"), new ScrapHoeItem());
		RoboticaModItems.SCRAP_HOE = () -> scrap_hoe;
		Item scrap_pickaxe = Registry.register(BuiltInRegistries.ITEM, id("scrap_pickaxe"), new ScrapPickaxeItem());
		RoboticaModItems.SCRAP_PICKAXE = () -> scrap_pickaxe;
		Item space_ship_capsule = Registry.register(BuiltInRegistries.ITEM, id("space_ship_capsule"), new FlyingCapsuleItem());
		RoboticaModItems.SPACE_SHIP_CAPSULE = () -> space_ship_capsule;
		Item titanium_ingot = Registry.register(BuiltInRegistries.ITEM, id("titanium_ingot"), new TitaniumIngotItem());
		RoboticaModItems.TITANIUM_INGOT = () -> titanium_ingot;
		Item orichalcum_ingot = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_ingot"), new OrichalcumIngotItem());
		RoboticaModItems.ORICHALCUM_INGOT = () -> orichalcum_ingot;
		Item cobalt_ingot = Registry.register(BuiltInRegistries.ITEM, id("cobalt_ingot"), new CobaltIngotItem());
		RoboticaModItems.COBALT_INGOT = () -> cobalt_ingot;
		Item raw_titanium = Registry.register(BuiltInRegistries.ITEM, id("raw_titanium"), new RawTitaniumItem());
		RoboticaModItems.RAW_TITANIUM = () -> raw_titanium;
		Item raw_cobalt = Registry.register(BuiltInRegistries.ITEM, id("raw_cobalt"), new RawCobaltItem());
		RoboticaModItems.RAW_COBALT = () -> raw_cobalt;
		Item raw_orichalcum = Registry.register(BuiltInRegistries.ITEM, id("raw_orichalcum"), new RawOrichalcumItem());
		RoboticaModItems.RAW_ORICHALCUM = () -> raw_orichalcum;
		Item titanium_axe = Registry.register(BuiltInRegistries.ITEM, id("titanium_axe"), new TitaniumAxeItem());
		RoboticaModItems.TITANIUM_AXE = () -> titanium_axe;
		Item titanium_sword = Registry.register(BuiltInRegistries.ITEM, id("titanium_sword"), new TitaniumSwordItem());
		RoboticaModItems.TITANIUM_SWORD = () -> titanium_sword;
		Item titanium_shovel = Registry.register(BuiltInRegistries.ITEM, id("titanium_shovel"), new TitaniumShovelItem());
		RoboticaModItems.TITANIUM_SHOVEL = () -> titanium_shovel;
		Item titanium_hoe = Registry.register(BuiltInRegistries.ITEM, id("titanium_hoe"), new TitaniumHoeItem());
		RoboticaModItems.TITANIUM_HOE = () -> titanium_hoe;
		Item titanium_pickaxe = Registry.register(BuiltInRegistries.ITEM, id("titanium_pickaxe"), new TitaniumPickaxeItem());
		RoboticaModItems.TITANIUM_PICKAXE = () -> titanium_pickaxe;
		Item scrap_armor_helmet = Registry.register(BuiltInRegistries.ITEM, id("scrap_armor_helmet"), new ScrapArmorItem.Helmet());
		RoboticaModItems.SCRAP_ARMOR_HELMET = () -> scrap_armor_helmet;
		Item scrap_armor_chestplate = Registry.register(BuiltInRegistries.ITEM, id("scrap_armor_chestplate"), new ScrapArmorItem.Chestplate());
		RoboticaModItems.SCRAP_ARMOR_CHESTPLATE = () -> scrap_armor_chestplate;
		Item scrap_armor_leggings = Registry.register(BuiltInRegistries.ITEM, id("scrap_armor_leggings"), new ScrapArmorItem.Leggings());
		RoboticaModItems.SCRAP_ARMOR_LEGGINGS = () -> scrap_armor_leggings;
		Item scrap_armor_boots = Registry.register(BuiltInRegistries.ITEM, id("scrap_armor_boots"), new ScrapArmorItem.Boots());
		RoboticaModItems.SCRAP_ARMOR_BOOTS = () -> scrap_armor_boots;
		Item orichalcum_armor_helmet = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_armor_helmet"), new OrichalcumArmorItem.Helmet());
		RoboticaModItems.ORICHALCUM_ARMOR_HELMET = () -> orichalcum_armor_helmet;
		Item orichalcum_armor_chestplate = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_armor_chestplate"), new OrichalcumArmorItem.Chestplate());
		RoboticaModItems.ORICHALCUM_ARMOR_CHESTPLATE = () -> orichalcum_armor_chestplate;
		Item orichalcum_armor_leggings = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_armor_leggings"), new OrichalcumArmorItem.Leggings());
		RoboticaModItems.ORICHALCUM_ARMOR_LEGGINGS = () -> orichalcum_armor_leggings;
		Item orichalcum_armor_boots = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_armor_boots"), new OrichalcumArmorItem.Boots());
		RoboticaModItems.ORICHALCUM_ARMOR_BOOTS = () -> orichalcum_armor_boots;
		Item titanium_armor_helmet = Registry.register(BuiltInRegistries.ITEM, id("titanium_armor_helmet"), new TitaniumArmorItem.Helmet());
		RoboticaModItems.TITANIUM_ARMOR_HELMET = () -> titanium_armor_helmet;
		Item titanium_armor_chestplate = Registry.register(BuiltInRegistries.ITEM, id("titanium_armor_chestplate"), new TitaniumArmorItem.Chestplate());
		RoboticaModItems.TITANIUM_ARMOR_CHESTPLATE = () -> titanium_armor_chestplate;
		Item titanium_armor_leggings = Registry.register(BuiltInRegistries.ITEM, id("titanium_armor_leggings"), new TitaniumArmorItem.Leggings());
		RoboticaModItems.TITANIUM_ARMOR_LEGGINGS = () -> titanium_armor_leggings;
		Item titanium_armor_boots = Registry.register(BuiltInRegistries.ITEM, id("titanium_armor_boots"), new TitaniumArmorItem.Boots());
		RoboticaModItems.TITANIUM_ARMOR_BOOTS = () -> titanium_armor_boots;
		Item cobalt_armor_helmet = Registry.register(BuiltInRegistries.ITEM, id("cobalt_armor_helmet"), new CobaltArmorItem.Helmet());
		RoboticaModItems.COBALT_ARMOR_HELMET = () -> cobalt_armor_helmet;
		Item cobalt_armor_chestplate = Registry.register(BuiltInRegistries.ITEM, id("cobalt_armor_chestplate"), new CobaltArmorItem.Chestplate());
		RoboticaModItems.COBALT_ARMOR_CHESTPLATE = () -> cobalt_armor_chestplate;
		Item cobalt_armor_leggings = Registry.register(BuiltInRegistries.ITEM, id("cobalt_armor_leggings"), new CobaltArmorItem.Leggings());
		RoboticaModItems.COBALT_ARMOR_LEGGINGS = () -> cobalt_armor_leggings;
		Item cobalt_armor_boots = Registry.register(BuiltInRegistries.ITEM, id("cobalt_armor_boots"), new CobaltArmorItem.Boots());
		RoboticaModItems.COBALT_ARMOR_BOOTS = () -> cobalt_armor_boots;
		Item cobalt_axe = Registry.register(BuiltInRegistries.ITEM, id("cobalt_axe"), new CobaltAxeItem());
		RoboticaModItems.COBALT_AXE = () -> cobalt_axe;
		Item cobalt_sword = Registry.register(BuiltInRegistries.ITEM, id("cobalt_sword"), new CobaltSwordItem());
		RoboticaModItems.COBALT_SWORD = () -> cobalt_sword;
		Item cobalt_shovel = Registry.register(BuiltInRegistries.ITEM, id("cobalt_shovel"), new CobaltShovelItem());
		RoboticaModItems.COBALT_SHOVEL = () -> cobalt_shovel;
		Item cobalt_hoe = Registry.register(BuiltInRegistries.ITEM, id("cobalt_hoe"), new CobaltHoeItem());
		RoboticaModItems.COBALT_HOE = () -> cobalt_hoe;
		Item cobalt_pickaxe = Registry.register(BuiltInRegistries.ITEM, id("cobalt_pickaxe"), new CobaltPickaxeItem());
		RoboticaModItems.COBALT_PICKAXE = () -> cobalt_pickaxe;
		Item orichalcum_axe = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_axe"), new OrichalcumAxeItem());
		RoboticaModItems.ORICHALCUM_AXE = () -> orichalcum_axe;
		Item orichalcum_sword = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_sword"), new OrichalcumSwordItem());
		RoboticaModItems.ORICHALCUM_SWORD = () -> orichalcum_sword;
		Item orichalcum_shovel = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_shovel"), new OrichalcumShovelItem());
		RoboticaModItems.ORICHALCUM_SHOVEL = () -> orichalcum_shovel;
		Item orichalcum_hoe = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_hoe"), new OrichalcumHoeItem());
		RoboticaModItems.ORICHALCUM_HOE = () -> orichalcum_hoe;
		Item orichalcum_pickaxe = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_pickaxe"), new OrichalcumPickaxeItem());
		RoboticaModItems.ORICHALCUM_PICKAXE = () -> orichalcum_pickaxe;
		Item scrap_block = Registry.register(BuiltInRegistries.ITEM, id("scrap_block"), new BlockItem(RoboticaModBlocks.SCRAP_BLOCK.get(), new Item.Properties()));
		RoboticaModItems.SCRAP_BLOCK = () -> scrap_block;
		Item crafting_workstation = Registry.register(BuiltInRegistries.ITEM, id("crafting_workstation"), new BlockItem(RoboticaModBlocks.CRAFTING_WORKSTATION.get(), new Item.Properties()));
		RoboticaModItems.CRAFTING_WORKSTATION = () -> crafting_workstation;
		Item scrap_slab = Registry.register(BuiltInRegistries.ITEM, id("scrap_slab"), new BlockItem(RoboticaModBlocks.SCRAP_SLAB.get(), new Item.Properties()));
		RoboticaModItems.SCRAP_SLAB = () -> scrap_slab;
		Item scrap_stairs_block = Registry.register(BuiltInRegistries.ITEM, id("scrap_stairs_block"), new BlockItem(RoboticaModBlocks.SCRAP_STAIRS_BLOCK.get(), new Item.Properties()));
		RoboticaModItems.SCRAP_STAIRS_BLOCK = () -> scrap_stairs_block;
		Item scrap_debris = Registry.register(BuiltInRegistries.ITEM, id("scrap_debris"), new BlockItem(RoboticaModBlocks.SCRAP_DEBRIS.get(), new Item.Properties()));
		RoboticaModItems.SCRAP_DEBRIS = () -> scrap_debris;
		Item titanium_ore = Registry.register(BuiltInRegistries.ITEM, id("titanium_ore"), new BlockItem(RoboticaModBlocks.TITANIUM_ORE.get(), new Item.Properties()));
		RoboticaModItems.TITANIUM_ORE = () -> titanium_ore;
		Item cobalt_ore = Registry.register(BuiltInRegistries.ITEM, id("cobalt_ore"), new BlockItem(RoboticaModBlocks.COBALT_ORE.get(), new Item.Properties()));
		RoboticaModItems.COBALT_ORE = () -> cobalt_ore;
		Item orichalcum_ore = Registry.register(BuiltInRegistries.ITEM, id("orichalcum_ore"), new BlockItem(RoboticaModBlocks.ORICHALCUM_ORE.get(), new Item.Properties()));
		RoboticaModItems.ORICHALCUM_ORE = () -> orichalcum_ore;
	}

	private void registerEntities() {
		EntityType<BoggieEntity> boggieType = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("boggie"),
				FabricEntityTypeBuilder.createMob().entityFactory(BoggieEntity::new).spawnGroup(MobCategory.MONSTER)
					.dimensions(EntityDimensions.scalable(0.6f, 1.0f)).build());
		RoboticaModEntities.BOGGIE = () -> boggieType;
		FabricDefaultAttributeRegistry.register(boggieType, BoggieEntity.createAttributes());
		EntityType<SentinelEntity> sentinelType = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("sentinel"),
				FabricEntityTypeBuilder.createMob().entityFactory(SentinelEntity::new).spawnGroup(MobCategory.MONSTER)
					.dimensions(EntityDimensions.scalable(0.6f, 3.5f)).build());
		RoboticaModEntities.SENTINEL = () -> sentinelType;
		FabricDefaultAttributeRegistry.register(sentinelType, SentinelEntity.createAttributes());
		EntityType<MechEntity> mechType = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("mech"),
				FabricEntityTypeBuilder.createMob().entityFactory(MechEntity::new).spawnGroup(MobCategory.MONSTER)
					.dimensions(EntityDimensions.scalable(2.0f, 3.7f)).build());
		RoboticaModEntities.MECH = () -> mechType;
		FabricDefaultAttributeRegistry.register(mechType, MechEntity.createAttributes());
		EntityType<SpaceShipEntity> space_shipType = Registry.register(BuiltInRegistries.ENTITY_TYPE, id("space_ship"),
				FabricEntityTypeBuilder.createMob().entityFactory(SpaceShipEntity::new).spawnGroup(MobCategory.MONSTER)
					.dimensions(EntityDimensions.scalable(4.0f, 2.0f)).build());
		RoboticaModEntities.SPACE_SHIP = () -> space_shipType;
		FabricDefaultAttributeRegistry.register(space_shipType, SpaceShipEntity.createAttributes());
		Item sentinel_spawn_egg = Registry.register(BuiltInRegistries.ITEM, id("sentinel_spawn_egg"), new SpawnEggItem(RoboticaModEntities.SENTINEL.get(), -13421773, -6710887, new Item.Properties()));
		RoboticaModItems.SENTINEL_SPAWN_EGG = () -> sentinel_spawn_egg;
		BoggieEntity.init();
		SentinelEntity.init();
		MechEntity.init();
		SpaceShipEntity.init();
	}
}
