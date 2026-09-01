package tn.nightbeam.robotica.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.robotica.RoboticaMod;

public class RoboticaModTabsFabric {
	public static CreativeModeTab THE_YEAR_3001_TAB;
	public static CreativeModeTab THE_YEAR_3001_CARDS;

	public static void register() {
		THE_YEAR_3001_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
				ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "the_year_3001_tab"),
				FabricItemGroup.builder()
						.title(Component.translatable("item_group.robotica.the_year_3001_tab"))
						.icon(() -> new ItemStack(RoboticaModItems.PRINTED_CIRCUIT_BOARD.get()))
						.displayItems((parameters, tabData) -> {
							tabData.accept(RoboticaModItems.OIL_BUCKET.get());
							tabData.accept(RoboticaModItems.SCRAP.get());
							tabData.accept(RoboticaModBlocks.SCRAP_BLOCK.get().asItem());
							tabData.accept(RoboticaModBlocks.SCRAP_SLAB.get().asItem());
							tabData.accept(RoboticaModBlocks.SCRAP_STAIRS_BLOCK.get().asItem());
							tabData.accept(RoboticaModBlocks.CRAFTING_WORKSTATION.get().asItem());
							tabData.accept(RoboticaModItems.PRINTED_CIRCUIT_BOARD.get());
							tabData.accept(RoboticaModItems.BLANK_PRINTED_CIRCUIT_BOARD.get());
							tabData.accept(RoboticaModItems.CENTRAL_PROCESSING_UNIT.get());
							tabData.accept(RoboticaModItems.ELECTRICAL_CONDUCTORS.get());
							tabData.accept(RoboticaModItems.MECH_MK_1_EGG.get());
							tabData.accept(RoboticaModItems.BOGGIE_MK_1_EGG.get());
							tabData.accept(RoboticaModBlocks.SCRAP_DEBRIS.get().asItem());
							tabData.accept(RoboticaModBlocks.TITANIUM_ORE.get().asItem());
							tabData.accept(RoboticaModItems.TITANIUM_INGOT.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_INGOT.get());
							tabData.accept(RoboticaModItems.COBALT_INGOT.get());
							tabData.accept(RoboticaModItems.RAW_TITANIUM.get());
							tabData.accept(RoboticaModItems.RAW_COBALT.get());
							tabData.accept(RoboticaModItems.RAW_ORICHALCUM.get());
							tabData.accept(RoboticaModBlocks.COBALT_ORE.get().asItem());
							tabData.accept(RoboticaModBlocks.ORICHALCUM_ORE.get().asItem());
							tabData.accept(RoboticaModItems.SCRAP_ARMOR_HELMET.get());
							tabData.accept(RoboticaModItems.SCRAP_ARMOR_CHESTPLATE.get());
							tabData.accept(RoboticaModItems.SCRAP_ARMOR_LEGGINGS.get());
							tabData.accept(RoboticaModItems.SCRAP_ARMOR_BOOTS.get());
							tabData.accept(RoboticaModItems.SCRAP_AXE.get());
							tabData.accept(RoboticaModItems.SCRAP_SWORD.get());
							tabData.accept(RoboticaModItems.SCRAP_SHOVEL.get());
							tabData.accept(RoboticaModItems.SCRAP_HOE.get());
							tabData.accept(RoboticaModItems.SCRAP_PICKAXE.get());
							tabData.accept(RoboticaModItems.TITANIUM_AXE.get());
							tabData.accept(RoboticaModItems.TITANIUM_SWORD.get());
							tabData.accept(RoboticaModItems.TITANIUM_SHOVEL.get());
							tabData.accept(RoboticaModItems.TITANIUM_HOE.get());
							tabData.accept(RoboticaModItems.TITANIUM_PICKAXE.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_ARMOR_HELMET.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_ARMOR_CHESTPLATE.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_ARMOR_LEGGINGS.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_ARMOR_BOOTS.get());
							tabData.accept(RoboticaModItems.TITANIUM_ARMOR_HELMET.get());
							tabData.accept(RoboticaModItems.TITANIUM_ARMOR_CHESTPLATE.get());
							tabData.accept(RoboticaModItems.TITANIUM_ARMOR_LEGGINGS.get());
							tabData.accept(RoboticaModItems.TITANIUM_ARMOR_BOOTS.get());
							tabData.accept(RoboticaModItems.COBALT_ARMOR_HELMET.get());
							tabData.accept(RoboticaModItems.COBALT_ARMOR_CHESTPLATE.get());
							tabData.accept(RoboticaModItems.COBALT_ARMOR_LEGGINGS.get());
							tabData.accept(RoboticaModItems.COBALT_ARMOR_BOOTS.get());
							tabData.accept(RoboticaModItems.COBALT_AXE.get());
							tabData.accept(RoboticaModItems.COBALT_SWORD.get());
							tabData.accept(RoboticaModItems.COBALT_SHOVEL.get());
							tabData.accept(RoboticaModItems.COBALT_HOE.get());
							tabData.accept(RoboticaModItems.COBALT_PICKAXE.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_AXE.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_SWORD.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_SHOVEL.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_HOE.get());
							tabData.accept(RoboticaModItems.ORICHALCUM_PICKAXE.get());
							tabData.accept(RoboticaModItems.SENTINEL_SPAWN_EGG.get());
							tabData.accept(RoboticaModItems.SPACE_SHIP_CAPSULE.get());
						})
						.build());

		THE_YEAR_3001_CARDS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
				ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "the_year_3001_cards"),
				FabricItemGroup.builder()
						.title(Component.translatable("item_group.robotica.the_year_3001_cards"))
						.icon(() -> new ItemStack(RoboticaModItems.BULK_UPGRADE_CARD.get()))
						.displayItems((parameters, tabData) -> {
							tabData.accept(RoboticaModItems.EMPTY_CARD.get());
							tabData.accept(RoboticaModItems.DAMAGE_UPGRADE_CARD.get());
							tabData.accept(RoboticaModItems.BULK_UPGRADE_CARD.get());
							tabData.accept(RoboticaModItems.SPEED_UPGRADE_CARD.get());
							tabData.accept(RoboticaModItems.REPAIR_CARD.get());
							tabData.accept(RoboticaModItems.DEFENSE_UPGRADE_CARD.get());
						})
						.build());
	}
}
