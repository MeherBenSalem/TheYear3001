package tn.nightbeam.robotica.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import tn.nightbeam.robotica.init.RoboticaModEntities;
import tn.nightbeam.robotica.network.FabricCraftingButtonPacket;
import tn.nightbeam.robotica.network.FabricFlyDownPacket;
import tn.nightbeam.robotica.network.FabricFlyForwardPacket;
import tn.nightbeam.robotica.network.FabricFlyUpPacket;
import tn.nightbeam.robotica.network.FabricMenuStateUpdatePacket;

public class FabricRoboticaModEvents {
	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(FabricMenuStateUpdatePacket.ID,
				(server, player, handler, buf, responseSender) -> {
					net.minecraft.network.FriendlyByteBuf copy = new net.minecraft.network.FriendlyByteBuf(buf.copy());
					server.execute(() -> FabricMenuStateUpdatePacket.handleServer(player, copy));
				});
		ServerPlayNetworking.registerGlobalReceiver(FabricCraftingButtonPacket.ID,
				(server, player, handler, buf, responseSender) -> {
					net.minecraft.network.FriendlyByteBuf copy = new net.minecraft.network.FriendlyByteBuf(buf.copy());
					server.execute(() -> FabricCraftingButtonPacket.handleServer(player, copy));
				});
		ServerPlayNetworking.registerGlobalReceiver(FabricFlyUpPacket.ID,
				(server, player, handler, buf, responseSender) -> {
					net.minecraft.network.FriendlyByteBuf copy = new net.minecraft.network.FriendlyByteBuf(buf.copy());
					server.execute(() -> FabricFlyUpPacket.handleServer(player, copy));
				});
		ServerPlayNetworking.registerGlobalReceiver(FabricFlyDownPacket.ID,
				(server, player, handler, buf, responseSender) -> {
					net.minecraft.network.FriendlyByteBuf copy = new net.minecraft.network.FriendlyByteBuf(buf.copy());
					server.execute(() -> FabricFlyDownPacket.handleServer(player, copy));
				});
		ServerPlayNetworking.registerGlobalReceiver(FabricFlyForwardPacket.ID,
				(server, player, handler, buf, responseSender) -> {
					net.minecraft.network.FriendlyByteBuf copy = new net.minecraft.network.FriendlyByteBuf(buf.copy());
					server.execute(() -> FabricFlyForwardPacket.handleServer(player, copy));
				});

		SpawnPlacements.register(RoboticaModEntities.SENTINEL.get(), SpawnPlacements.Type.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> world.getDifficulty() != Difficulty.PEACEFUL
						&& Monster.isDarkEnoughToSpawn(world, pos, random)
						&& Mob.checkMobSpawnRules(entityType, world, reason, pos, random));
	}
}
