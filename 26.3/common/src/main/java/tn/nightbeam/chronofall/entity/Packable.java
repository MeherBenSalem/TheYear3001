package tn.nightbeam.chronofall.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

/** A robot or vehicle that can be packed into its item and deployed again. */
public interface Packable {
    /** The data to keep on the item: {@code Flux}, chips, upgrades. */
    CompoundTag pack();

    /** Restores packed data on deployment; {@code player} (may be null) becomes the owner where that matters. */
    void unpack(CompoundTag data, Player player);
}
