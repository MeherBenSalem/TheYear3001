package tn.nightbeam.robotica.init;

import tn.nightbeam.robotica.entity.SpaceShipEntity;
import tn.nightbeam.robotica.entity.SentinelEntity;
import tn.nightbeam.robotica.entity.MechEntity;
import tn.nightbeam.robotica.entity.BoggieEntity;
import tn.nightbeam.robotica.RoboticaMod;
import tn.nightbeam.robotica.RoboticaModForge;

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
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.DungeonHooks;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RoboticaModEntitiesForge {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RoboticaMod.MOD_ID);
	public static final RegistryObject<EntityType<BoggieEntity>> BOGGIE = register("boggie",
			EntityType.Builder.<BoggieEntity>of(BoggieEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(0.6f, 1f));
	public static final RegistryObject<EntityType<SentinelEntity>> SENTINEL = register("sentinel",
			EntityType.Builder.<SentinelEntity>of(SentinelEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3)

					.sized(0.6f, 3.5f));
	public static final RegistryObject<EntityType<MechEntity>> MECH = register("mech",
			EntityType.Builder.<MechEntity>of(MechEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(2f, 3.7f));
	public static final RegistryObject<EntityType<SpaceShipEntity>> SPACE_SHIP = register("space_ship",
			EntityType.Builder.<SpaceShipEntity>of(SpaceShipEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(4f, 2f));
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SpawnPlacements.register(SENTINEL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					(entityType, world, reason, pos, random) -> world.getDifficulty() != Difficulty.PEACEFUL
							&& Monster.isDarkEnoughToSpawn(world, pos, random)
							&& Mob.checkMobSpawnRules(entityType, world, reason, pos, random));
			DungeonHooks.addDungeonMob(SENTINEL.get(), 180);
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(BOGGIE.get(), BoggieEntity.createAttributes().build());
		event.put(SENTINEL.get(), SentinelEntity.createAttributes().build());
		event.put(MECH.get(), MechEntity.createAttributes().build());
		event.put(SPACE_SHIP.get(), SpaceShipEntity.createAttributes().build());
	}


	static {
		tn.nightbeam.robotica.init.RoboticaModEntities.BOGGIE = BOGGIE;
		tn.nightbeam.robotica.init.RoboticaModEntities.SENTINEL = SENTINEL;
		tn.nightbeam.robotica.init.RoboticaModEntities.MECH = MECH;
		tn.nightbeam.robotica.init.RoboticaModEntities.SPACE_SHIP = SPACE_SHIP;
	}
}
