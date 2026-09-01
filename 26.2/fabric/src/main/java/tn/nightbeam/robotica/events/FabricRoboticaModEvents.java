package tn.nightbeam.robotica.events;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import tn.nightbeam.robotica.init.MenuAccessor;
import tn.nightbeam.robotica.network.CraftingWorkStationGUIButtonMessage;
import tn.nightbeam.robotica.network.FabricCraftingButtonPayload;
import tn.nightbeam.robotica.network.FabricFlyDownPayload;
import tn.nightbeam.robotica.network.FabricFlyForwardPayload;
import tn.nightbeam.robotica.network.FabricFlyUpPayload;
import tn.nightbeam.robotica.network.FabricMenuStateUpdatePayload;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;

public final class FabricRoboticaModEvents {
	private FabricRoboticaModEvents() {
	}

	public static void register() {
		PayloadTypeRegistry.serverboundPlay().register(FabricMenuStateUpdatePayload.TYPE, FabricMenuStateUpdatePayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(FabricCraftingButtonPayload.TYPE, FabricCraftingButtonPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(FabricFlyUpPayload.TYPE, FabricFlyUpPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(FabricFlyDownPayload.TYPE, FabricFlyDownPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(FabricFlyForwardPayload.TYPE, FabricFlyForwardPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(FabricMenuStateUpdatePayload.TYPE, FabricMenuStateUpdatePayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(FabricMenuStateUpdatePayload.TYPE, (payload, context) -> context.server().execute(() -> {
			if (context.player().containerMenu instanceof MenuAccessor menu) {
				menu.getMenuState().put(payload.elementType() + ":" + payload.name(), payload.elementState());
			}
		}));

		ServerPlayNetworking.registerGlobalReceiver(FabricCraftingButtonPayload.TYPE, (payload, context) -> context.server().execute(() ->
				CraftingWorkStationGUIButtonMessage.handleButtonAction(context.player(), payload.buttonID(), payload.x(), payload.y(), payload.z())));

		ServerPlayNetworking.registerGlobalReceiver(FabricFlyUpPayload.TYPE, (payload, context) -> context.server().execute(() ->
				FlyCapsuleGoUpMessage.pressAction(context.player(), payload.pressAction(), payload.pressedms())));

		ServerPlayNetworking.registerGlobalReceiver(FabricFlyDownPayload.TYPE, (payload, context) -> context.server().execute(() ->
				FlyCapsuleGoDownMessage.pressAction(context.player(), payload.pressAction(), payload.pressedms())));

		ServerPlayNetworking.registerGlobalReceiver(FabricFlyForwardPayload.TYPE, (payload, context) -> context.server().execute(() ->
				FlyCapsuleGoForwardMessage.pressAction(context.player(), payload.pressAction(), payload.pressedms())));
	}
}
