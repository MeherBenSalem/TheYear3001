package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import tn.nightbeam.chronofall.block.FabricatorBlockEntity;
import tn.nightbeam.chronofall.core.FabricatorRecipes;

/**
 * The hologram above the Fabricator: the selected item (or the one printing) spins full-bright above the emitter.
 * While printing it spins faster and grows to full size as the print progresses.
 */
public class FabricatorRenderer implements BlockEntityRenderer<FabricatorBlockEntity> {
    private ItemStack cached = ItemStack.EMPTY;
    private int cachedIndex = -2;

    public FabricatorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FabricatorBlockEntity fab, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        boolean printing = fab.printing() >= 0;
        int index = printing ? fab.printing() : fab.selected();
        FabricatorRecipes.Recipe recipe = FabricatorRecipes.get(index);
        if (recipe == null || fab.getLevel() == null) {
            return;
        }
        if (index != cachedIndex) {
            cached = FabricatorBlockEntity.result(recipe);
            cachedIndex = index;
        }
        float time = fab.getLevel().getGameTime() + partialTick;
        float grow = printing ? 0.35F + 0.65F * Mth.clamp(fab.progress() / (float) recipe.ticks(), 0, 1) : 1.0F;
        pose.pushPose();
        pose.translate(0.5, 1.32 + Mth.sin(time * 0.08F) * 0.04, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(time * (printing ? 6.0F : 2.0F)));
        pose.scale(0.55F * grow, 0.55F * grow, 0.55F * grow);
        Minecraft.getInstance().getItemRenderer().renderStatic(cached, ItemDisplayContext.FIXED, 0xF000F0, OverlayTexture.NO_OVERLAY, pose, buffers,
                fab.getLevel(), (int) fab.getBlockPos().asLong());
        pose.popPose();
    }

    @Override
    public int getViewDistance() {
        return 48;
    }
}
