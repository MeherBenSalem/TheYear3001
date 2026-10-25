package tn.nightbeam.chronofall;

import net.minecraft.world.entity.Entity;

/**
 * Calls from common game code into the client, which a dedicated server does not have. The client sets the real
 * implementation at startup; everything else sees {@link #NONE}.
 */
public interface ClientHooks {
    ClientHooks NONE = new ClientHooks() {
    };

    /** Whether the local player holds the jump key (climb in a skiff, thrust with a jetpack). */
    default boolean jumpHeld() {
        return false;
    }

    /** Whether the local player holds the sprint key (descend in a skiff). */
    default boolean descendHeld() {
        return false;
    }

    /** Whether {@code entity} is the player of this client. */
    default boolean isLocalPlayer(Entity entity) {
        return false;
    }

    /** Starts the looping engine or thruster sound of {@code entity} if it is not already playing. */
    default void loopSound(Entity entity, String kind) {
    }
}
