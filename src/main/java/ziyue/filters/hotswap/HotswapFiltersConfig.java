package ziyue.filters.hotswap;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ziyue.filters.Filter;
import ziyue.filters.FilterBuilder;
import ziyue.filters.FilterList;

import java.util.*;

import static ziyue.filters.FiltersApi.LOGGER;

/**
 * Reading configs of filters when resources are reloaded. This belongs to the modern filter implementation.
 *
 * @author ZiYueCommentary
 * @since 1.1.0
 */
public class HotswapFiltersConfig
{
    public static final HashMap<Optional<CreativeModeTab>, PendingFilterList> PENDING_FILTERS = new HashMap<>();

    /**
     * Initialize modern filters.
     * This will clear corresponding tab's filter, so if the modern and legacy is used at the same time, the legacy one will be discarded.
     *
     * @author ZiYueCommentary
     * @since 1.1.0
     */
    public static void getReady() {
        for (Map.Entry<Optional<CreativeModeTab>, PendingFilterList> pendingTab : HotswapFiltersConfig.PENDING_FILTERS.entrySet()) {
            final String tabId = pendingTab.getKey().get().toString();
            if (pendingTab.getKey().isEmpty()) {
                LOGGER.warn("Tab {} not found!", tabId);
                continue;
            }
            final CreativeModeTab tab = pendingTab.getKey().get();
            final FilterList filters = FilterBuilder.FILTERS.getOrDefault(tab, FilterList.empty());
            filters.clear(); // if modern config has been found, then ignore the legacy one.
            for (PendingFilter pendingFilter : pendingTab.getValue()) {
                List<Item> buildItems = new ArrayList<>();
                for (ResourceLocation itemLocation : pendingFilter.items()) {
                    final var item = BuiltInRegistries.ITEM.getOptional(itemLocation);
                    if (item.isEmpty()) {
                        LOGGER.warn("Item {} not found at filter {}, tab {}", itemLocation, pendingFilter.id(), tabId);
                        continue;
                    }
                    buildItems.add(item.get());
                }
                filters.add(new Filter(pendingFilter.title(), () -> {
                    final var icon = BuiltInRegistries.ITEM.getOptional(pendingFilter.icon());
                    return icon.map(ItemStack::new).orElseGet(() -> new ItemStack(Items.BARRIER));
                }, buildItems));
            }
            if (pendingTab.getValue().uncategorized != null) {
                filters.uncategorizedItems = new Filter(pendingTab.getValue().uncategorized.title(), () -> {
                    final var icon = BuiltInRegistries.ITEM.getOptional(pendingTab.getValue().uncategorized.icon());
                    return icon.map(ItemStack::new).orElseGet(() -> new ItemStack(Items.BARRIER));
                }, new ArrayList<>());
            }
            FilterBuilder.FILTERS.put(tab, filters);
            LOGGER.info("Built {} filters for tab {}", filters.size(), tabId);
        }
    }
}
