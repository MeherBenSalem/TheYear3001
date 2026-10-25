package tn.nightbeam.chronofall;

import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.ChronoConfig;
import tn.nightbeam.chronofall.platform.Platform;
import tn.nightbeam.chronofall.platform.Registrar;

public final class Chronofall {
    private static Platform platform;
    private static ClientHooks client = ClientHooks.NONE;

    private Chronofall() {
    }

    public static void init(Platform loader, Registrar registrar) {
        platform = loader;
        ChronoConfig.load(loader.configDir());
        ModContent.register(registrar, loader);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(Constants.MOD_ID, path);
    }

    public static Platform platform() {
        return platform;
    }

    /** The client side of the game, or a no-op on a dedicated server. */
    public static ClientHooks client() {
        return client;
    }

    public static void setClient(ClientHooks hooks) {
        client = hooks;
    }
}
