package tn.nightbeam.chronofall.client.render;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

/** The skiff and pod renderers' state: heading, bank or spin, and whether the glow strips are lit. */
public class PartRenderState extends EntityRenderState {
    public float yRot;
    public float roll;
    public boolean lit;
}
