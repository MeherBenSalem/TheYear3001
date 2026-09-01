package tn.nightbeam.robotica.platform;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import tn.nightbeam.robotica.init.ScreenAccessor;
import tn.nightbeam.robotica.neoforge.NeoForgeNetworking;

import java.util.function.Consumer;

public class NeoForgePlatformHelper implements IPlatformHelper {
	@Override
	public String getPlatformName() {
		return "NeoForge";
	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return true;
	}

	@Override
	public void sendMenuUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
		if (player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, new NeoForgeNetworking.MenuUpdatePayload(elementType, name, elementState));
		} else if (player.level().isClientSide()) {
			if (needClientUpdate && net.minecraft.client.Minecraft.getInstance().gui.screen() instanceof ScreenAccessor accessor) {
				accessor.updateMenuState(elementType, name, elementState);
			}
			ClientPacketDistributor.sendToServer(new NeoForgeNetworking.MenuUpdatePayload(elementType, name, elementState));
		}
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
		player.openMenu(menuProvider);
	}

	@Override
	public CompoundTag getPersistentData(Entity entity) {
		return entity.getPersistentData();
	}
}
