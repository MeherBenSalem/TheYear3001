package tn.nightbeam.chronofall.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.rift.RiftEvents;

/** Counts down three seconds with a rising beam, then opens a rift 10 to 16 blocks away. */
public class RiftBeaconBlockEntity extends BlockEntity {
    public static final int COUNTDOWN = 60;
    private int countdown;

    public RiftBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(ModContent.RIFT_BEACON_ENTITY.get(), pos, state);
    }

    public boolean active() {
        return countdown > 0;
    }

    public void start() {
        countdown = COUNTDOWN;
        setChanged();
        if (level != null) {
            level.playSound(null, worldPosition, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.5F, 0.7F);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RiftBeaconBlockEntity beacon) {
        if (beacon.countdown <= 0 || !(level instanceof ServerLevel server)) {
            return;
        }
        beacon.countdown--;
        int height = (COUNTDOWN - beacon.countdown) / 2;
        for (int y = 0; y < height; y += 2) {
            server.sendParticles(ModContent.RIFT_PARTICLE.get(), pos.getX() + 0.5, pos.getY() + 1.2 + y, pos.getZ() + 0.5, 1, 0.05, 0.2, 0.05, 0);
        }
        if (beacon.countdown == 0) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double distance = 10 + level.random.nextDouble() * 6;
            int x = pos.getX() + (int) Math.round(Math.cos(angle) * distance);
            int z = pos.getZ() + (int) Math.round(Math.sin(angle) * distance);
            RiftEvents.open(server, new BlockPos(x, server.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z));
        }
        beacon.setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        countdown = tag.getInt("Countdown");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Countdown", countdown);
    }
}
