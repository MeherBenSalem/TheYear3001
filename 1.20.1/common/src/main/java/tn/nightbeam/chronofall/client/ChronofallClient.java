package tn.nightbeam.chronofall.client;

import net.minecraft.client.Minecraft;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.item.Jetpack;

/** Client setup and the per-tick client hook shared by the loaders. */
public final class ChronofallClient {
    private ChronofallClient() {
    }

    public static void init() {
        Chronofall.setClient(new ClientHooksImpl());
    }

    /** End of every client tick. */
    public static void tick(Minecraft mc) {
        if (mc.player != null && mc.level != null && !mc.isPaused()) {
            Jetpack.clientTick(mc.player);
        }
    }
}
