package tn.nightbeam.robotica.platform;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;

public interface IPlatformHelper {
	String getPlatformName();

	boolean isModLoaded(String modId);

	boolean isDevelopmentEnvironment();

	default String getEnvironmentName() {
		return isDevelopmentEnvironment() ? "development" : "production";
	}

	void sendMenuUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate);

	void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter);

	CompoundTag getPersistentData(Entity entity);
}
