package ziyue.filters.hotswap;

import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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
    public static final HashMap<Optional<ItemGroup>, PendingFilterList> PENDING_FILTERS = new HashMap<>();

    /**
     * Initialize modern filters.
     * This will clear corresponding tab's filter, so if the modern and legacy is used at the same time, the legacy one will be discarded.
     *
     * @author ZiYueCommentary
     * @since 1.1.0
     */
    public static void getReady() {
        for (Map.Entry<Optional<ItemGroup>, PendingFilterList> pendingTab : HotswapFiltersConfig.PENDING_FILTERS.entrySet()) {
            final String tabId = pendingTab.getKey().get().getName();
            if (!pendingTab.getKey().isPresent()) {
                LOGGER.warn("Tab {} not found!", tabId);
                continue;
            }
            final ItemGroup tab = pendingTab.getKey().get();
            final FilterList filters = FilterBuilder.FILTERS.getOrDefault(tab.getIndex(), FilterList.empty());
            filters.clear(); // if modern config has been found, then ignore the legacy one.
            for (PendingFilter pendingFilter : pendingTab.getValue()) {
                List<Item> buildItems = new ArrayList<>();
                for (Identifier itemLocation : pendingFilter.items()) {
                    final Optional<Item> item = Registry.ITEM.getOrEmpty(itemLocation);
                    if (!item.isPresent()) {
                        LOGGER.warn("Item {} not found at filter {}, tab {}", itemLocation, pendingFilter.id(), tabId);
                        continue;
                    }
                    buildItems.add(item.get());
                }
                filters.add(new Filter(pendingFilter.title(), () -> {
                    final Optional<Item> icon = Registry.ITEM.getOrEmpty(pendingFilter.icon());
                    return icon.map(ItemStack::new).orElseGet(() -> new ItemStack(Items.BARRIER));
                }, buildItems));
            }
            if (pendingTab.getValue().uncategorized != null) {
                filters.uncategorizedItems = new Filter(pendingTab.getValue().uncategorized.title(), () -> {
                    final Optional<Item> icon = Registry.ITEM.getOrEmpty(pendingTab.getValue().uncategorized.icon());
                    return icon.map(ItemStack::new).orElseGet(() -> new ItemStack(Items.BARRIER));
                }, new ArrayList<>());
            }
            FilterBuilder.FILTERS.put(tab.getIndex(), filters);
            LOGGER.info("Built {} filters for tab {}", filters.size(), tabId);
        }
    }
}
