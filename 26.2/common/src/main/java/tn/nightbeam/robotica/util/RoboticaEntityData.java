package tn.nightbeam.robotica.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import tn.nightbeam.robotica.platform.Services;

public final class RoboticaEntityData {
	private RoboticaEntityData() {
	}

	public static CompoundTag get(Entity entity) {
		return Services.PLATFORM.getPersistentData(entity);
	}

	public static double getDouble(Entity entity, String key) {
		return get(entity).getDouble(key).orElse(0.0);
	}

	public static void putDouble(Entity entity, String key, double value) {
		get(entity).putDouble(key, value);
	}
}
