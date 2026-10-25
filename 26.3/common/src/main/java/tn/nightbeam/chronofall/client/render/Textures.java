package tn.nightbeam.chronofall.client.render;

import net.minecraft.resources.Identifier;
import tn.nightbeam.chronofall.Chronofall;

final class Textures {
    private Textures() {
    }

    static Identifier entity(String name) {
        return Chronofall.id("textures/entity/" + name + ".png");
    }

    static Identifier glow(String name) {
        return Chronofall.id("textures/entity/" + name + "_glow.png");
    }
}
