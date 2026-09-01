package tn.nightbeam.robotica;

import tn.nightbeam.robotica.init.RoboticaModTabs;
import tn.nightbeam.robotica.init.RoboticaModSoundsForge;
import tn.nightbeam.robotica.init.RoboticaModMenusForge;
import tn.nightbeam.robotica.init.RoboticaModItemsForge;
import tn.nightbeam.robotica.init.RoboticaModFluidsForge;
import tn.nightbeam.robotica.init.RoboticaModFluidTypes;
import tn.nightbeam.robotica.init.RoboticaModEntitiesForge;
import tn.nightbeam.robotica.init.RoboticaModBlocksForge;
import tn.nightbeam.robotica.init.RoboticaModBlockEntitiesForge;
import tn.nightbeam.robotica.network.CraftingWorkStationGUIButtonMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoDownMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoForwardMessage;
import tn.nightbeam.robotica.network.FlyCapsuleGoUpMessage;
import tn.nightbeam.robotica.network.MenuStateUpdateMessage;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

@Mod(RoboticaMod.MOD_ID)
public class RoboticaModForge {
	public static final Logger LOGGER = LogManager.getLogger(RoboticaModForge.class);
	public static final String MODID = RoboticaMod.MOD_ID;

	public RoboticaModForge() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		RoboticaModSoundsForge.REGISTRY.register(bus);
		RoboticaModBlocksForge.REGISTRY.register(bus);
		RoboticaModBlockEntitiesForge.REGISTRY.register(bus);
		RoboticaModItemsForge.REGISTRY.register(bus);
		RoboticaModEntitiesForge.REGISTRY.register(bus);
		RoboticaModTabs.REGISTRY.register(bus);
		RoboticaModMenusForge.REGISTRY.register(bus);
		RoboticaModFluidsForge.REGISTRY.register(bus);
		RoboticaModFluidTypes.REGISTRY.register(bus);
		bus.addListener(this::commonSetup);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			addNetworkMessage(MenuStateUpdateMessage.class, MenuStateUpdateMessage::buffer, MenuStateUpdateMessage::new,
					MenuStateUpdateMessage::handler);
			addNetworkMessage(CraftingWorkStationGUIButtonMessage.class, CraftingWorkStationGUIButtonMessage::buffer,
					CraftingWorkStationGUIButtonMessage::new, CraftingWorkStationGUIButtonMessage::handler);
			addNetworkMessage(FlyCapsuleGoUpMessage.class, FlyCapsuleGoUpMessage::buffer, FlyCapsuleGoUpMessage::new,
					FlyCapsuleGoUpMessage::handler);
			addNetworkMessage(FlyCapsuleGoDownMessage.class, FlyCapsuleGoDownMessage::buffer, FlyCapsuleGoDownMessage::new,
					FlyCapsuleGoDownMessage::handler);
			addNetworkMessage(FlyCapsuleGoForwardMessage.class, FlyCapsuleGoForwardMessage::buffer,
					FlyCapsuleGoForwardMessage::new, FlyCapsuleGoForwardMessage::handler);
		});
	}

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder,
			Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0)
					actions.add(work);
			});
			actions.forEach(e -> e.getKey().run());
			workQueue.removeAll(actions);
		}
	}
}
