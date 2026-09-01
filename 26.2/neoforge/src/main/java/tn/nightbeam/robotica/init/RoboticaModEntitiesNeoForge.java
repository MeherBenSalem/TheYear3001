package tn.nightbeam.robotica.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.minecraft.world.entity.SpawnPlacements;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.entity.BoggieEntity;
import tn.nightbeam.robotica.entity.MechEntity;
import tn.nightbeam.robotica.entity.SentinelEntity;
import tn.nightbeam.robotica.entity.SpaceShipEntity;

@EventBusSubscriber(modid = RoboticaMod.MOD_ID)
public class RoboticaModEntitiesNeoForge {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RoboticaMod.MOD_ID);
	public static final DeferredHolder<EntityType<?>, EntityType<BoggieEntity>> BOGGIE = register("boggie",
			EntityType.Builder.<BoggieEntity>of(BoggieEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.6f, 1f));
	public static final DeferredHolder<EntityType<?>, EntityType<SentinelEntity>> SENTINEL = register("sentinel",
			EntityType.Builder.<SentinelEntity>of(SentinelEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).sized(0.6f, 3.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<MechEntity>> MECH = register("mech",
			EntityType.Builder.<MechEntity>of(MechEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(2f, 3.7f));
	public static final DeferredHolder<EntityType<?>, EntityType<SpaceShipEntity>> SPACE_SHIP = register("space_ship",
			EntityType.Builder.<SpaceShipEntity>of(SpaceShipEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(4f, 2f));

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryName, EntityType.Builder<T> entityTypeBuilder) {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(RoboticaMod.MOD_ID, registryName));
		return REGISTRY.register(registryName, () -> entityTypeBuilder.build(key));
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(BOGGIE.get(), BoggieEntity.createAttributes().build());
		event.put(SENTINEL.get(), SentinelEntity.createAttributes().build());
		event.put(MECH.get(), MechEntity.createAttributes().build());
		event.put(SPACE_SHIP.get(), SpaceShipEntity.createAttributes().build());
	}

	static {
		RoboticaModEntities.BOGGIE = BOGGIE;
		RoboticaModEntities.SENTINEL = SENTINEL;
		RoboticaModEntities.MECH = MECH;
		RoboticaModEntities.SPACE_SHIP = SPACE_SHIP;
	}
}
