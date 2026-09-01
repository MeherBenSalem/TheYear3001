package tn.nightbeam.robotica.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.client.gui.CraftingWorkStationGUIScreen;

@EventBusSubscriber(modid = RoboticaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RoboticaModScreens {
	@SubscribeEvent
	public static void register(RegisterMenuScreensEvent event) {
		event.register(RoboticaModMenusForge.CRAFTING_WORK_STATION_GUI.get(), CraftingWorkStationGUIScreen::new);
	}
}
