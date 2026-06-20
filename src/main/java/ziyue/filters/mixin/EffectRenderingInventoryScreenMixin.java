package ziyue.filters.mixin;

import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Mixin;
import ziyue.filters.Filter;

/**
 * @author ZiYueCommentary
 * @see Filter
 * @see CreativeModeInventoryScreenMixin
 * @since 1.0.0
 */

// I don't know why, but seems the game will crash without this mixin

@Mixin(EffectsInInventory.class)
public abstract class EffectRenderingInventoryScreenMixin
{
}
