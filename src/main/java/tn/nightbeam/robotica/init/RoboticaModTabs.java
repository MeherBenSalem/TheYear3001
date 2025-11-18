/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.RoboticaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class RoboticaModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RoboticaMod.MODID);
	public static final RegistryObject<CreativeModeTab> THE_YEAR_3001_TAB = REGISTRY.register("the_year_3001_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.robotica.the_year_3001_tab")).icon(() -> new ItemStack(RoboticaModItems.PRINTED_CIRCUIT_BOARD.get())).displayItems((parameters, tabData) -> {
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
			}).build());
	public static final RegistryObject<CreativeModeTab> THE_YEAR_3001_CARDS = REGISTRY.register("the_year_3001_cards",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.robotica.the_year_3001_cards")).icon(() -> new ItemStack(RoboticaModItems.BULK_UPGRADE_CARD.get())).displayItems((parameters, tabData) -> {
				tabData.accept(RoboticaModItems.EMPTY_CARD.get());
				tabData.accept(RoboticaModItems.DAMAGE_UPGRADE_CARD.get());
				tabData.accept(RoboticaModItems.BULK_UPGRADE_CARD.get());
				tabData.accept(RoboticaModItems.SPEED_UPGRADE_CARD.get());
				tabData.accept(RoboticaModItems.REPAIR_CARD.get());
				tabData.accept(RoboticaModItems.DEFENSE_UPGRADE_CARD.get());
			}).withTabsBefore(THE_YEAR_3001_TAB.getId()).build());
}