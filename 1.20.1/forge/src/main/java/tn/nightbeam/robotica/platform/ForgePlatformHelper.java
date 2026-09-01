package tn.nightbeam.robotica.platform;

import tn.nightbeam.robotica.RoboticaModForge;
import tn.nightbeam.robotica.init.ScreenAccessor;
import tn.nightbeam.robotica.network.MenuStateUpdateMessage;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Consumer;

public class ForgePlatformHelper implements IPlatformHelper {
	@Override
	public String getPlatformName() {
		return "Forge";
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FMLLoader.getLoadingModList().getModFileById(modId) != null;
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public void sendMenuUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
		if (player instanceof ServerPlayer serverPlayer) {
			RoboticaModForge.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
					new MenuStateUpdateMessage(elementType, name, elementState));
		} else if (player.level().isClientSide) {
			if (needClientUpdate && Minecraft.getInstance().screen instanceof ScreenAccessor accessor) {
				accessor.updateMenuState(elementType, name, elementState);
			}
			RoboticaModForge.PACKET_HANDLER.sendToServer(new MenuStateUpdateMessage(elementType, name, elementState));
		}
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
		NetworkHooks.openScreen(player, menuProvider, extraDataWriter);
	}

	@Override
	public CompoundTag getPersistentData(Entity entity) {
		return entity.getPersistentData();
	}
}
