
package tn.nightbeam.nextmillennium.block;

import tn.nightbeam.nextmillennium.init.Theyear3001ModFluids;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.LiquidBlock;

public class OilBlock extends LiquidBlock {
	public OilBlock() {
		super(() -> Theyear3001ModFluids.OIL.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).strength(100f).noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
	}
}
