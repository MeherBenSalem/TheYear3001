package tn.nightbeam.robotica;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tn.nightbeam.robotica.init.RoboticaModBlockEntitiesForge;
import tn.nightbeam.robotica.init.RoboticaModBlocksForge;
import tn.nightbeam.robotica.init.RoboticaModEntitiesForge;
import tn.nightbeam.robotica.init.RoboticaModFluidTypes;
import tn.nightbeam.robotica.init.RoboticaModFluidsForge;
import tn.nightbeam.robotica.init.RoboticaModItemsForge;
import tn.nightbeam.robotica.init.RoboticaModMenusForge;
import tn.nightbeam.robotica.init.RoboticaModSoundsForge;
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
	public static final String MODID = RoboticaMod.MOD_ID;

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public RoboticaModNeoForge(IEventBus modEventBus) {
		NeoForge.EVENT_BUS.register(this);
		RoboticaModSoundsForge.REGISTRY.register(modEventBus);
		RoboticaModBlocksForge.REGISTRY.register(modEventBus);
		RoboticaModBlockEntitiesForge.REGISTRY.register(modEventBus);
		RoboticaModItemsForge.REGISTRY.register(modEventBus);
		RoboticaModEntitiesForge.REGISTRY.register(modEventBus);
		RoboticaModTabs.REGISTRY.register(modEventBus);
		RoboticaModMenusForge.REGISTRY.register(modEventBus);
		RoboticaModFluidsForge.REGISTRY.register(modEventBus);
		RoboticaModFluidTypes.REGISTRY.register(modEventBus);
		modEventBus.addListener(NeoForgeNetworking::register);
	}

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
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
