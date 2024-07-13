package ziyue.filters.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
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
 * @see CreativeScreenMixin
 * @since 1.0.0
 */

@Mixin(DisplayEffectsScreen.class)
public abstract class DisplayEffectsScreenMixin<T extends Container> extends ContainerScreen<T>
{
    public DisplayEffectsScreenMixin(T p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Inject(at = @At("HEAD"), method = "renderEffects")
    private void beforeRenderEffects(MatrixStack matrices, CallbackInfo ci) {
        final Screen screen = minecraft.getInstance().screen;
        if (screen instanceof CreativeScreen) {
            if (FilterBuilder.isTabHasFilters(((CreativeScreen) screen).getSelectedTab())) {
                this.leftPos = this.leftPos - 55; // move effect labels to avoid overlap with filters
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "renderEffects")
    private void afterRenderEffects(MatrixStack matrices, CallbackInfo ci) {
        final Screen screen = minecraft.getInstance().screen;
        if (screen instanceof CreativeScreen) {
            if (FilterBuilder.isTabHasFilters(((CreativeScreen) screen).getSelectedTab())) {
                this.leftPos = this.leftPos + 55;
            }
        }
    }
}
