package tn.nightbeam.chronofall.block;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import tn.nightbeam.chronofall.Constants;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.RiftRules;

/** The salvage a Chrono Pod brings. Unbreakable; right-click cracks it open and it drops its loot. */
public class RiftCacheBlock extends Block {
    public RiftCacheBlock(Properties properties) {
        super(properties);
    }

    private InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(level instanceof ServerLevel server)) {
            return InteractionResult.SUCCESS;
        }
        crack(server, pos);
        return InteractionResult.CONSUME;
    }

    public static void crack(ServerLevel level, BlockPos pos) {
        level.removeBlock(pos, false);
        for (RiftRules.Stack loot : RiftRules.cacheLoot(new Random(level.random.nextLong()))) {
            popResource(level, pos, new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(loot.item())), loot.count()));
        }
        level.playSound(null, pos, ModContent.sound("cache_open"), SoundSource.BLOCKS, 1.2F, 1.0F);
        level.sendParticles(ModContent.RIFT_PARTICLE.get(), pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 40, 0.4, 0.4, 0.4, 0.12);
        level.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 12, 0.3, 0.3, 0.3, 0.05);
        Constants.LOG.info("Rift Cache opened at {} {} {}", pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0) {
            level.addParticle(ModContent.RIFT_PARTICLE.get(), pos.getX() + random.nextDouble(), pos.getY() + 1.05, pos.getZ() + random.nextDouble(),
                    0, 0.05 + random.nextDouble() * 0.05, 0);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.InteractionHand hand, BlockHitResult hit) {
        return useWithoutItem(state, level, pos, player, hit);
    }
}
