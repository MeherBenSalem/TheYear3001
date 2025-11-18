/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.client.model.Modelship;
import tn.nightbeam.robotica.client.model.Modelsentinel;
import tn.nightbeam.robotica.client.model.Modelmech;
import tn.nightbeam.robotica.client.model.Modelboogie;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class RoboticaModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelship.LAYER_LOCATION, Modelship::createBodyLayer);
		event.registerLayerDefinition(Modelmech.LAYER_LOCATION, Modelmech::createBodyLayer);
		event.registerLayerDefinition(Modelboogie.LAYER_LOCATION, Modelboogie::createBodyLayer);
		event.registerLayerDefinition(Modelsentinel.LAYER_LOCATION, Modelsentinel::createBodyLayer);
	}
}