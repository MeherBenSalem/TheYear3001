package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.item.RocketLauncherItem;

import software.bernie.geckolib.animatable.GeoItem;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.ItemStack;

@Mod.EventBusSubscriber
public class ItemAnimationFactory {
	@SubscribeEvent
	public static void animatedItems(TickEvent.PlayerTickEvent event) {
		String animation = "";
		ItemStack mainhandItem = event.player.getMainHandItem().copy();
		ItemStack offhandItem = event.player.getOffhandItem().copy();
		if (event.phase == TickEvent.Phase.START && (mainhandItem.getItem() instanceof GeoItem || offhandItem.getItem() instanceof GeoItem)) {
			if (mainhandItem.getItem() instanceof RocketLauncherItem animatable) {
				animation = mainhandItem.getOrCreateTag().getString("geckoAnim");
				if (!animation.isEmpty()) {
					event.player.getMainHandItem().getOrCreateTag().putString("geckoAnim", "");
					if (event.player.level().isClientSide()) {
						((RocketLauncherItem) event.player.getMainHandItem().getItem()).animationprocedure = animation;
					}
				}
			}
			if (offhandItem.getItem() instanceof RocketLauncherItem animatable) {
				animation = offhandItem.getOrCreateTag().getString("geckoAnim");
				if (!animation.isEmpty()) {
					event.player.getOffhandItem().getOrCreateTag().putString("geckoAnim", "");
					if (event.player.level().isClientSide()) {
						((RocketLauncherItem) event.player.getOffhandItem().getItem()).animationprocedure = animation;
					}
				}
			}
		}
	}
}
