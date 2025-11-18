// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 4.12.6 Exported for Minecraft version 1.19 or later with
 * Mojang mappings
 * 
 * @author Author
 */
public class shipAnimation {
	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(0.0F).looping()
			.addAnimation("submarine",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F),
									AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("propeller",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F,
							KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.build();

	public static final AnimationDefinition movement = AnimationDefinition.Builder.withLength(0.0F).looping()
			.addAnimation("submarine",
					new AnimationChannel(AnimationChannel.Targets.ROTATION,
							new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
									AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("propeller",
					new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0.0F,
							KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
			.build();
}