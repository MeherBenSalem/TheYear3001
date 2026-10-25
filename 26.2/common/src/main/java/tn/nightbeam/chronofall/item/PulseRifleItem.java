package tn.nightbeam.chronofall.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.Flux;
import tn.nightbeam.chronofall.entity.LaserBoltEntity;

/** Fires a laser bolt (7 damage) every half second for 60 Flux. */
public class PulseRifleItem extends FluxToolItem {
    public static final float DAMAGE = 7.0F;
    public static final int COOLDOWN = 10;

    public PulseRifleItem(Properties properties) {
        super(Flux.RIFLE_CAPACITY, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.getAbilities().instabuild && flux(stack) < Flux.RIFLE_PER_SHOT) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) {
            if (!player.getAbilities().instabuild) {
                drain(stack, Flux.RIFLE_PER_SHOT);
            }
            LaserBoltEntity.fire(level, player, player.getEyePosition().add(player.getLookAngle().scale(0.6)).add(0, -0.15, 0),
                    player.getLookAngle(), DAMAGE, LaserBoltEntity.CYAN);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModContent.sound("laser_shot"), SoundSource.PLAYERS, 0.8F,
                    1.1F + level.getRandom().nextFloat() * 0.2F);
        }
        player.getCooldowns().addCooldown(stack, COOLDOWN);
        return InteractionResult.SUCCESS;
    }
}
