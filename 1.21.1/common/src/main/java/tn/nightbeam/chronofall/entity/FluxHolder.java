package tn.nightbeam.chronofall.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.chronofall.content.ModContent;
import tn.nightbeam.chronofall.core.Flux;

/** A robot or vehicle that stores Flux and can be charged by a Flux Charger or a Flux Cell. */
public interface FluxHolder {
    int flux();

    int fluxCapacity();

    void setFlux(int value);

    /** Adds up to {@code amount}; returns how much was taken. */
    default int charge(int amount) {
        int room = Flux.room(flux(), amount, fluxCapacity());
        if (room > 0) {
            setFlux(flux() + room);
        }
        return room;
    }

    /** Right-clicking with a Flux Cell adds {@link Flux#PER_CELL} and uses the cell up. */
    static boolean useCell(FluxHolder holder, Player player, ItemStack held) {
        if (held.isEmpty() || held.getItem() != ModContent.item("flux_cell")) {
            return false;
        }
        if (holder.charge(Flux.PER_CELL) > 0) {
            held.consume(1, player);
            Entity entity = (Entity) holder;
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BEACON_POWER_SELECT, SoundSource.NEUTRAL, 0.6F, 1.8F);
        }
        player.displayClientMessage(Component.translatable("message.chronofall.flux", Flux.percent(holder.flux(), holder.fluxCapacity())), true);
        return true;
    }
}
