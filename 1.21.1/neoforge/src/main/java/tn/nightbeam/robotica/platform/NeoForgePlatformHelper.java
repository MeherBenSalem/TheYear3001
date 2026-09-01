package tn.nightbeam.robotica.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
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
		return !FMLLoader.isProduction();
	}

	@Override
	public void sendMenuUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
		NeoForgeNetworking.MenuUpdatePayload payload = new NeoForgeNetworking.MenuUpdatePayload(elementType, name, elementState);
		if (player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, payload);
		} else if (player.level().isClientSide) {
			if (needClientUpdate && Minecraft.getInstance().screen instanceof ScreenAccessor accessor) {
				accessor.updateMenuState(elementType, name, elementState);
			}
			PacketDistributor.sendToServer(payload);
		}
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menuProvider, java.util.function.Consumer<FriendlyByteBuf> extraDataWriter) {
		player.openMenu(menuProvider, (RegistryFriendlyByteBuf buf) -> extraDataWriter.accept(buf));
	}

	@Override
	public CompoundTag getPersistentData(Entity entity) {
		return entity.getPersistentData();
	}
}
