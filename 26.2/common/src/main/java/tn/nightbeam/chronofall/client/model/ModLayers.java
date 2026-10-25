package tn.nightbeam.chronofall.client.model;

import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import tn.nightbeam.chronofall.Chronofall;

/** Model layer ids and their definitions; each loader registers {@link #ALL}. */
public final class ModLayers {
    public static final ModelLayerLocation DRONE = layer("drone");
    public static final ModelLayerLocation SENTINEL = layer("sentinel");
    public static final ModelLayerLocation MECH = layer("mech");
    public static final ModelLayerLocation SKIFF = layer("skiff");
    public static final ModelLayerLocation CHRONO_POD = layer("chrono_pod");
    public static final ModelLayerLocation JETPACK = layer("jetpack");

    public static final Map<ModelLayerLocation, Supplier<LayerDefinition>> ALL = Map.of(
            DRONE, ModelMeshes::drone, SENTINEL, ModelMeshes::sentinel, MECH, ModelMeshes::mech, SKIFF, ModelMeshes::skiff,
            CHRONO_POD, ModelMeshes::chronoPod, JETPACK, ModelMeshes::jetpack);

    private ModLayers() {
    }

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(Chronofall.id(name), "main");
    }
}
