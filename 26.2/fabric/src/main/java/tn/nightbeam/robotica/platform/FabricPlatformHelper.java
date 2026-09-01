package tn.nightbeam.robotica.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tn.nightbeam.robotica.init.ScreenAccessor;
import tn.nightbeam.robotica.network.FabricMenuStateUpdatePayload;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class FabricPlatformHelper implements IPlatformHelper {
	private static final Map<Entity, CompoundTag> PERSISTENT_DATA = new WeakHashMap<>();

	@Override
	public String getPlatformName() {
		return "Fabric";
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public void sendMenuUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
		if (player instanceof ServerPlayer serverPlayer) {
			ServerPlayNetworking.send(serverPlayer, new FabricMenuStateUpdatePayload(elementType, name, elementState));
		} else if (player.level().isClientSide()) {
			ClientPlayNetworking.send(new FabricMenuStateUpdatePayload(elementType, name, elementState));
			if (needClientUpdate && net.minecraft.client.Minecraft.getInstance().gui.screen() instanceof ScreenAccessor accessor) {
				accessor.updateMenuState(elementType, name, elementState);
			}
		}
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
		player.openMenu(new ExtendedMenuProvider<net.minecraft.core.BlockPos>() {
			@Override
			public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int syncId, net.minecraft.world.entity.player.Inventory inv, Player playerEntity) {
				return menuProvider.createMenu(syncId, inv, playerEntity);
			}

			@Override
			public net.minecraft.network.chat.Component getDisplayName() {
				return menuProvider.getDisplayName();
			}

			@Override
			public net.minecraft.core.BlockPos getScreenOpeningData(ServerPlayer serverPlayer) {
				return serverPlayer.blockPosition();
			}
		});
	}

	@Override
	public CompoundTag getPersistentData(Entity entity) {
		return PERSISTENT_DATA.computeIfAbsent(entity, e -> new CompoundTag());
	}
}
