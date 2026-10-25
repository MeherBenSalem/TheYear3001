package tn.nightbeam.chronofall.core;

import java.util.Map;
import java.util.Set;

/**
 * Ids from The Year 3001 (0.x, namespace {@code theyear3001}) and Robotica (1.0.x, namespace {@code robotica}) and
 * what they are called now. Worlds from either keep their blocks, items, robots and vehicles: when the game looks up
 * an id it does not know, the loader hook asks {@link #remap(String)} for the new one.
 */
public final class LegacyIds {
    public static final String NAMESPACE = "chronofall";
    public static final Set<String> OLD_NAMESPACES = Set.of("robotica", "theyear3001");

    /** Paths that changed name. Anything kept under the same path needs no entry. */
    private static final Map<String, String> RENAMED = Map.ofEntries(
            Map.entry("crafting_workstation", "fabricator"),
            Map.entry("boggie", "drone"),
            Map.entry("boggie_mk_1", "drone"),
            Map.entry("boggie_mk_1_egg", "drone"),
            Map.entry("mech_mk_1", "mech"),
            Map.entry("mech_mk_1_egg", "mech"),
            Map.entry("sentinel_mk_1", "sentinel"),
            Map.entry("sentinel_mk_1_spawn_egg", "sentinel_spawn_egg"),
            Map.entry("space_ship", "skiff"),
            Map.entry("space_ship_capsule", "skiff"),
            Map.entry("flying_capsule", "skiff"),
            Map.entry("flying_capsule_mech", "skiff"),
            Map.entry("flying_capsule_mech_spawn_egg", "skiff"),
            Map.entry("empty_card", "blank_card"));

    /** Old content with no successor: it disappears from old worlds. */
    private static final Set<String> REMOVED = Set.of("oil", "oil_bucket", "scrap_stick", "titanium_rod", "b_1928", "b_1990", "b_2050",
            "b_1928_spawn_egg", "b_1990_spawn_egg", "b_2050_spawn_egg");

    private LegacyIds() {
    }

    /**
     * The current id for an old {@code namespace:path}, or null when the id is not an old one or its content was
     * removed. The path is looked up in the rename table first and kept as it is otherwise; whether the result
     * exists is for the registry to decide.
     */
    public static String remap(String id) {
        int colon = id.indexOf(':');
        if (colon <= 0) {
            return null;
        }
        String namespace = id.substring(0, colon);
        String path = id.substring(colon + 1);
        if (!OLD_NAMESPACES.contains(namespace) || path.isEmpty() || REMOVED.contains(path)) {
            return null;
        }
        return NAMESPACE + ":" + RENAMED.getOrDefault(path, path);
    }

    public static boolean isRemoved(String path) {
        return REMOVED.contains(path);
    }

    /** The renamed paths, for loaders that register aliases up front. */
    public static Map<String, String> renamed() {
        return RENAMED;
    }
}
