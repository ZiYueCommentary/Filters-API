package ziyue.filters.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.filters.Filter;
import ziyue.filters.FilterBuilder;

/**
 * Change effect labels' position in creative mode tab with filters.
 *
 * @author ZiYueCommentary
 * @see Filter
 * @see CreativeModeInventoryScreenMixin
 * @since 1.0.0
 */

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
    public EffectRenderingInventoryScreenMixin(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Inject(at = @At("HEAD"), method = "renderEffects")
    private void beforeRenderEffects(PoseStack p_194015_, int p_194016_, int p_194017_, CallbackInfo ci) {
        final Screen screen = minecraft.getInstance().screen;
        if (screen instanceof CreativeModeInventoryScreen inventory) {
            if (FilterBuilder.isTabHasFilters(inventory.getSelectedTab())) {
                this.leftPos = this.leftPos - 55; // move effect labels to avoid overlap with filters
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "renderEffects")
    private void afterRenderEffects(PoseStack p_194015_, int p_194016_, int p_194017_, CallbackInfo ci) {
        final Screen screen = minecraft.getInstance().screen;
        if (screen instanceof CreativeModeInventoryScreen inventory) {
            if (FilterBuilder.isTabHasFilters(inventory.getSelectedTab())) {
                this.leftPos = this.leftPos + 55;
            }
        }
    }
}
