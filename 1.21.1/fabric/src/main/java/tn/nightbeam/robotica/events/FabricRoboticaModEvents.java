package tn.nightbeam.robotica.events;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import tn.nightbeam.robotica.init.MenuAccessor;
import tn.nightbeam.robotica.network.CraftingWorkStationGUIButtonMessage;
import tn.nightbeam.robotica.network.FabricCraftingButtonPayload;
import tn.nightbeam.robotica.network.FabricFlyDownPayload;
import tn.nightbeam.robotica.network.FabricFlyForwardPayload;
import tn.nightbeam.robotica.network.FabricFlyUpPayload;
import tn.nightbeam.robotica.network.FabricMenuStateUpdatePacket;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;
import tn.nightbeam.robotica.init.RoboticaModEntities;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

public class FabricRoboticaModEvents {
	public static void register() {
		PayloadTypeRegistry.playC2S().register(FabricMenuStateUpdatePacket.TYPE, FabricMenuStateUpdatePacket.CODEC);
		PayloadTypeRegistry.playS2C().register(FabricMenuStateUpdatePacket.TYPE, FabricMenuStateUpdatePacket.CODEC);
		PayloadTypeRegistry.playC2S().register(FabricCraftingButtonPayload.TYPE, FabricCraftingButtonPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(FabricFlyUpPayload.TYPE, FabricFlyUpPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(FabricFlyDownPayload.TYPE, FabricFlyDownPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(FabricFlyForwardPayload.TYPE, FabricFlyForwardPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(FabricMenuStateUpdatePacket.TYPE,
				(payload, context) -> context.server().execute(() -> handleMenuStateUpdate(payload, context.player())));
		ServerPlayNetworking.registerGlobalReceiver(FabricCraftingButtonPayload.TYPE, (payload, context) -> context.server().execute(() ->
				CraftingWorkStationGUIButtonMessage.handleButtonAction(context.player(), payload.buttonID(), payload.x(), payload.y(), payload.z())));
		ServerPlayNetworking.registerGlobalReceiver(FabricFlyUpPayload.TYPE, (payload, context) -> context.server().execute(() ->
				FlyCapsuleGoUpMessage.pressAction(context.player(), payload.pressAction(), payload.pressedms())));
		ServerPlayNetworking.registerGlobalReceiver(FabricFlyDownPayload.TYPE, (payload, context) -> context.server().execute(() ->
				FlyCapsuleGoDownMessage.pressAction(context.player(), payload.pressAction(), payload.pressedms())));
		ServerPlayNetworking.registerGlobalReceiver(FabricFlyForwardPayload.TYPE, (payload, context) -> context.server().execute(() ->
				FlyCapsuleGoForwardMessage.pressAction(context.player(), payload.pressAction(), payload.pressedms())));

		registerSpawnPlacements();
	}

	public static void registerSpawnPlacements() {
		SpawnPlacements.register(RoboticaModEntities.SENTINEL.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> world.getDifficulty() != Difficulty.PEACEFUL
						&& Monster.isDarkEnoughToSpawn(world, pos, random)
						&& Mob.checkMobSpawnRules(entityType, world, reason, pos, random));
	}

	public static void handleMenuStateUpdate(FabricMenuStateUpdatePacket payload, net.minecraft.server.level.ServerPlayer player) {
		if (payload.name().length() > 256 || payload.elementState() instanceof String string && string.length() > 8192) {
			return;
		}
		if (player.containerMenu instanceof MenuAccessor menu) {
			menu.getMenuState().put(payload.elementType() + ":" + payload.name(), payload.elementState());
		}
	}
}
