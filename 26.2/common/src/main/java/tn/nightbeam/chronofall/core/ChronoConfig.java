package tn.nightbeam.chronofall.core;

import com.google.gson.JsonObject;
import java.nio.file.Path;
import tn.nightbeam.chronofall.Constants;

/** {@code config/chronofall.json}, read once at startup. */
public final class ChronoConfig {
    public static final String FILE = "chronofall.json";

    private static volatile Values values = Values.defaults();

    private ChronoConfig() {
    }

    public record Rifts(boolean enabled, double chancePerMinute, int minMinutesBetween, int minDistance, int maxDistance, int maxSentinels,
                        boolean crater, boolean announce, int beaconShards) {
    }

    public record Gates(boolean crossDimension, int chargeTicks, int cooldownTicks) {
    }

    public record Robots(double slamDamage, double slamRadius, int droneRange, boolean droneLampLight) {
    }

    public record Values(Rifts rifts, Gates gates, Robots robots, int cutterRange) {
        public static Values defaults() {
            return read(spec().defaults());
        }
    }

    public static CommentedJson spec() {
        JsonObject root = Json.obj(
                "rifts", Json.obj("enabled", true, "chance_per_minute", 0.04, "min_minutes_between", 15, "min_distance", 24,
                        "max_distance", 48, "max_sentinels", 3, "crater", true, "announce", true, "beacon_shards", 4),
                "rift_gates", Json.obj("cross_dimension", true, "charge_ticks", 40, "cooldown_ticks", 60),
                "robots", Json.obj("mech_slam_damage", 8.0, "mech_slam_radius", 4.0, "drone_range", 10, "drone_lamp_light", true),
                "plasma_cutter_range", 12);
        return new CommentedJson(root)
                .header("""
                        Chronofall. Changes apply after a restart.""")
                .comment("rifts", """
                        Chrono Rifts: at night, once a minute, each player in the Overworld has chance_per_minute (0 to 1)
                        of a rift opening above a spot min_distance to max_distance blocks away, at least
                        min_minutes_between minutes after their last one. A Chrono Pod crashes there with a Rift Cache
                        and up to max_sentinels Sentinels (none on Peaceful). crater: scorch and dig a small crater
                        (no other blocks are destroyed). announce: tell nearby players. beacon_shards: Rift Shards a
                        Rift Beacon takes to call a rift.""")
                .comment("rift_gates", """
                        Rift Gates: cross_dimension lets linked gates be in different dimensions. A player standing on a
                        linked gate for charge_ticks (20 = one second) is sent to the other gate, then waits
                        cooldown_ticks before the next jump.""")
                .comment("robots", """
                        mech_slam_damage and mech_slam_radius: the Mech's ground slam. drone_range: how far a Drone looks
                        for items (Collector chip) and hostile mobs (Guard chip). drone_lamp_light: the Lamp chip places
                        a light block that follows the drone.""")
                .comment("plasma_cutter_range", "How far the Plasma Cutter reaches, in blocks.");
    }

    public static Values read(JsonObject root) {
        Rifts rifts = new Rifts(
                Json.bool(root, "rifts.enabled", true),
                clamp(Json.num(root, "rifts.chance_per_minute", 0.04), 0, 1),
                Math.max(0, Json.integer(root, "rifts.min_minutes_between", 15)),
                Math.max(8, Json.integer(root, "rifts.min_distance", 24)),
                Math.max(8, Json.integer(root, "rifts.max_distance", 48)),
                Math.max(0, Math.min(8, Json.integer(root, "rifts.max_sentinels", 3))),
                Json.bool(root, "rifts.crater", true),
                Json.bool(root, "rifts.announce", true),
                Math.max(1, Math.min(64, Json.integer(root, "rifts.beacon_shards", 4))));
        if (rifts.maxDistance() < rifts.minDistance()) {
            rifts = new Rifts(rifts.enabled(), rifts.chancePerMinute(), rifts.minMinutesBetween(), rifts.minDistance(), rifts.minDistance(),
                    rifts.maxSentinels(), rifts.crater(), rifts.announce(), rifts.beaconShards());
        }
        Gates gates = new Gates(
                Json.bool(root, "rift_gates.cross_dimension", true),
                Math.max(1, Json.integer(root, "rift_gates.charge_ticks", 40)),
                Math.max(0, Json.integer(root, "rift_gates.cooldown_ticks", 60)));
        Robots robots = new Robots(
                clamp(Json.num(root, "robots.mech_slam_damage", 8.0), 0, 100),
                clamp(Json.num(root, "robots.mech_slam_radius", 4.0), 1, 12),
                Math.max(2, Math.min(32, Json.integer(root, "robots.drone_range", 10))),
                Json.bool(root, "robots.drone_lamp_light", true));
        int cutterRange = Math.max(2, Math.min(32, Json.integer(root, "plasma_cutter_range", 12)));
        return new Values(rifts, gates, robots, cutterRange);
    }

    private static double clamp(double value, double min, double max) {
        return Double.isNaN(value) ? min : Math.max(min, Math.min(max, value));
    }

    public static void load(Path configDir) {
        values = read(spec().load(configDir.resolve(FILE)));
        Constants.LOG.info("Loaded {} (rifts {}, {} per minute at night)", FILE, values.rifts().enabled() ? "on" : "off",
                values.rifts().chancePerMinute());
    }

    public static Values get() {
        return values;
    }

    /** For tests and the QA command: replaces the values in memory. */
    public static void set(Values newValues) {
        values = newValues;
    }
}
