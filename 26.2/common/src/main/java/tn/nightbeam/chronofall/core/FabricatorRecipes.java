package tn.nightbeam.chronofall.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Everything the Fabricator makes. Ids are plain strings so the table can be checked without Minecraft; the block
 * resolves them against the item registry. Ingredients are taken from the player's inventory when printing starts.
 */
public final class FabricatorRecipes {
    public record Ingredient(String item, int count) {
    }

    /** {@code ticks}: how long the Fabricator prints (20 ticks = 1 second). */
    public record Recipe(String result, int count, int ticks, List<Ingredient> ingredients) {
        /** Total of each item needed, so an ingredient listed twice is counted once. */
        public Map<String, Integer> totals() {
            Map<String, Integer> totals = new LinkedHashMap<>();
            for (Ingredient ingredient : ingredients) {
                totals.merge(ingredient.item(), ingredient.count(), Integer::sum);
            }
            return totals;
        }
    }

    private static final String C = "chronofall:";
    private static final String M = "minecraft:";

    public static final List<Recipe> ALL = List.of(
            r(C + "flux_cell", 1, 60, C + "cobalt_ingot", 2, M + "redstone", 4, C + "electrical_conductors", 1),
            r(C + "printed_circuit_board", 1, 60, C + "blank_printed_circuit_board", 1, C + "electrical_conductors", 2, M + "gold_nugget", 3),
            r(C + "central_processing_unit", 1, 100, C + "printed_circuit_board", 2, C + "rift_shard", 1, M + "quartz", 2),
            r(C + "jetpack", 1, 200, C + "titanium_ingot", 6, C + "flux_cell", 2, C + "central_processing_unit", 1, M + "blaze_rod", 2),
            r(C + "plasma_cutter", 1, 200, C + "titanium_ingot", 3, C + "flux_cell", 1, C + "central_processing_unit", 1, C + "rift_shard", 2,
                    M + "diamond", 1),
            r(C + "pulse_rifle", 1, 200, C + "titanium_ingot", 4, C + "flux_cell", 1, C + "central_processing_unit", 1, C + "rift_shard", 2),
            r(C + "drone", 1, 200, C + "scrap", 6, C + "flux_cell", 1, C + "central_processing_unit", 1, C + "printed_circuit_board", 1),
            r(C + "collector_chip", 1, 80, C + "blank_card", 1, C + "printed_circuit_board", 1, M + "hopper", 1),
            r(C + "guard_chip", 1, 80, C + "blank_card", 1, C + "printed_circuit_board", 1, C + "rift_shard", 1),
            r(C + "lamp_chip", 1, 80, C + "blank_card", 1, C + "printed_circuit_board", 1, M + "glowstone_dust", 4),
            r(C + "mech", 1, 300, C + "scrap_block", 4, C + "titanium_ingot", 8, C + "flux_cell", 3, C + "central_processing_unit", 1,
                    M + "piston", 2),
            r(C + "skiff", 1, 300, C + "titanium_ingot", 10, C + "flux_cell", 4, C + "central_processing_unit", 1, C + "rift_shard", 4,
                    M + "phantom_membrane", 2),
            r(C + "rift_gate", 1, 200, C + "rift_glass", 4, C + "orichalcum_ingot", 2, M + "ender_pearl", 2, C + "rift_shard", 2),
            r(C + "rift_tuner", 1, 80, C + "titanium_ingot", 2, C + "rift_shard", 1, M + "compass", 1),
            r(C + "rift_beacon", 1, 300, C + "rift_glass", 4, C + "orichalcum_ingot", 4, C + "central_processing_unit", 1, C + "rift_shard", 4),
            r(C + "bulk_upgrade_card", 1, 80, C + "blank_card", 1, M + "golden_apple", 1),
            r(C + "damage_upgrade_card", 1, 80, C + "blank_card", 1, M + "blaze_powder", 4),
            r(C + "defense_upgrade_card", 1, 80, C + "blank_card", 1, C + "titanium_ingot", 4),
            r(C + "speed_upgrade_card", 1, 80, C + "blank_card", 1, M + "sugar", 4, M + "redstone", 2),
            r(C + "repair_card", 1, 60, C + "blank_card", 1, M + "iron_ingot", 2, C + "scrap", 4));

    private FabricatorRecipes() {
    }

    private static Recipe r(String result, int count, int ticks, Object... pairs) {
        Ingredient[] ingredients = new Ingredient[pairs.length / 2];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = new Ingredient((String) pairs[i * 2], (Integer) pairs[i * 2 + 1]);
        }
        return new Recipe(result, count, ticks, List.of(ingredients));
    }

    /** The recipe at {@code index}, or null when the index is out of range (a stale or forged button click). */
    public static Recipe get(int index) {
        return index >= 0 && index < ALL.size() ? ALL.get(index) : null;
    }

    public static int indexOf(String result) {
        for (int i = 0; i < ALL.size(); i++) {
            if (ALL.get(i).result().equals(result)) {
                return i;
            }
        }
        return -1;
    }

    /** Whether {@code inventory} (item id to count held) covers every ingredient of {@code recipe}. */
    public static boolean canAfford(Recipe recipe, Map<String, Integer> inventory) {
        for (Map.Entry<String, Integer> need : recipe.totals().entrySet()) {
            if (inventory.getOrDefault(need.getKey(), 0) < need.getValue()) {
                return false;
            }
        }
        return true;
    }
}
