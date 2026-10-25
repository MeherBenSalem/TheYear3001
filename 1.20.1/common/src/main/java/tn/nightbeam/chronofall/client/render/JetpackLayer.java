package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.chronofall.client.model.ModLayers;
import tn.nightbeam.chronofall.client.model.PartModel;
import tn.nightbeam.chronofall.item.Jetpack;
import tn.nightbeam.chronofall.item.JetpackItem;

/** The jetpack model on a player's back while it is worn in the chest slot. */
public class JetpackLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PartModel model;

    public JetpackLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet models) {
        super(parent);
        model = new PartModel(models.bakeLayer(ModLayers.JETPACK));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack pack = Jetpack.worn(player);
        if (pack.isEmpty()) {
            return;
        }
        pose.pushPose();
        getParentModel().body.translateAndRotate(pose);
        model.renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(Textures.entity("jetpack"))), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (((JetpackItem) pack.getItem()).flux(pack) > 0) {
            model.renderToBuffer(pose, buffers.getBuffer(RenderType.eyes(Textures.glow("jetpack"))), 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        pose.popPose();
    }
}
