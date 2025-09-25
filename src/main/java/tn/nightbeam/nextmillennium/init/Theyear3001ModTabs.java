
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
				tabData.accept(Theyear3001ModItems.OIL_BUCKET.get());
				tabData.accept(Theyear3001ModItems.SCRAP.get());
				tabData.accept(Theyear3001ModBlocks.SCRAP_BLOCK.get().asItem());
				tabData.accept(Theyear3001ModBlocks.CRAFTING_WORKSTATION.get().asItem());
				tabData.accept(Theyear3001ModItems.MECH_MK_1_EGG.get());
				tabData.accept(Theyear3001ModItems.BOGGIE_MK_1_EGG.get());
				tabData.accept(Theyear3001ModItems.PRINTED_CIRCUIT_BOARD.get());
				tabData.accept(Theyear3001ModItems.BLANK_PRINTED_CIRCUIT_BOARD.get());
				tabData.accept(Theyear3001ModItems.CENTRAL_PROCESSING_UNIT.get());
				tabData.accept(Theyear3001ModItems.ELECTRICAL_CONDUCTORS.get());
				tabData.accept(Theyear3001ModItems.MECH_MK_1_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.B_1928_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.B_1990_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.BOGGIE_MK_1_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.ROBOT_MK_1_SPAWN_EGG.get());
				tabData.accept(Theyear3001ModItems.B_2050_SPAWN_EGG.get());
			}).build());
	public static final RegistryObject<CreativeModeTab> THE_YEAR_3001_CARDS = REGISTRY.register("the_year_3001_cards",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.theyear3001.the_year_3001_cards")).icon(() -> new ItemStack(Theyear3001ModItems.BULK_UPGRADE_CARD.get())).displayItems((parameters, tabData) -> {
				tabData.accept(Theyear3001ModItems.DAMAGE_UPGRADE_CARD.get());
				tabData.accept(Theyear3001ModItems.BULK_UPGRADE_CARD.get());
				tabData.accept(Theyear3001ModItems.SPEED_UPGRADE_CARD.get());
				tabData.accept(Theyear3001ModItems.REPAIR_CARD.get());
				tabData.accept(Theyear3001ModItems.EMPTY_CARD.get());
			}).withTabsBefore(THE_YEAR_3001_TAB.getId()).build());
}
