package tn.nightbeam.robotica.init;

import net.minecraft.world.entity.EntityType;
import tn.nightbeam.robotica.entity.BoggieEntity;
import tn.nightbeam.robotica.entity.SentinelEntity;
import tn.nightbeam.robotica.entity.MechEntity;
import tn.nightbeam.robotica.entity.SpaceShipEntity;
import java.util.function.Supplier;

public class RoboticaModEntities {
	public static Supplier<EntityType<BoggieEntity>> BOGGIE;
	public static Supplier<EntityType<SentinelEntity>> SENTINEL;
	public static Supplier<EntityType<MechEntity>> MECH;
	public static Supplier<EntityType<SpaceShipEntity>> SPACE_SHIP;
}
