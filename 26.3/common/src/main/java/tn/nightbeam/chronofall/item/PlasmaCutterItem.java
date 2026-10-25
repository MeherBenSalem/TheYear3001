package tn.nightbeam.chronofall.item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tn.nightbeam.chronofall.Chronofall;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.ChronoConfig;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.util.Fx;

/**
 * Hold use to cut the block you look at from up to 12 blocks away. Cutting time follows the block's hardness
 * (obsidian about four seconds); the block drops as if mined by a diamond tool, through the player's own game mode so
 * spawn protection and claim mods apply.
 */
public class PlasmaCutterItem extends FluxToolItem {
    private static final Map<UUID, Cut> CUTS = new HashMap<>();

    private record Cut(BlockPos pos, float progress) {
    }

    public PlasmaCutterItem(Properties properties) {
        super(Flux.CUTTER_CAPACITY, properties);
    }

    /** Ticks to cut a block of this hardness (a negative hardness cannot be cut). */
    public static int cutTicks(float hardness) {
        return hardness < 0 ? -1 : 1 + (int) Math.ceil(hardness * 1.6F);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.getAbilities().instabuild && flux(stack) < Flux.CUTTER_PER_BLOCK) {
            return InteractionResult.FAIL;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72_000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return !state.is(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (!(entity instanceof Player player)) {
            return;
        }
        double range = ChronoConfig.get().cutterRange();
        HitResult hit = player.pick(range, 1.0F, false);
        Vec3 from = player.getEyePosition().add(player.getLookAngle().scale(0.4)).add(0, -0.2, 0)
                .add(Vec3.directionFromRotation(0, player.getYRot() + 90).scale(player.getUsedItemHand() == InteractionHand.MAIN_HAND ? 0.25 : -0.25));
        Vec3 to = hit.getType() == HitResult.Type.MISS ? player.getEyePosition().add(player.getLookAngle().scale(range)) : hit.getLocation();
        if (level.isClientSide()) {
            Fx.beam(level, ModContent.FLUX_PARTICLE.get(), from, to, 0.35);
            Chronofall.client().loopSound(player, "cutter");
            return;
        }
        if (!(player instanceof ServerPlayer serverPlayer) || !(level instanceof ServerLevel server)) {
            return;
        }
        if (!(hit instanceof BlockHitResult blockHit) || hit.getType() != HitResult.Type.BLOCK) {
            stop(server, player);
            return;
        }
        BlockPos pos = blockHit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        int ticks = cutTicks(state.getDestroySpeed(level, pos));
        if (state.isAir() || ticks < 0 || !server.mayInteract(player, pos)) {
            stop(server, player);
            return;
        }
        if (remaining % 4 == 0) {
            server.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA, to.x, to.y, to.z, 1, 0.1, 0.1, 0.1, 0.0);
        }
        Cut cut = CUTS.get(player.getUUID());
        float progress = cut != null && cut.pos().equals(pos) ? cut.progress() + 1.0F / ticks : 1.0F / ticks;
        if (progress < 1.0F) {
            CUTS.put(player.getUUID(), new Cut(pos, progress));
            server.destroyBlockProgress(breakerId(player), pos, (int) (progress * 10.0F) - 1);
            return;
        }
        if (!player.getAbilities().instabuild && !drain(stack, Flux.CUTTER_PER_BLOCK)) {
            stop(server, player);
            player.stopUsingItem();
            return;
        }
        server.destroyBlockProgress(breakerId(player), pos, -1);
        CUTS.remove(player.getUUID());
        serverPlayer.gameMode.destroyBlock(pos);
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (level instanceof ServerLevel server && entity instanceof Player player) {
            stop(server, player);
        }
        return true;
    }

    private static void stop(ServerLevel level, Player player) {
        Cut cut = CUTS.remove(player.getUUID());
        if (cut != null) {
            level.destroyBlockProgress(breakerId(player), cut.pos(), -1);
        }
    }

    /** Vanilla hides a breaker's own crack from them; a separate id shows it to the cutter's user too. */
    private static int breakerId(Player player) {
        return -1_000 - player.getId();
    }
}
