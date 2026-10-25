package tn.nightbeam.chronofall.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import tn.nightbeam.chronofall.block.FabricatorBlockEntity;
import tn.nightbeam.chronofall.core.FabricatorRecipes;

/**
 * The hologram above the Fabricator: the selected item (or the one printing) spins full-bright above the emitter.
 * While printing it spins faster and grows to full size as the print progresses.
 */
public class FabricatorRenderer implements BlockEntityRenderer<FabricatorBlockEntity, FabricatorRenderer.State> {
    private final ItemModelResolver items;
    private ItemStack cached = ItemStack.EMPTY;
    private int cachedIndex = -2;

    public FabricatorRenderer(BlockEntityRendererProvider.Context context) {
        this.items = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float time;
        public float grow;
        public boolean printing;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(FabricatorBlockEntity fab, State state, float partialTick, Vec3 camera,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(fab, state, partialTick, camera, breakProgress);
        state.item.clear();
        state.printing = fab.printing() >= 0;
        int index = state.printing ? fab.printing() : fab.selected();
        FabricatorRecipes.Recipe recipe = FabricatorRecipes.get(index);
        if (recipe == null || fab.getLevel() == null) {
            return;
        }
        if (index != cachedIndex) {
            cached = FabricatorBlockEntity.result(recipe);
            cachedIndex = index;
        }
        state.time = fab.getLevel().getGameTime() + partialTick;
        state.grow = state.printing ? 0.35F + 0.65F * Mth.clamp(fab.progress() / (float) recipe.ticks(), 0, 1) : 1.0F;
        items.updateForTopItem(state.item, cached, ItemDisplayContext.FIXED, fab.getLevel(), null, (int) fab.getBlockPos().asLong());
    }

    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.item.isEmpty()) {
            return;
        }
        pose.pushPose();
        pose.translate(0.5, 1.32 + Mth.sin(state.time * 0.08F) * 0.04, 0.5);
        pose.rotateDegrees(Axis.YP, state.time * (state.printing ? 6.0F : 2.0F));
        pose.scale(0.55F * state.grow, 0.55F * state.grow, 0.55F * state.grow);
        state.item.submit(pose, collector, 0xF000F0, OverlayTexture.NO_OVERLAY, 0);
        pose.popPose();
    }

    @Override
    public int getViewDistance() {
        return 48;
    }
}
