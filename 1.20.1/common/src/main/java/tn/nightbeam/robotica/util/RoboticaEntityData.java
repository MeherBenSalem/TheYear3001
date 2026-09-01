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
}
