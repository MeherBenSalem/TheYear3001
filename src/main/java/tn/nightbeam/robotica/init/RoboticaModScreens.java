
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.client.gui.CraftingWorkStationGUIScreen;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RoboticaModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(RoboticaModMenus.CRAFTING_WORK_STATION_GUI.get(), CraftingWorkStationGUIScreen::new);
		});
	}
}
