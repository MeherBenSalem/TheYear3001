package tn.nightbeam.chronofall.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Every id the mod registers, by path. The registration code and this list are checked against each other in game
 * (QA) and the recipe and legacy tables are checked against it by unit tests.
 */
public final class Catalog {
    public static final List<String> METALS = List.of("scrap", "titanium", "cobalt", "orichalcum");
    public static final List<String> TOOLS = List.of("sword", "pickaxe", "axe", "shovel", "hoe");
    public static final List<String> ARMOR = List.of("helmet", "chestplate", "leggings", "boots");

    /** Blocks; each also has a block item of the same id. */
    public static final List<String> BLOCKS = List.of(
            "scrap_block", "scrap_slab", "scrap_stairs_block", "scrap_debris", "titanium_ore", "cobalt_ore", "orichalcum_ore",
            "titanium_block", "cobalt_block", "orichalcum_block", "rift_glass",
            "fabricator", "flux_charger", "rift_gate", "rift_cache", "rift_beacon");

    public static final List<String> ENTITIES = List.of("sentinel", "drone", "mech", "skiff", "chrono_pod", "laser_bolt");

    /** Block entities. */
    public static final List<String> BLOCK_ENTITIES = List.of("fabricator", "flux_charger", "rift_gate", "rift_beacon");

    private static final List<String> PLAIN_ITEMS = List.of(
            "scrap", "rift_shard", "raw_titanium", "titanium_ingot", "raw_cobalt", "cobalt_ingot", "raw_orichalcum", "orichalcum_ingot",
            "electrical_conductors", "blank_printed_circuit_board", "printed_circuit_board", "central_processing_unit", "flux_cell",
            "blank_card", "repair_card", "bulk_upgrade_card", "damage_upgrade_card", "defense_upgrade_card", "speed_upgrade_card",
            "collector_chip", "guard_chip", "lamp_chip",
            "jetpack", "plasma_cutter", "pulse_rifle", "rift_tuner", "drone", "mech", "skiff", "sentinel_spawn_egg");

    private Catalog() {
    }

    /** Every item id path: plain items, tools, armour and the block items. */
    public static List<String> items() {
        List<String> items = new ArrayList<>(PLAIN_ITEMS);
        for (String metal : METALS) {
            for (String tool : TOOLS) {
                items.add(metal + "_" + tool);
            }
            for (String piece : ARMOR) {
                items.add(metal + "_armor_" + piece);
            }
        }
        items.addAll(BLOCKS);
        return items;
    }
}
