package tn.nightbeam.robotica.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import tn.nightbeam.robotica.init.RoboticaModFluids;

public class OilBlock extends LiquidBlock {
	public OilBlock() {
		super(RoboticaModFluids.OIL.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).strength(100f).noCollision().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
	}
}
