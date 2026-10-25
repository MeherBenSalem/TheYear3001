package tn.nightbeam.chronofall.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import tn.nightbeam.chronofall.content.ModContent;

/** Spawns a Sentinel where it is used (a plain item, so it does not need the entity type at registration). */
public class SentinelEggItem extends Item {
    public SentinelEggItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        if (ModContent.SENTINEL.get().spawn(level, context.getItemInHand(), context.getPlayer(),
                context.getClickedPos().relative(context.getClickedFace()), EntitySpawnReason.SPAWN_ITEM_USE, true, false) != null) {
            context.getItemInHand().consume(1, context.getPlayer());
        }
        return InteractionResult.CONSUME;
    }
}
