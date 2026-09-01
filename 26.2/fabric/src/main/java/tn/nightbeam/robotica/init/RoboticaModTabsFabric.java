package tn.nightbeam.robotica.init;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.robotica.RoboticaMod;

public final class RoboticaModTabsFabric {
	private RoboticaModTabsFabric() {
	}

	public static void register() {
		CreativeModeTab mainTab = FabricCreativeModeTab.builder()
				.title(Component.translatable("item_group.robotica.the_year_3001_tab"))
				.icon(() -> new ItemStack(RoboticaModItems.SCRAP.get()))
				.build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "the_year_3001_tab"), mainTab);

		CreativeModeTab cardsTab = FabricCreativeModeTab.builder()
				.title(Component.translatable("item_group.robotica.the_year_3001_cards"))
				.icon(() -> new ItemStack(RoboticaModItems.EMPTY_CARD.get()))
				.build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "the_year_3001_cards"), cardsTab);

		CreativeModeTabEvents.modifyOutputEvent(net.minecraft.resources.ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "the_year_3001_tab")))
				.register(content -> {
					content.accept(RoboticaModItems.SCRAP.get());
					content.accept(RoboticaModItems.SCRAP_BLOCK.get());
					content.accept(RoboticaModItems.CRAFTING_WORKSTATION.get());
					content.accept(RoboticaModItems.MECH_MK_1_EGG.get());
					content.accept(RoboticaModItems.BOGGIE_MK_1_EGG.get());
					content.accept(RoboticaModItems.SENTINEL_SPAWN_EGG.get());
					content.accept(RoboticaModItems.SPACE_SHIP_CAPSULE.get());
				});

		CreativeModeTabEvents.modifyOutputEvent(net.minecraft.resources.ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "the_year_3001_cards")))
				.register(content -> {
					content.accept(RoboticaModItems.EMPTY_CARD.get());
					content.accept(RoboticaModItems.REPAIR_CARD.get());
					content.accept(RoboticaModItems.BULK_UPGRADE_CARD.get());
					content.accept(RoboticaModItems.DAMAGE_UPGRADE_CARD.get());
					content.accept(RoboticaModItems.SPEED_UPGRADE_CARD.get());
					content.accept(RoboticaModItems.DEFENSE_UPGRADE_CARD.get());
				});
	}
}
