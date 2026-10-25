package tn.nightbeam.chronofall.core;

/**
 * Flux: the one energy unit of the mod. Devices keep it as a plain integer (item data or entity data); this class holds
 * the numbers and the arithmetic so every device charges, drains and draws its bar the same way.
 */
public final class Flux {
    /** Flux per burn tick of furnace fuel (coal burns 1600 ticks: 6,400 flux). */
    public static final int PER_BURN_TICK = 4;
    /** Flux from one Rift Shard burned in a charger. */
    public static final int PER_RIFT_SHARD = 40_000;
    /** Flux a Flux Cell adds when used on a robot or vehicle. */
    public static final int PER_CELL = 4_000;

    public static final int CHARGER_CAPACITY = 100_000;
    /** Flux per tick the charger moves into the item in its slot. */
    public static final int CHARGER_SLOT_RATE = 400;
    /** Flux per tick the charger moves into each robot or vehicle in range. */
    public static final int CHARGER_ENTITY_RATE = 200;
    /** Flux per tick the charger moves into each worn or held device of a player in range. */
    public static final int CHARGER_PLAYER_RATE = 200;
    public static final double CHARGER_ENTITY_RANGE = 8.0;
    public static final double CHARGER_PLAYER_RANGE = 4.0;

    public static final int JETPACK_CAPACITY = 12_000;
    public static final int JETPACK_PER_TICK = 8;
    public static final int CUTTER_CAPACITY = 6_000;
    public static final int CUTTER_PER_BLOCK = 30;
    public static final int RIFLE_CAPACITY = 6_000;
    public static final int RIFLE_PER_SHOT = 60;
    public static final int DRONE_CAPACITY = 12_000;
    public static final int DRONE_PER_TICK = 1;
    public static final int DRONE_PER_SHOT = 40;
    public static final int MECH_CAPACITY = 30_000;
    public static final int MECH_PER_TICK = 2;
    public static final int SKIFF_CAPACITY = 30_000;
    public static final int SKIFF_PER_TICK = 6;

    /** Width of the item bar in pixels, as vanilla draws durability. */
    public static final int BAR_WIDTH = 13;

    private Flux() {
    }

    /** {@code stored + amount}, never above {@code capacity} or below zero. */
    public static int add(int stored, int amount, int capacity) {
        long sum = (long) stored + amount;
        return (int) Math.max(0, Math.min(capacity, sum));
    }

    /** How much of {@code offered} fits into a device holding {@code stored} of {@code capacity}. */
    public static int room(int stored, int offered, int capacity) {
        return Math.max(0, Math.min(offered, capacity - stored));
    }

    /** Whether {@code stored} pays for {@code cost}. */
    public static boolean canPay(int stored, int cost) {
        return stored >= cost;
    }

    /** Bar width in pixels (0 to 13); a non-empty device always shows at least one pixel. */
    public static int barWidth(int stored, int capacity) {
        if (capacity <= 0 || stored <= 0) {
            return 0;
        }
        return Math.max(1, Math.min(BAR_WIDTH, Math.round(BAR_WIDTH * (float) stored / capacity)));
    }

    /** Bar colour (RGB): magenta when nearly empty, through violet, to flux cyan when full. */
    public static int barColor(int stored, int capacity) {
        float f = capacity <= 0 ? 0 : Math.max(0, Math.min(1, (float) stored / capacity));
        int r = Math.round(255 + (56 - 255) * f);
        int g = Math.round(63 + (243 - 63) * f);
        int b = Math.round(210 + (255 - 210) * f);
        return (r << 16) | (g << 8) | b;
    }

    /** Flux a charger gets from a fuel that burns {@code burnTicks} ticks in a furnace. */
    public static int fromBurnTicks(int burnTicks) {
        return Math.max(0, burnTicks) * PER_BURN_TICK;
    }

    /** "12,000 / 30,000 Flux" style text for tooltips and screens. */
    public static String format(int stored, int capacity) {
        return String.format(java.util.Locale.ROOT, "%,d / %,d", stored, capacity);
    }

    /** Percentage, rounded down, for the HUD. */
    public static int percent(int stored, int capacity) {
        return capacity <= 0 ? 0 : (int) Math.floor(100.0 * Math.max(0, stored) / capacity);
    }
}
