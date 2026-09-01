package tn.nightbeam.robotica.init;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.client.gui.CraftingWorkStationGUIScreen;
import tn.nightbeam.robotica.neoforge.NeoForgeNetworking;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;

@EventBusSubscriber(modid = RoboticaMod.MOD_ID, value = Dist.CLIENT)
public class RoboticaModClientNeoForge {
	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(RoboticaModKeyMappings.FLY_CAPSULE_GO_UP);
		event.register(RoboticaModKeyMappings.FLY_CAPSULE_GO_DOWN);
		event.register(RoboticaModKeyMappings.FLY_CAPSULE_GO_FORWARD);
	}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(RoboticaModMenusNeoForge.CRAFTING_WORK_STATION_GUI.get(), CraftingWorkStationGUIScreen::new);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (Minecraft.getInstance().gui.screen() != null) {
			return;
		}
		while (RoboticaModKeyMappings.FLY_CAPSULE_GO_UP.consumeClick()) {
			ClientPacketDistributor.sendToServer(new NeoForgeNetworking.FlyUpPayload(0, 0));
			FlyCapsuleGoUpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
		}
		while (RoboticaModKeyMappings.FLY_CAPSULE_GO_DOWN.consumeClick()) {
			ClientPacketDistributor.sendToServer(new NeoForgeNetworking.FlyDownPayload(0, 0));
			FlyCapsuleGoDownMessage.pressAction(Minecraft.getInstance().player, 0, 0);
		}
		while (RoboticaModKeyMappings.FLY_CAPSULE_GO_FORWARD.consumeClick()) {
			ClientPacketDistributor.sendToServer(new NeoForgeNetworking.FlyForwardPayload(0, 0));
			FlyCapsuleGoForwardMessage.pressAction(Minecraft.getInstance().player, 0, 0);
		}
	}
}
