package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.model.PartModel;
import tn.nightbeam.chronofall.item.JetpackItem;

/** The jetpack model on a player's back while it is worn in the chest slot. */
public class JetpackLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final PartModel model;

    public JetpackLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent, EntityModelSet models) {
        super(parent);
        model = new PartModel(models.bakeLayer(ModLayers.JETPACK));
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, AvatarRenderState state, float yRot, float xRot) {
        ItemStack pack = state.chestEquipment;
        if (!(pack.getItem() instanceof JetpackItem jetpack)) {
            return;
        }
        pose.pushPose();
        getParentModel().body.translateAndRotate(pose);
        collector.submitModel(model, Unit.INSTANCE, pose, RenderTypes.entityCutout(Textures.entity("jetpack")), light, OverlayTexture.NO_OVERLAY,
                state.outlineColor);
        if (jetpack.flux(pack) > 0) {
            collector.order(1).submitModel(model, Unit.INSTANCE, pose, RenderTypes.eyes(Textures.glow("jetpack")), 0xF000F0, OverlayTexture.NO_OVERLAY,
                    state.outlineColor);
        }
        pose.popPose();
    }
}
