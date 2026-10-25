package tn.nightbeam.chronofall.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** When and where a Chrono Rift opens, how many Sentinels guard the pod, and what the Rift Cache holds. */
public final class RiftRules {
    /** Rifts only open between dusk and dawn. */
    public static final int NIGHT_START = 13_000;
    public static final int NIGHT_END = 23_000;
    /** The chance roll happens once a minute per player. */
    public static final int CHECK_INTERVAL = 1_200;

    public record Offset(int dx, int dz) {
    }

    public record Stack(String item, int count) {
    }

    private RiftRules() {
    }

    public static boolean isNight(long dayTime) {
        long t = Math.floorMod(dayTime, 24_000L);
        return t >= NIGHT_START && t < NIGHT_END;
    }

    /**
     * Whether a rift opens for a player on this minute's check: it must be night, the player's last rift must be at
     * least {@code minGapTicks} ago (a negative {@code lastRiftTick} means never), and {@code roll} (0 to 1) must be
     * under {@code chance}.
     */
    public static boolean shouldOpen(long dayTime, long gameTime, long lastRiftTick, int minGapTicks, double chance, double roll) {
        if (!isNight(dayTime) || chance <= 0) {
            return false;
        }
        if (lastRiftTick >= 0 && gameTime - lastRiftTick < minGapTicks) {
            return false;
        }
        return roll < chance;
    }

    /** Where the pod lands relative to the player: {@code minDistance} to {@code maxDistance} blocks away, any direction. */
    public static Offset landing(Random random, int minDistance, int maxDistance) {
        double angle = random.nextDouble() * Math.PI * 2;
        double distance = minDistance + random.nextDouble() * Math.max(0, maxDistance - minDistance);
        return new Offset((int) Math.round(Math.cos(angle) * distance), (int) Math.round(Math.sin(angle) * distance));
    }

    /** Eight-way compass direction from an offset (+x east, +z south), as a language key suffix. */
    public static String direction(int dx, int dz) {
        if (dx == 0 && dz == 0) {
            return "here";
        }
        double angle = Math.toDegrees(Math.atan2(dx, -dz)); // 0 = north, 90 = east
        int sector = (int) Math.floorMod(Math.round(angle / 45.0), 8L);
        return new String[]{"north", "north_east", "east", "south_east", "south", "south_west", "west", "north_west"}[sector];
    }

    /** Sentinels guarding a pod: 2 or 3, none on Peaceful, never more than {@code max}. */
    public static int sentinels(Random random, boolean peaceful, int max) {
        if (peaceful || max <= 0) {
            return 0;
        }
        return Math.min(max, 2 + random.nextInt(2));
    }

    /** What a Rift Cache drops when cracked open. */
    public static List<Stack> cacheLoot(Random random) {
        List<Stack> loot = new ArrayList<>();
        loot.add(new Stack("chronofall:rift_shard", 2 + random.nextInt(3)));
        loot.add(new Stack("chronofall:scrap", 3 + random.nextInt(4)));
        String[] metals = {"chronofall:titanium_ingot", "chronofall:cobalt_ingot", "chronofall:orichalcum_ingot"};
        int metal = random.nextInt(10) < 6 ? 0 : random.nextInt(10) < 7 ? 1 : 2;
        loot.add(new Stack(metals[metal], 2 + random.nextInt(3)));
        loot.add(random.nextBoolean()
                ? new Stack("chronofall:electrical_conductors", 2 + random.nextInt(3))
                : new Stack("chronofall:blank_printed_circuit_board", 1 + random.nextInt(2)));
        if (random.nextInt(4) == 0) {
            loot.add(new Stack("chronofall:printed_circuit_board", 1));
        }
        if (random.nextInt(10) == 0) {
            loot.add(new Stack("chronofall:central_processing_unit", 1));
        }
        return loot;
    }
}
