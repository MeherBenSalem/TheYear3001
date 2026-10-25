package tn.nightbeam.chronofall.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

/** A model that is just its parts, for things that are not living entities (skiff, pod, jetpack). */
public class PartModel extends Model.Simple {
    public PartModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }
}
