package tn.nightbeam.robotica;

import tn.nightbeam.robotica.RoboticaMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tn.nightbeam.robotica.init.RoboticaModBlockEntitiesNeoForge;
import tn.nightbeam.robotica.init.RoboticaModBlocksNeoForge;
import tn.nightbeam.robotica.init.RoboticaModEntitiesNeoForge;
import tn.nightbeam.robotica.init.RoboticaModFluidTypes;
import tn.nightbeam.robotica.init.RoboticaModFluidsNeoForge;
import tn.nightbeam.robotica.init.RoboticaModItemsNeoForge;
import tn.nightbeam.robotica.init.RoboticaModMenusNeoForge;
import tn.nightbeam.robotica.init.RoboticaModSoundsNeoForge;
import tn.nightbeam.robotica.init.RoboticaModTabs;
import tn.nightbeam.robotica.neoforge.NeoForgeNetworking;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(RoboticaMod.MOD_ID)
public class RoboticaModNeoForge {
	public static final Logger LOGGER = LogManager.getLogger(RoboticaModNeoForge.class);

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RoboticaMod.MOD_ID);

	public RoboticaModNeoForge(IEventBus modEventBus) {
		RoboticaModSoundsNeoForge.REGISTRY.register(modEventBus);
		RoboticaModBlocksNeoForge.REGISTRY.register(modEventBus);
		RoboticaModBlockEntitiesNeoForge.REGISTRY.register(modEventBus);
		RoboticaModItemsNeoForge.REGISTRY.register(modEventBus);
		RoboticaModEntitiesNeoForge.REGISTRY.register(modEventBus);
		RoboticaModTabs.REGISTRY.register(modEventBus);
		RoboticaModMenusNeoForge.REGISTRY.register(modEventBus);
		RoboticaModFluidsNeoForge.REGISTRY.register(modEventBus);
		RoboticaModFluidTypes.REGISTRY.register(modEventBus);
		CREATIVE_TABS.register(modEventBus);

		modEventBus.addListener(NeoForgeNetworking::register);
		NeoForge.EVENT_BUS.register(this);
	}

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event) {
		List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setValue(work.getValue() - 1);
			if (work.getValue() == 0) {
				actions.add(work);
			}
		});
		actions.forEach(e -> e.getKey().run());
		workQueue.removeAll(actions);
	}
}
