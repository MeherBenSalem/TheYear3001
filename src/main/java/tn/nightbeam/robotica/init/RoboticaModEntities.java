/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.entity.SpaceShipEntity;
import tn.nightbeam.robotica.entity.SentinelEntity;
import tn.nightbeam.robotica.entity.MechEntity;
import tn.nightbeam.robotica.entity.BoggieEntity;
import tn.nightbeam.robotica.RoboticaMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RoboticaModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RoboticaMod.MODID);
	public static final RegistryObject<EntityType<BoggieEntity>> BOGGIE = register("boggie",
			EntityType.Builder.<BoggieEntity>of(BoggieEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BoggieEntity::new)

					.sized(0.6f, 1f));
	public static final RegistryObject<EntityType<SentinelEntity>> SENTINEL = register("sentinel",
			EntityType.Builder.<SentinelEntity>of(SentinelEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(SentinelEntity::new)

					.sized(0.6f, 3.5f));
	public static final RegistryObject<EntityType<MechEntity>> MECH = register("mech",
			EntityType.Builder.<MechEntity>of(MechEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MechEntity::new)

					.sized(2f, 3.7f));
	public static final RegistryObject<EntityType<SpaceShipEntity>> SPACE_SHIP = register("space_ship",
			EntityType.Builder.<SpaceShipEntity>of(SpaceShipEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SpaceShipEntity::new)

					.sized(4f, 2f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			BoggieEntity.init();
			SentinelEntity.init();
			MechEntity.init();
			SpaceShipEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(BOGGIE.get(), BoggieEntity.createAttributes().build());
		event.put(SENTINEL.get(), SentinelEntity.createAttributes().build());
		event.put(MECH.get(), MechEntity.createAttributes().build());
		event.put(SPACE_SHIP.get(), SpaceShipEntity.createAttributes().build());
	}
}