package tn.nightbeam.chronofall.client.render;

import net.minecraft.resources.ResourceLocation;
import tn.nightbeam.chronofall.Chronofall;

final class Textures {
    private Textures() {
    }

    static ResourceLocation entity(String name) {
        return Chronofall.id("textures/entity/" + name + ".png");
    }

    static ResourceLocation glow(String name) {
        return Chronofall.id("textures/entity/" + name + "_glow.png");
    }
}
