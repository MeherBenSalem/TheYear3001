package tn.nightbeam.robotica.platform;

import io.netty.buffer.Unpooled;
import tn.nightbeam.robotica.init.ScreenAccessor;
import tn.nightbeam.robotica.network.FabricMenuStateUpdatePacket;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

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
		FabricMenuStateUpdatePacket payload = new FabricMenuStateUpdatePacket(elementType, name, elementState);
		if (player instanceof ServerPlayer serverPlayer) {
			ServerPlayNetworking.send(serverPlayer, payload);
		} else if (player.level().isClientSide) {
			ClientPlayNetworking.send(payload);
			if (needClientUpdate && Minecraft.getInstance().screen instanceof ScreenAccessor accessor) {
				accessor.updateMenuState(elementType, name, elementState);
			}
		}
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		extraDataWriter.accept(buf);
		BlockPos pos = buf.readBlockPos();
		player.openMenu(new net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory<BlockPos>() {
			@Override
			public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int syncId,
					net.minecraft.world.entity.player.Inventory inv, net.minecraft.world.entity.player.Player player) {
				return menuProvider.createMenu(syncId, inv, player);
			}

			@Override
			public net.minecraft.network.chat.Component getDisplayName() {
				return menuProvider.getDisplayName();
			}

			@Override
			public BlockPos getScreenOpeningData(ServerPlayer player) {
				return pos;
			}
		});
	}

	@Override
	public CompoundTag getPersistentData(Entity entity) {
		return PERSISTENT_DATA.computeIfAbsent(entity, e -> new CompoundTag());
	}
}
