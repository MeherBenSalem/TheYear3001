
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.Theyear3001Mod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class Theyear3001ModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Theyear3001Mod.MODID);
	public static final RegistryObject<CreativeModeTab> THE_YEAR_3001_TAB = REGISTRY.register("the_year_3001_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.theyear3001.the_year_3001_tab")).icon(() -> new ItemStack(Theyear3001ModBlocks.CRAFTING_WORKSTATION.get())).displayItems((parameters, tabData) -> {
				tabData.accept(Theyear3001ModItems.B_1928_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.B_1990_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.B_2050_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.SENTINEL_MK_1_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.FLYING_CAPSULE_MECH_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.OIL_BUCKET.get());
				tabData.accept(Theyear3001ModItems.SCRAP.get());
				tabData.accept(Theyear3001ModBlocks.SCRAP_BLOCK.get().asItem());
				tabData.accept(Theyear3001ModBlocks.SCRAP_SLAB.get().asItem());
				tabData.accept(Theyear3001ModBlocks.SCRAP_STAIRS_BLOCK.get().asItem());
				tabData.accept(Theyear3001ModBlocks.CRAFTING_WORKSTATION.get().asItem());
				tabData.accept(Theyear3001ModItems.PRINTED_CIRCUIT_BOARD.get());
				tabData.accept(Theyear3001ModItems.BLANK_PRINTED_CIRCUIT_BOARD.get());
				tabData.accept(Theyear3001ModItems.CENTRAL_PROCESSING_UNIT.get());
				tabData.accept(Theyear3001ModItems.ELECTRICAL_CONDUCTORS.get());
				tabData.accept(Theyear3001ModItems.MECH_MK_1_EGG.get());
				tabData.accept(Theyear3001ModItems.BOGGIE_MK_1_EGG.get());
				tabData.accept(Theyear3001ModItems.FLYING_CAPSULE.get());
				tabData.accept(Theyear3001ModBlocks.SCRAP_DEBRIS.get().asItem());
				tabData.accept(Theyear3001ModBlocks.TITANIUM_ORE.get().asItem());
				tabData.accept(Theyear3001ModItems.TITANIUM_INGOT.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_INGOT.get());
				tabData.accept(Theyear3001ModItems.COBALT_INGOT.get());
				tabData.accept(Theyear3001ModItems.RAW_TITANIUM.get());
				tabData.accept(Theyear3001ModItems.RAW_COBALT.get());
				tabData.accept(Theyear3001ModItems.RAW_ORICHALCUM.get());
				tabData.accept(Theyear3001ModBlocks.COBALT_ORE.get().asItem());
				tabData.accept(Theyear3001ModBlocks.ORICHALCUM_ORE.get().asItem());
				tabData.accept(Theyear3001ModItems.SCRAP_ARMOR_HELMET.get());
				tabData.accept(Theyear3001ModItems.SCRAP_ARMOR_CHESTPLATE.get());
				tabData.accept(Theyear3001ModItems.SCRAP_ARMOR_LEGGINGS.get());
				tabData.accept(Theyear3001ModItems.SCRAP_ARMOR_BOOTS.get());
				tabData.accept(Theyear3001ModItems.SCRAP_STICK.get());
				tabData.accept(Theyear3001ModItems.SCRAP_AXE.get());
				tabData.accept(Theyear3001ModItems.SCRAP_SWORD.get());
				tabData.accept(Theyear3001ModItems.SCRAP_SHOVEL.get());
				tabData.accept(Theyear3001ModItems.SCRAP_HOE.get());
				tabData.accept(Theyear3001ModItems.SCRAP_PICKAXE.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_ROD.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_AXE.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_SWORD.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_SHOVEL.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_HOE.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_PICKAXE.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_ARMOR_HELMET.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_ARMOR_CHESTPLATE.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_ARMOR_LEGGINGS.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_ARMOR_BOOTS.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_ARMOR_HELMET.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_ARMOR_CHESTPLATE.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_ARMOR_LEGGINGS.get());
				tabData.accept(Theyear3001ModItems.TITANIUM_ARMOR_BOOTS.get());
				tabData.accept(Theyear3001ModItems.COBALT_ARMOR_HELMET.get());
				tabData.accept(Theyear3001ModItems.COBALT_ARMOR_CHESTPLATE.get());
				tabData.accept(Theyear3001ModItems.COBALT_ARMOR_LEGGINGS.get());
				tabData.accept(Theyear3001ModItems.COBALT_ARMOR_BOOTS.get());
				tabData.accept(Theyear3001ModItems.COBALT_AXE.get());
				tabData.accept(Theyear3001ModItems.COBALT_SWORD.get());
				tabData.accept(Theyear3001ModItems.COBALT_SHOVEL.get());
				tabData.accept(Theyear3001ModItems.COBALT_HOE.get());
				tabData.accept(Theyear3001ModItems.COBALT_PICKAXE.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_AXE.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_SWORD.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_SHOVEL.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_HOE.get());
				tabData.accept(Theyear3001ModItems.ORICHALCUM_PICKAXE.get());
			}).build());
	public static final RegistryObject<CreativeModeTab> THE_YEAR_3001_CARDS = REGISTRY.register("the_year_3001_cards",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.theyear3001.the_year_3001_cards")).icon(() -> new ItemStack(Theyear3001ModItems.BULK_UPGRADE_CARD.get())).displayItems((parameters, tabData) -> {
				tabData.accept(Theyear3001ModItems.EMPTY_CARD.get());
				tabData.accept(Theyear3001ModItems.DAMAGE_UPGRADE_CARD.get());
				tabData.accept(Theyear3001ModItems.BULK_UPGRADE_CARD.get());
				tabData.accept(Theyear3001ModItems.SPEED_UPGRADE_CARD.get());
				tabData.accept(Theyear3001ModItems.REPAIR_CARD.get());
				tabData.accept(Theyear3001ModItems.DEFENSE_UPGRADE_CARD.get());
			}).withTabsBefore(THE_YEAR_3001_TAB.getId()).build());
}
