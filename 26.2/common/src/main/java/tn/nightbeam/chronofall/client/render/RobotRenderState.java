package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** What the drone, sentinel and mech models need from their entity each frame. */
public class RobotRenderState extends LivingEntityRenderState {
    /** Whether the glow layer is drawn (powered drone or mech, living sentinel). */
    public boolean lit;
    /** Drone: powered down and resting. */
    public boolean poweredDown;
    /** Sentinel: aiming its rifle arm. */
    public boolean aggressive;
    /** Mech: ticks left of the ground-slam arm swing. */
    public int slamTicks;
}
