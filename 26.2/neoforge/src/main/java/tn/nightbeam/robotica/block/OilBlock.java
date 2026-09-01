package tn.nightbeam.robotica.block;

import tn.nightbeam.robotica.init.RoboticaModFluidsNeoForge;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.LiquidBlock;

public class OilBlock extends LiquidBlock {
	public OilBlock() {
		super(RoboticaModFluidsNeoForge.OIL.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).strength(100f).noCollision().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
	}
}
