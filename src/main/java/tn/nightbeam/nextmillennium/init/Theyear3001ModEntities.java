
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tn.nightbeam.nextmillennium.init;

import tn.nightbeam.nextmillennium.entity.RobotMK1Entity;
import tn.nightbeam.nextmillennium.entity.MechMK1Entity;
import tn.nightbeam.nextmillennium.entity.BoggieMK1Entity;
import tn.nightbeam.nextmillennium.entity.B2050Entity;
import tn.nightbeam.nextmillennium.entity.B1990Entity;
import tn.nightbeam.nextmillennium.entity.B1928Entity;
import tn.nightbeam.nextmillennium.Theyear3001Mod;

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
public class Theyear3001ModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Theyear3001Mod.MODID);
	public static final RegistryObject<EntityType<RobotMK1Entity>> ROBOT_MK_1 = register("robot_mk_1",
			EntityType.Builder.<RobotMK1Entity>of(RobotMK1Entity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(RobotMK1Entity::new)

					.sized(0.6f, 2.5f));
	public static final RegistryObject<EntityType<BoggieMK1Entity>> BOGGIE_MK_1 = register("boggie_mk_1",
			EntityType.Builder.<BoggieMK1Entity>of(BoggieMK1Entity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BoggieMK1Entity::new)

					.sized(0.6f, 0.8f));
	public static final RegistryObject<EntityType<MechMK1Entity>> MECH_MK_1 = register("mech_mk_1",
			EntityType.Builder.<MechMK1Entity>of(MechMK1Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MechMK1Entity::new)

					.sized(0.6f, 3f));
	public static final RegistryObject<EntityType<B1928Entity>> B_1928 = register("b_1928",
			EntityType.Builder.<B1928Entity>of(B1928Entity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(B1928Entity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<B1990Entity>> B_1990 = register("b_1990",
			EntityType.Builder.<B1990Entity>of(B1990Entity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(B1990Entity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<B2050Entity>> B_2050 = register("b_2050",
			EntityType.Builder.<B2050Entity>of(B2050Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(B2050Entity::new)

					.sized(0.6f, 1.8f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			RobotMK1Entity.init();
			BoggieMK1Entity.init();
			MechMK1Entity.init();
			B1928Entity.init();
			B1990Entity.init();
			B2050Entity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ROBOT_MK_1.get(), RobotMK1Entity.createAttributes().build());
		event.put(BOGGIE_MK_1.get(), BoggieMK1Entity.createAttributes().build());
		event.put(MECH_MK_1.get(), MechMK1Entity.createAttributes().build());
		event.put(B_1928.get(), B1928Entity.createAttributes().build());
		event.put(B_1990.get(), B1990Entity.createAttributes().build());
		event.put(B_2050.get(), B2050Entity.createAttributes().build());
	}
}
