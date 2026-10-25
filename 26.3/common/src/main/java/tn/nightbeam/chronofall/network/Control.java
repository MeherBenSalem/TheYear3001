package tn.nightbeam.chronofall.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

/**
 * The one packet the mod sends: the client's control bits (today only "jetpack thrusting"). The server keeps the
 * latest bits per player; they expire if the client stops sending, so a lost packet cannot leave a jetpack running.
 */
public final class Control {
    public static final int THRUST = 1;
    /** Clients resend their state at least this often (ticks) while it is not zero. */
    public static final int RESEND = 10;
    private static final int EXPIRE = 25;
    private static final Map<UUID, int[]> STATE = new HashMap<>();

    private Control() {
    }

    /** Called by the loaders when the packet arrives (on the server thread). */
    public static void receive(ServerPlayer player, int flags) {
        STATE.put(player.getUUID(), new int[]{flags, player.level().getServer().getTickCount()});
    }

    public static boolean has(ServerPlayer player, int flag) {
        int[] state = STATE.get(player.getUUID());
        return state != null && (state[0] & flag) != 0 && player.level().getServer().getTickCount() - state[1] <= EXPIRE;
    }

    public static void forget(ServerPlayer player) {
        STATE.remove(player.getUUID());
    }
}
