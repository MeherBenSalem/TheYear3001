package tn.nightbeam.robotica.neoforge;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.init.MenuAccessor;
import tn.nightbeam.robotica.init.ScreenAccessor;
import tn.nightbeam.robotica.network.CraftingWorkStationGUIButtonMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;

public class NeoForgeNetworking {
	public static final String PROTOCOL_VERSION = "1";

	public static final ResourceLocation MENU_UPDATE_ID = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "menu_update");
	public static final ResourceLocation CRAFTING_BUTTON_ID = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "crafting_button");
	public static final ResourceLocation FLY_UP_ID = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_up");
	public static final ResourceLocation FLY_DOWN_ID = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_down");
	public static final ResourceLocation FLY_FORWARD_ID = ResourceLocation.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_forward");

	public static void register(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(RoboticaMod.MOD_ID).versioned(PROTOCOL_VERSION);
		registrar.playBidirectional(MenuUpdatePayload.TYPE, MenuUpdatePayload.CODEC, NeoForgeNetworking::handleMenuUpdate);
		registrar.playToServer(CraftingButtonPayload.TYPE, CraftingButtonPayload.CODEC, NeoForgeNetworking::handleCraftingButton);
		registrar.playToServer(FlyCapsulePayload.TYPE, FlyCapsulePayload.CODEC, NeoForgeNetworking::handleFlyCapsule);
	}

	public record MenuUpdatePayload(int elementType, String name, Object elementState) implements CustomPacketPayload {
		public static final Type<MenuUpdatePayload> TYPE = new Type<>(MENU_UPDATE_ID);
		public static final StreamCodec<RegistryFriendlyByteBuf, MenuUpdatePayload> CODEC = StreamCodec.of(
				(buf, payload) -> {
					buf.writeInt(payload.elementType);
					buf.writeUtf(payload.name);
					if (payload.elementType == 0) {
						buf.writeUtf((String) payload.elementState);
					} else if (payload.elementType == 1) {
						buf.writeBoolean((Boolean) payload.elementState);
					}
				},
				buf -> {
					int elementType = buf.readInt();
					String name = buf.readUtf();
					Object state = null;
					if (elementType == 0) {
						state = buf.readUtf();
					} else if (elementType == 1) {
						state = buf.readBoolean();
					}
					return new MenuUpdatePayload(elementType, name, state);
				});

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record CraftingButtonPayload(int buttonID, int x, int y, int z) implements CustomPacketPayload {
		public static final Type<CraftingButtonPayload> TYPE = new Type<>(CRAFTING_BUTTON_ID);
		public static final StreamCodec<RegistryFriendlyByteBuf, CraftingButtonPayload> CODEC = StreamCodec.of(
				(buf, payload) -> {
					buf.writeInt(payload.buttonID);
					buf.writeInt(payload.x);
					buf.writeInt(payload.y);
					buf.writeInt(payload.z);
				},
				buf -> new CraftingButtonPayload(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record FlyCapsulePayload(String action, int pressType, int pressedms) implements CustomPacketPayload {
		public static final Type<FlyCapsulePayload> TYPE = new Type<>(FLY_UP_ID);
		public static final StreamCodec<RegistryFriendlyByteBuf, FlyCapsulePayload> CODEC = StreamCodec.of(
				(buf, payload) -> {
					buf.writeUtf(payload.action);
					buf.writeInt(payload.pressType);
					buf.writeInt(payload.pressedms);
				},
				buf -> new FlyCapsulePayload(buf.readUtf(), buf.readInt(), buf.readInt()));

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	private static void handleMenuUpdate(MenuUpdatePayload payload, IPayloadContext context) {
		if (payload.name.length() > 256 || payload.elementState instanceof String string && string.length() > 8192) {
			return;
		}
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				if (serverPlayer.containerMenu instanceof MenuAccessor menu) {
					menu.getMenuState().put(payload.elementType + ":" + payload.name, payload.elementState);
				}
			} else if (net.minecraft.client.Minecraft.getInstance().screen instanceof ScreenAccessor accessor) {
				accessor.updateMenuState(payload.elementType, payload.name, payload.elementState);
			}
		});
	}

	private static void handleCraftingButton(CraftingButtonPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				CraftingWorkStationGUIButtonMessage.handleButtonAction(serverPlayer, payload.buttonID(), payload.x(), payload.y(), payload.z());
			}
		});
	}

	private static void handleFlyCapsule(FlyCapsulePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				switch (payload.action()) {
					case "up" -> FlyCapsuleGoUpMessage.pressAction(serverPlayer, payload.pressType(), payload.pressedms());
					case "down" -> FlyCapsuleGoDownMessage.pressAction(serverPlayer, payload.pressType(), payload.pressedms());
					case "forward" -> FlyCapsuleGoForwardMessage.pressAction(serverPlayer, payload.pressType(), payload.pressedms());
					default -> {
					}
				}
			}
		});
	}
}
