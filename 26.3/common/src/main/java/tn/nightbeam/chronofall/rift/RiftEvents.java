package tn.nightbeam.chronofall.rift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.Constants;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.ChronoConfig;
import tn.nightbeam.chronofall.core.RiftRules;
import tn.nightbeam.chronofall.entity.ChronoPodEntity;
import tn.nightbeam.chronofall.entity.SentinelEntity;
import tn.nightbeam.chronofall.util.Fx;

/**
 * Chrono Rifts: the night-time event (and the Rift Beacon and {@code /chronofall rift}). A rift opens in the sky
 * above the landing spot for two seconds, drops a Chrono Pod, and closes; the pod's impact digs a small crater,
 * places a Rift Cache and spawns its Sentinel guard.
 */
public final class RiftEvents {
    public static final int OPEN_TICKS = 40;
    public static final int CLOSE_TICKS = 40;
    private static final double VIEW_RANGE = 192;
    private static final List<Rift> ACTIVE = new ArrayList<>();
    private static final Map<UUID, Long> LAST_RIFT = new HashMap<>();
    @Nullable
    private static BlockPos lastLanding;

    private static final class Rift {
        final ResourceKey<Level> dimension;
        final Vec3 sky;
        int age;

        Rift(ResourceKey<Level> dimension, Vec3 sky) {
            this.dimension = dimension;
            this.sky = sky;
        }
    }

    private RiftEvents() {
    }

    /** Where the last pod landed (its Rift Cache), for the command and QA. */
    @Nullable
    public static BlockPos lastLanding() {
        return lastLanding;
    }

    public static void tick(MinecraftServer server) {
        Iterator<Rift> it = ACTIVE.iterator();
        while (it.hasNext()) {
            Rift rift = it.next();
            ServerLevel level = server.getLevel(rift.dimension);
            if (level == null || ++rift.age > OPEN_TICKS + CLOSE_TICKS) {
                it.remove();
                continue;
            }
            tickRift(level, rift);
        }
        ChronoConfig.Rifts cfg = ChronoConfig.get().rifts();
        if (!cfg.enabled() || server.getTickCount() % RiftRules.CHECK_INTERVAL != 0) {
            return;
        }
        ServerLevel overworld = server.overworld();
        long gameTime = overworld.getGameTime();
        for (ServerPlayer player : overworld.players()) {
            if (player.isSpectator()) {
                continue;
            }
            long last = LAST_RIFT.getOrDefault(player.getUUID(), -1L);
            if (RiftRules.shouldOpen(overworld.getOverworldClockTime(), gameTime, last, cfg.minMinutesBetween() * 1200, cfg.chancePerMinute(),
                    overworld.getRandom().nextDouble())) {
                openNear(overworld, player);
            }
        }
    }

    private static void tickRift(ServerLevel level, Rift rift) {
        int age = rift.age;
        double radius = age <= OPEN_TICKS ? 0.6 + 5.4 * age / OPEN_TICKS : 6.0 * (1.0 - (double) (age - OPEN_TICKS) / CLOSE_TICKS);
        double phase = age * 0.15;
        // Two counter-rotating rings (rift magenta outside, flux cyan inside) and a swirl of portal motes in the middle.
        Fx.ring(level, ModContent.RIFT_PARTICLE.get(), rift.sky, radius, 80, phase, VIEW_RANGE);
        Fx.ring(level, ModContent.RIFT_PARTICLE.get(), rift.sky, radius * 0.82, 56, -phase * 0.7, VIEW_RANGE);
        Fx.ring(level, ModContent.FLUX_PARTICLE.get(), rift.sky, radius * 0.55, 32, -phase * 1.5, VIEW_RANGE);
        if (age % 2 == 0) {
            Fx.send(level, ParticleTypes.REVERSE_PORTAL, rift.sky.x, rift.sky.y, rift.sky.z, 16, radius * 0.4, 0.3, radius * 0.4, 0.05, VIEW_RANGE);
        }
        // While it opens, rift motes rain from the tear towards where the pod will land.
        if (age <= OPEN_TICKS) {
            for (int i = 0; i < 4; i++) {
                double a = level.getRandom().nextDouble() * Math.PI * 2;
                double r = level.getRandom().nextDouble() * radius * 0.5;
                // count 0: the three spreads are the motion, scaled by the speed.
                Fx.send(level, ModContent.RIFT_PARTICLE.get(), rift.sky.x + Math.cos(a) * r, rift.sky.y - 1, rift.sky.z + Math.sin(a) * r, 0,
                        0, -1, 0, 0.7, VIEW_RANGE);
            }
        }
        if (age == 1) {
            level.playSound(null, rift.sky.x, rift.sky.y, rift.sky.z, ModContent.sound("rift_open"), SoundSource.AMBIENT, 10.0F, 1.0F);
        }
        if (age == OPEN_TICKS) {
            ChronoPodEntity pod = new ChronoPodEntity(ModContent.CHRONO_POD.get(), level);
            pod.snapTo(rift.sky.x, rift.sky.y - 1.0, rift.sky.z, level.getRandom().nextFloat() * 360, 0);
            pod.setDeltaMovement(0, -0.4, 0);
            level.addFreshEntity(pod);
            level.playSound(null, rift.sky.x, rift.sky.y, rift.sky.z, ModContent.sound("pod_fall"), SoundSource.AMBIENT, 8.0F, 1.0F);
        }
    }

    /** Opens a rift above a random spot 24 to 48 blocks (config) from {@code player}. */
    public static BlockPos openNear(ServerLevel level, ServerPlayer player) {
        ChronoConfig.Rifts cfg = ChronoConfig.get().rifts();
        RiftRules.Offset offset = RiftRules.landing(new Random(level.getRandom().nextLong()), cfg.minDistance(), cfg.maxDistance());
        int x = player.getBlockX() + offset.dx();
        int z = player.getBlockZ() + offset.dz();
        BlockPos ground = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
        LAST_RIFT.put(player.getUUID(), level.getGameTime());
        Constants.LOG.info("Chrono Rift for player {}", player.getName().getString());
        open(level, ground);
        return ground;
    }

    /** Opens a rift above {@code ground} and warns players nearby. */
    public static void open(ServerLevel level, BlockPos ground) {
        int top = level.getMaxY() - 3;
        double skyY = Math.max(ground.getY() + 12, Math.min(top, ground.getY() + 40));
        ACTIVE.add(new Rift(level.dimension(), new Vec3(ground.getX() + 0.5, skyY, ground.getZ() + 0.5)));
        Constants.LOG.info("Chrono Rift opening above {} {} {} in {}", ground.getX(), ground.getY(), ground.getZ(), level.dimension().identifier());
        if (!ChronoConfig.get().rifts().announce()) {
            return;
        }
        for (ServerPlayer player : level.players()) {
            int dx = ground.getX() - player.getBlockX();
            int dz = ground.getZ() - player.getBlockZ();
            if (dx * dx + dz * dz <= 160 * 160) {
                player.sendOverlayMessage(Component.translatable("message.chronofall.rift.opening",
                        Component.translatable("direction.chronofall." + RiftRules.direction(dx, dz))));
            }
        }
    }

    /** The pod hits the ground at {@code pos}: crater, Rift Cache, Sentinels. */
    public static void impact(ServerLevel level, BlockPos pos) {
        ChronoConfig.Rifts cfg = ChronoConfig.get().rifts();
        Vec3 centre = Vec3.atCenterOf(pos);
        level.playSound(null, centre.x, centre.y, centre.z, ModContent.sound("pod_impact"), SoundSource.BLOCKS, 8.0F, 0.9F);
        Fx.send(level, ParticleTypes.EXPLOSION_EMITTER, centre.x, centre.y + 0.5, centre.z, 1, 0, 0, 0, 0, VIEW_RANGE);
        Fx.send(level, ModContent.RIFT_PARTICLE.get(), centre.x, centre.y + 1, centre.z, 80, 1.5, 1.0, 1.5, 0.15, VIEW_RANGE);
        Fx.send(level, ParticleTypes.LARGE_SMOKE, centre.x, centre.y + 1, centre.z, 40, 2.0, 1.0, 2.0, 0.05, VIEW_RANGE);
        Random random = new Random(level.getRandom().nextLong());
        BlockPos cache = pos.above();
        if (cfg.crater()) {
            cache = crater(level, pos, random);
        }
        if (cache.getY() <= level.getMinY()) {
            cache = new BlockPos(cache.getX(), level.getMinY() + 1, cache.getZ());
        }
        while (!level.getBlockState(cache).canBeReplaced() && cache.getY() < pos.getY() + 4) {
            cache = cache.above();
        }
        level.setBlock(cache, ModContent.RIFT_CACHE.get().defaultBlockState(), 3);
        lastLanding = cache;
        int wanted = RiftRules.sentinels(random, level.getDifficulty() == Difficulty.PEACEFUL, cfg.maxSentinels());
        int sentinels = 0;
        for (int i = 0; i < wanted; i++) {
            double angle = Math.PI * 2 * (i + random.nextDouble() * 0.5) / Math.max(1, sentinels);
            int x = cache.getX() + (int) Math.round(Math.cos(angle) * 5);
            int z = cache.getZ() + (int) Math.round(Math.sin(angle) * 5);
            BlockPos at = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
            SentinelEntity sentinel = ModContent.SENTINEL.get().spawn(level, at, EntitySpawnReason.EVENT);
            if (sentinel != null) {
                // Guards stay with their cache instead of despawning when players are far away.
                sentinel.setPersistenceRequired();
                sentinels++;
            }
        }
        Constants.LOG.info("Chrono Pod landed at {} {} {} with {} Sentinels", cache.getX(), cache.getY(), cache.getZ(), sentinels);
    }

    /** Digs a bowl three blocks wide around {@code pos}, lines it with scorched blocks and returns its bottom centre. */
    private static BlockPos crater(ServerLevel level, BlockPos pos, Random random) {
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                double r = Math.sqrt(dx * dx + dz * dz);
                for (int dy = 3; dy >= -2; dy--) {
                    double allowed = 3.2 + Math.min(0, dy);
                    BlockPos p = pos.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(p);
                    if (r <= allowed && (dy > 0 ? state.canBeReplaced() || state.is(BlockTags.LEAVES) : diggable(state))) {
                        level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
                if (r <= 4.5) {
                    int depth = r <= 1.2 ? -3 : r <= 2.2 ? -2 : r <= 3.2 ? -1 : 0;
                    BlockPos floor = pos.offset(dx, depth, dz);
                    while (level.getBlockState(floor).isAir() && floor.getY() > pos.getY() - 5) {
                        floor = floor.below();
                    }
                    if (diggable(level.getBlockState(floor)) && random.nextDouble() < (r <= 3.2 ? 0.9 : 0.45)) {
                        double roll = random.nextDouble();
                        BlockState scorch = roll < 0.15 ? ModContent.RIFT_GLASS.get().defaultBlockState()
                                : roll < 0.35 ? ModContent.SCRAP_DEBRIS.get().defaultBlockState()
                                : roll < 0.45 ? Blocks.MAGMA_BLOCK.defaultBlockState() : Blocks.BLACKSTONE.defaultBlockState();
                        level.setBlock(floor, scorch, 3);
                    }
                }
            }
        }
        BlockPos bottom = pos.below(2);
        while (!level.getBlockState(bottom.below()).isSolid() && bottom.getY() > Math.max(pos.getY() - 6, level.getMinY() + 1)) {
            bottom = bottom.below();
        }
        return bottom;
    }

    /** Natural ground the impact may dig out or scorch; never anything with a block entity or that cannot be mined. */
    private static boolean diggable(BlockState state) {
        if (state.isAir() || state.hasBlockEntity()) {
            return false;
        }
        return state.is(BlockTags.SUBSTRATE_OVERWORLD) || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.SAND) || state.is(Blocks.GRAVEL)
                || state.is(BlockTags.SNOW) || state.is(BlockTags.TERRACOTTA) || state.is(Blocks.SANDSTONE) || state.is(Blocks.RED_SANDSTONE)
                || state.is(Blocks.CLAY) || state.is(Blocks.MUD) || state.is(BlockTags.ICE) || state.is(Blocks.FARMLAND) || state.is(Blocks.DIRT_PATH)
                || state.canBeReplaced();
    }
}
