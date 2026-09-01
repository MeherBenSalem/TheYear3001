package tn.nightbeam.robotica.neoforge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
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

public final class NeoForgeNetworking {
	public static final String PROTOCOL_VERSION = "1";

	public static final Identifier MENU_UPDATE_ID = Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "menu_update");
	public static final Identifier CRAFTING_BUTTON_ID = Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "crafting_button");
	public static final Identifier FLY_UP_ID = Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_up");
	public static final Identifier FLY_DOWN_ID = Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_down");
	public static final Identifier FLY_FORWARD_ID = Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, "fly_forward");

	private NeoForgeNetworking() {
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(RoboticaMod.MOD_ID).versioned(PROTOCOL_VERSION);

		registrar.playBidirectional(MenuUpdatePayload.TYPE, MenuUpdatePayload.CODEC, NeoForgeNetworking::handleMenuUpdateClient, NeoForgeNetworking::handleMenuUpdateServer);
		registrar.playToServer(CraftingButtonPayload.TYPE, CraftingButtonPayload.CODEC, NeoForgeNetworking::handleCraftingButton);
		registrar.playToServer(FlyUpPayload.TYPE, FlyUpPayload.CODEC, NeoForgeNetworking::handleFlyUp);
		registrar.playToServer(FlyDownPayload.TYPE, FlyDownPayload.CODEC, NeoForgeNetworking::handleFlyDown);
		registrar.playToServer(FlyForwardPayload.TYPE, FlyForwardPayload.CODEC, NeoForgeNetworking::handleFlyForward);
	}

	public record MenuUpdatePayload(int elementType, String name, Object elementState) implements CustomPacketPayload {
		public static final Type<MenuUpdatePayload> TYPE = new Type<>(MENU_UPDATE_ID);
		public static final StreamCodec<FriendlyByteBuf, MenuUpdatePayload> CODEC = StreamCodec.of(MenuUpdatePayload::encode, MenuUpdatePayload::decode);

		private static void encode(FriendlyByteBuf buf, MenuUpdatePayload payload) {
			buf.writeInt(payload.elementType);
			buf.writeUtf(payload.name);
			if (payload.elementType == 0) {
				buf.writeUtf((String) payload.elementState);
			} else if (payload.elementType == 1) {
				buf.writeBoolean((Boolean) payload.elementState);
			}
		}

		private static MenuUpdatePayload decode(FriendlyByteBuf buf) {
			int elementType = buf.readInt();
			String name = buf.readUtf();
			Object state = null;
			if (elementType == 0) {
				state = buf.readUtf();
			} else if (elementType == 1) {
				state = buf.readBoolean();
			}
			return new MenuUpdatePayload(elementType, name, state);
		}

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record CraftingButtonPayload(int buttonID, int x, int y, int z) implements CustomPacketPayload {
		public static final Type<CraftingButtonPayload> TYPE = new Type<>(CRAFTING_BUTTON_ID);
		public static final StreamCodec<FriendlyByteBuf, CraftingButtonPayload> CODEC = StreamCodec.of(
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

	public record FlyUpPayload(int pressAction, int pressedms) implements CustomPacketPayload {
		public static final Type<FlyUpPayload> TYPE = new Type<>(FLY_UP_ID);
		public static final StreamCodec<FriendlyByteBuf, FlyUpPayload> CODEC = StreamCodec.of(
				(buf, payload) -> {
					buf.writeInt(payload.pressAction);
					buf.writeInt(payload.pressedms);
				},
				buf -> new FlyUpPayload(buf.readInt(), buf.readInt()));

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record FlyDownPayload(int pressAction, int pressedms) implements CustomPacketPayload {
		public static final Type<FlyDownPayload> TYPE = new Type<>(FLY_DOWN_ID);
		public static final StreamCodec<FriendlyByteBuf, FlyDownPayload> CODEC = StreamCodec.of(
				(buf, payload) -> {
					buf.writeInt(payload.pressAction);
					buf.writeInt(payload.pressedms);
				},
				buf -> new FlyDownPayload(buf.readInt(), buf.readInt()));

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record FlyForwardPayload(int pressAction, int pressedms) implements CustomPacketPayload {
		public static final Type<FlyForwardPayload> TYPE = new Type<>(FLY_FORWARD_ID);
		public static final StreamCodec<FriendlyByteBuf, FlyForwardPayload> CODEC = StreamCodec.of(
				(buf, payload) -> {
					buf.writeInt(payload.pressAction);
					buf.writeInt(payload.pressedms);
				},
				buf -> new FlyForwardPayload(buf.readInt(), buf.readInt()));

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	private static void handleMenuUpdateServer(MenuUpdatePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer && serverPlayer.containerMenu instanceof MenuAccessor menu) {
				menu.getMenuState().put(payload.elementType + ":" + payload.name, payload.elementState);
			}
		});
	}

	private static void handleMenuUpdateClient(MenuUpdatePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (net.minecraft.client.Minecraft.getInstance().gui.screen() instanceof ScreenAccessor accessor) {
				accessor.updateMenuState(payload.elementType, payload.name, payload.elementState);
			}
		});
	}

	private static void handleCraftingButton(CraftingButtonPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				CraftingWorkStationGUIButtonMessage.handleButtonAction(serverPlayer, payload.buttonID, payload.x, payload.y, payload.z);
			}
		});
	}

	private static void handleFlyUp(FlyUpPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				FlyCapsuleGoUpMessage.pressAction(serverPlayer, payload.pressAction, payload.pressedms);
			}
		});
	}

	private static void handleFlyDown(FlyDownPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				FlyCapsuleGoDownMessage.pressAction(serverPlayer, payload.pressAction, payload.pressedms);
			}
		});
	}

	private static void handleFlyForward(FlyForwardPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				FlyCapsuleGoForwardMessage.pressAction(serverPlayer, payload.pressAction, payload.pressedms);
			}
		});
	}
}
