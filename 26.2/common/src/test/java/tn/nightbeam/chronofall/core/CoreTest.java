package tn.nightbeam.chronofall.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CoreTest {
    private static final Set<String> ITEMS = new HashSet<>(Catalog.items());

    @Test
    void fluxArithmeticStaysInBounds() {
        assertEquals(100, Flux.add(50, 80, 100));
        assertEquals(0, Flux.add(10, -50, 100));
        assertEquals(Integer.MAX_VALUE, Flux.add(Integer.MAX_VALUE - 1, 10, Integer.MAX_VALUE));
        assertEquals(30, Flux.room(70, 400, 100));
        assertEquals(0, Flux.room(100, 400, 100));
        assertTrue(Flux.canPay(30, 30));
        assertFalse(Flux.canPay(29, 30));
    }

    @Test
    void fluxBarAndColour() {
        assertEquals(0, Flux.barWidth(0, 1000));
        assertEquals(1, Flux.barWidth(1, 1000), "a nearly empty device still shows a pixel");
        assertEquals(13, Flux.barWidth(1000, 1000));
        assertEquals(0x38F3FF, Flux.barColor(1000, 1000), "full is flux cyan");
        assertEquals(0xFF3FD2, Flux.barColor(0, 1000), "empty is rift magenta");
        assertEquals(6_400, Flux.fromBurnTicks(1600), "coal");
        assertEquals("12,000 / 30,000", Flux.format(12_000, 30_000));
        assertEquals(33, Flux.percent(10_000, 30_000));
    }

    @Test
    void fabricatorRecipesOnlyUseRealItems() {
        assertFalse(FabricatorRecipes.ALL.isEmpty());
        Set<String> results = new HashSet<>();
        for (FabricatorRecipes.Recipe recipe : FabricatorRecipes.ALL) {
            assertTrue(results.add(recipe.result()), "one recipe per result: " + recipe.result());
            assertTrue(ITEMS.contains(path(recipe.result())), "result is registered: " + recipe.result());
            assertTrue(recipe.count() > 0 && recipe.ticks() > 0);
            assertFalse(recipe.ingredients().isEmpty());
            for (FabricatorRecipes.Ingredient ingredient : recipe.ingredients()) {
                assertTrue(ingredient.count() > 0 && ingredient.count() <= 64);
                if (ingredient.item().startsWith("chronofall:")) {
                    assertTrue(ITEMS.contains(path(ingredient.item())), "ingredient is registered: " + ingredient.item());
                } else {
                    assertTrue(ingredient.item().startsWith("minecraft:"), ingredient.item());
                }
                assertFalse(ingredient.item().equals(recipe.result()), "no recipe consumes its own result");
            }
        }
        for (String device : List.of("jetpack", "plasma_cutter", "pulse_rifle", "drone", "mech", "skiff", "rift_gate", "rift_tuner",
                "rift_beacon", "flux_cell", "central_processing_unit")) {
            assertTrue(FabricatorRecipes.indexOf("chronofall:" + device) >= 0, device + " can be fabricated");
        }
    }

    @Test
    void fabricatorAffordability() {
        FabricatorRecipes.Recipe cell = FabricatorRecipes.ALL.get(FabricatorRecipes.indexOf("chronofall:flux_cell"));
        Map<String, Integer> inventory = new HashMap<>(Map.of("chronofall:cobalt_ingot", 2, "minecraft:redstone", 4));
        assertFalse(FabricatorRecipes.canAfford(cell, inventory));
        inventory.put("chronofall:electrical_conductors", 1);
        assertTrue(FabricatorRecipes.canAfford(cell, inventory));
        assertNull(FabricatorRecipes.get(-1));
        assertNull(FabricatorRecipes.get(FabricatorRecipes.ALL.size()));
    }

    @Test
    void legacyIdsMapBothOldNamespaces() {
        assertEquals("chronofall:titanium_ingot", LegacyIds.remap("robotica:titanium_ingot"));
        assertEquals("chronofall:titanium_ingot", LegacyIds.remap("theyear3001:titanium_ingot"));
        assertEquals("chronofall:fabricator", LegacyIds.remap("robotica:crafting_workstation"));
        assertEquals("chronofall:drone", LegacyIds.remap("robotica:boggie"));
        assertEquals("chronofall:drone", LegacyIds.remap("theyear3001:boggie_mk_1_egg"));
        assertEquals("chronofall:skiff", LegacyIds.remap("robotica:space_ship"));
        assertEquals("chronofall:sentinel", LegacyIds.remap("theyear3001:sentinel_mk_1"));
        assertEquals("chronofall:blank_card", LegacyIds.remap("robotica:empty_card"));
        assertNull(LegacyIds.remap("robotica:oil"), "oil was removed");
        assertNull(LegacyIds.remap("minecraft:stone"));
        assertNull(LegacyIds.remap("chronofall:fabricator"));
        assertNull(LegacyIds.remap("nonsense"));
    }

    @Test
    void everyOldIdOfKeptContentLandsOnARealId() {
        Set<String> known = new HashSet<>(ITEMS);
        known.addAll(Catalog.ENTITIES);
        known.addAll(Catalog.BLOCK_ENTITIES);
        List<String> robotica = List.of("scrap_block", "crafting_workstation", "scrap_slab", "scrap_stairs_block", "scrap_debris", "titanium_ore",
                "cobalt_ore", "orichalcum_ore", "boggie", "sentinel", "mech", "space_ship", "damage_upgrade_card", "bulk_upgrade_card",
                "speed_upgrade_card", "repair_card", "scrap", "empty_card", "mech_mk_1_egg", "boggie_mk_1_egg", "printed_circuit_board",
                "blank_printed_circuit_board", "central_processing_unit", "electrical_conductors", "defense_upgrade_card", "space_ship_capsule",
                "titanium_ingot", "orichalcum_ingot", "cobalt_ingot", "raw_titanium", "raw_cobalt", "raw_orichalcum", "sentinel_spawn_egg",
                "flying_capsule", "flying_capsule_mech", "mech_mk_1", "sentinel_mk_1", "boggie_mk_1", "sentinel_mk_1_spawn_egg");
        for (String old : robotica) {
            for (String namespace : LegacyIds.OLD_NAMESPACES) {
                String now = LegacyIds.remap(namespace + ":" + old);
                assertNotNull(now, old);
                assertTrue(known.contains(path(now)), old + " -> " + now);
            }
        }
        for (String metal : Catalog.METALS) {
            for (String tool : Catalog.TOOLS) {
                assertTrue(known.contains(path(LegacyIds.remap("robotica:" + metal + "_" + tool))));
            }
            for (String piece : Catalog.ARMOR) {
                assertTrue(known.contains(path(LegacyIds.remap("theyear3001:" + metal + "_armor_" + piece))));
            }
        }
    }

    @Test
    void riftsOnlyAtNightAndNotTooOften() {
        assertFalse(RiftRules.shouldOpen(6_000, 100_000, -1, 18_000, 1.0, 0.0), "never by day");
        assertTrue(RiftRules.shouldOpen(18_000, 100_000, -1, 18_000, 0.04, 0.03));
        assertFalse(RiftRules.shouldOpen(18_000, 100_000, -1, 18_000, 0.04, 0.05));
        assertFalse(RiftRules.shouldOpen(18_000, 100_000, 90_000, 18_000, 1.0, 0.0), "too soon after the last one");
        assertTrue(RiftRules.shouldOpen(24_000L * 5 + 14_000, 200_000, 90_000, 18_000, 1.0, 0.0));
        assertFalse(RiftRules.shouldOpen(18_000, 100_000, -1, 0, 0.0, 0.0), "chance 0 turns rifts off");
    }

    @Test
    void landingStaysInTheRing() {
        Random random = new Random(42);
        for (int i = 0; i < 500; i++) {
            RiftRules.Offset offset = RiftRules.landing(random, 24, 48);
            double distance = Math.hypot(offset.dx(), offset.dz());
            assertTrue(distance >= 23 && distance <= 49, "distance " + distance);
        }
        assertEquals("north", RiftRules.direction(0, -10));
        assertEquals("east", RiftRules.direction(10, 0));
        assertEquals("south_west", RiftRules.direction(-10, 10));
    }

    @Test
    void sentinelsAndLoot() {
        Random random = new Random(7);
        for (int i = 0; i < 100; i++) {
            int n = RiftRules.sentinels(random, false, 3);
            assertTrue(n == 2 || n == 3);
        }
        assertEquals(0, RiftRules.sentinels(random, true, 3));
        assertEquals(1, RiftRules.sentinels(new Random(1), false, 1));
        int shards = 0;
        for (int i = 0; i < 200; i++) {
            List<RiftRules.Stack> loot = RiftRules.cacheLoot(random);
            assertTrue(loot.size() >= 4);
            for (RiftRules.Stack stack : loot) {
                assertTrue(ITEMS.contains(path(stack.item())), stack.item());
                assertTrue(stack.count() >= 1 && stack.count() <= 64);
                if (stack.item().equals("chronofall:rift_shard")) {
                    shards += stack.count();
                    assertTrue(stack.count() >= 2 && stack.count() <= 4);
                }
            }
        }
        assertTrue(shards >= 400, "at least two shards per cache");
    }

    @Test
    void upgradeCardsHaveFixedStepsAndCaps() {
        assertEquals(1, UpgradeRules.apply(UpgradeRules.Card.BULK, 0));
        assertEquals(-1, UpgradeRules.apply(UpgradeRules.Card.BULK, 5), "capped");
        assertEquals(-1, UpgradeRules.apply(UpgradeRules.Card.SPEED, 3));
        assertEquals(130.0, UpgradeRules.maxHealth(5));
        assertEquals(130.0, UpgradeRules.maxHealth(99), "levels past the cap do nothing");
        assertEquals(28.0, UpgradeRules.armor(5));
        assertEquals(0.28 * 1.3, UpgradeRules.speed(3), 1e-9);
        assertEquals(16.0, UpgradeRules.bonusDamage(5));
        assertEquals(UpgradeRules.Card.REPAIR, UpgradeRules.Card.byItem("repair_card"));
        assertFalse(UpgradeRules.Card.REPAIR.isUpgrade());
        assertNull(UpgradeRules.Card.byItem("jetpack"));
    }

    @Test
    void catalogHasNoDuplicates() {
        List<String> items = Catalog.items();
        assertEquals(items.size(), new HashSet<>(items).size());
        assertTrue(items.containsAll(Catalog.BLOCKS));
    }

    private static String path(String id) {
        return id.substring(id.indexOf(':') + 1);
    }
}
