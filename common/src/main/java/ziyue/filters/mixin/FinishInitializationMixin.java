package ziyue.filters.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.filters.Filter;
import ziyue.filters.FilterList;
import ziyue.filters.FiltersAPI;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Collect uncategorized items.
 *
 * @author ZiYueCommentary
 * @see RenderSystem
 * @since 1.0.0
 */

@Mixin(RenderSystem.class)
public abstract class FinishInitializationMixin
{
    @Inject(at = @At("TAIL"), method = "finishInitialization")
    private static void afterFinishInitialization(CallbackInfo callbackInfo) {
        AtomicInteger uncategorizedItems = new AtomicInteger(0);
        AtomicInteger uncategorizedFilters = new AtomicInteger(0);

        // collecting uncategorized items
        Registry.ITEM.forEach(item -> {
            CreativeModeTab itemCategory = item.getItemCategory();
            if (itemCategory != null) {
                if (Filter.isTabHasFilters(itemCategory)) {
                    FilterList filters = Filter.FILTERS.get(itemCategory.getId());
                    if ((filters.uncategorizedItems != null) && (!Filter.isItemCategorized(itemCategory, item))) {
                        filters.uncategorizedItems.addItems(item);
                        uncategorizedItems.getAndIncrement();
                    }
                }
            }
        });

        // adding uncategorized items filter to filter list
        Filter.FILTERS.forEach((tabId, filterList) -> {
            if ((filterList.uncategorizedItems != null) && (!filterList.uncategorizedItems.items.isEmpty())) {
                filterList.add(filterList.uncategorizedItems);
                uncategorizedFilters.getAndIncrement();
            }
        });

        FiltersAPI.LOGGER.info("Found {} uncategorized items, added {} filters to the filter lists", uncategorizedItems.get(), uncategorizedFilters.get());
    }
}
