package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.RoboticaMod;

import tn.nightbeam.robotica.client.model.Modelship;
import tn.nightbeam.robotica.client.model.Modelsentinel;
import tn.nightbeam.robotica.client.model.Modelmech;
import tn.nightbeam.robotica.client.model.Modelboogie;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(modid = RoboticaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class RoboticaModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelship.LAYER_LOCATION, Modelship::createBodyLayer);
		event.registerLayerDefinition(Modelmech.LAYER_LOCATION, Modelmech::createBodyLayer);
		event.registerLayerDefinition(Modelboogie.LAYER_LOCATION, Modelboogie::createBodyLayer);
		event.registerLayerDefinition(Modelsentinel.LAYER_LOCATION, Modelsentinel::createBodyLayer);
	}
}