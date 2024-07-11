package ziyue.filters.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
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
 * @see CreativeInventoryScreenMixin
 * @since 1.0.0
 */

@Mixin(AbstractInventoryScreen.class)
public abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T>
{
    public AbstractInventoryScreenMixin(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(at = @At("HEAD"), method = "drawStatusEffects")
    private void beforeRenderEffects(MatrixStack matrices, CallbackInfo ci) {
        final Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof CreativeInventoryScreen) {
            if (FilterBuilder.isTabHasFilters(((CreativeInventoryScreen) screen).getSelectedTab())) {
                this.x = this.x - 55; // move effect labels to avoid overlap with filters
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "drawStatusEffects")
    private void afterRenderEffects(MatrixStack matrices, CallbackInfo ci) {
        final Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof CreativeInventoryScreen) {
            if (FilterBuilder.isTabHasFilters(((CreativeInventoryScreen) screen).getSelectedTab())) {
                this.x = this.x + 55;
            }
        }
    }
}
