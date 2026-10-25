package tn.nightbeam.chronofall.legacy;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.core.Catalog;
import tn.nightbeam.chronofall.core.LegacyIds;

/**
 * Every old id ({@code robotica:} and {@code theyear3001:}) of the content kept in 2.0, per registry, for loaders that
 * register aliases up front (NeoForge and Forge). Fabric resolves old ids on lookup instead (see its registry mixin).
 */
public final class LegacyAliases {
    private LegacyAliases() {
    }

    public static void each(ResourceKey<? extends Registry<?>> registry, BiConsumer<Identifier, Identifier> alias) {
        List<String> paths = paths(registry);
        if (paths.isEmpty()) {
            return;
        }
        for (String namespace : LegacyIds.OLD_NAMESPACES) {
            for (String path : paths) {
                alias.accept(Identifier.fromNamespaceAndPath(namespace, path), Chronofall.id(path));
            }
            for (Map.Entry<String, String> renamed : LegacyIds.renamed().entrySet()) {
                if (paths.contains(renamed.getValue()) && !paths.contains(renamed.getKey())) {
                    alias.accept(Identifier.fromNamespaceAndPath(namespace, renamed.getKey()), Chronofall.id(renamed.getValue()));
                }
            }
        }
    }

    private static List<String> paths(ResourceKey<? extends Registry<?>> registry) {
        if (registry.equals(Registries.BLOCK)) {
            return Catalog.BLOCKS;
        }
        if (registry.equals(Registries.ITEM)) {
            return Catalog.items();
        }
        if (registry.equals(Registries.ENTITY_TYPE)) {
            return Catalog.ENTITIES;
        }
        if (registry.equals(Registries.BLOCK_ENTITY_TYPE)) {
            return Catalog.BLOCK_ENTITIES;
        }
        return List.of();
    }
}
