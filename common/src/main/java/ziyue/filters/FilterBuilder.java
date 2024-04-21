package ziyue.filters;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @see Filter
 * @since 1.0.0
 */

public class FilterBuilder
{
    /**
     * The core of Filters API - the hashmap which stores all filters.
     *
     * @since 1.0.0
     */
    public static final HashMap<Integer, FilterList> FILTERS = new HashMap<>();

    /**
     * Register a filter for specific creative mode tab.
     *
     * @param creativeModeTab specific creative mode tab
     * @param filterName      name of the filter
     * @param filterIcon      icon for filter
     * @return Filter instance
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static Filter registerFilter(CreativeModeTab creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab.getId(), FilterList.empty());
        filterList.add(filter);
        FILTERS.put(creativeModeTab.getId(), filterList);
        return filter;
    }

    /**
     * Register a filter for uncategorized items in the specific creative mode tab.
     * "Uncategorized items filter" is for that not of current mod. Category the block/item when registering block/item is recommended.
     *
     * @param creativeModeTab specific creative mode tab
     * @param filterName      name of the filter
     * @param filterIcon      icon for filter
     * @return Filter instance
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static Filter registerUncategorizedItemsFilter(CreativeModeTab creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab.getId(), FilterList.empty());
        filterList.uncategorizedItems = filter;
        FILTERS.put(creativeModeTab.getId(), filterList);
        return filter;
    }

    /**
     * @author ZiYueCommentary
     * @see #setReservedButton(CreativeModeTab, Component, Button.OnPress, ResourceLocation, int, int)
     * @since 1.0.0
     */
    public static void setReservedButton(CreativeModeTab creativeModeTab, Component tooltip, Button.OnPress onPress) {
        FilterBuilder.setReservedButton(creativeModeTab, tooltip, onPress, FiltersApi.ICONS, 64, 0);
    }

    /**
     * Configure the third button on the left.
     *
     * @param creativeModeTab specific creative mode tab
     * @param tooltip         text when hovering the button
     * @param onPress         function when clicking the button, set this as null to make the button invisible
     * @param icon            the icon of the button
     * @param iconU           iconU of the icon
     * @param iconV           iconV of the icon
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static void setReservedButton(CreativeModeTab creativeModeTab, Component tooltip, Button.OnPress onPress, ResourceLocation icon, int iconU, int iconV) {
        FilterList filters = FilterBuilder.FILTERS.get(creativeModeTab.getId());
        filters.btnReservedTooltip = tooltip;
        filters.btnReservedOnPress = onPress;
        filters.btnReservedIcon = icon;
        filters.btnReservedIconU = iconU;
        filters.btnReservedIconV = iconV;
    }

    /**
     * @param creativeModeTabId the id of the creative mode tab
     * @param visible           whether the filters are enabled
     * @author ZiYueCommentary
     * @see #filtersVisibility(CreativeModeTab, boolean)
     * @since 1.0.0
     * @deprecated
     */
    @Deprecated
    public static void filtersVisibility(int creativeModeTabId, boolean visible) {
        if (FilterBuilder.FILTERS.containsKey(creativeModeTabId))
            FilterBuilder.FILTERS.get(creativeModeTabId).enabled = visible;
    }

    /**
     * Disable or enable the filters of the tab.
     *
     * @param creativeModeTab the creative mode tab
     * @param visible         whether the filters are enabled
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static void filtersVisibility(CreativeModeTab creativeModeTab, boolean visible) {
        FilterBuilder.filtersVisibility(creativeModeTab.getId(), visible);
    }

    /**
     * @param creativeModeTabId the id of the creative mode tab
     * @param item              the item
     * @author ZiYueCommentary
     * @see #isItemCategorized(CreativeModeTab, Item)
     * @since 1.0.0
     * @deprecated
     */
    @Deprecated
    public static boolean isItemCategorized(int creativeModeTabId, Item item) {
        for (Filter filter : FilterBuilder.FILTERS.get(creativeModeTabId)) {
            if (filter.items.contains(item)) return true;
        }
        return false;
    }

    /**
     * Check whether the item is categorized in specific creative mode tab.
     *
     * @param creativeModeTab the creative mode tab
     * @param item            the item
     * @return a boolean value, true is categorized, vise versa
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static boolean isItemCategorized(CreativeModeTab creativeModeTab, Item item) {
        return isItemCategorized(creativeModeTab.getId(), item);
    }

    /**
     * @param creativeModeTabId the id of the creative mode tab
     * @author ZiYueCommentary
     * @see #isTabHasFilters(CreativeModeTab)
     * @since 1.0.0
     * @deprecated
     */
    @Deprecated
    public static boolean isTabHasFilters(int creativeModeTabId) {
        return (FilterBuilder.FILTERS.containsKey(creativeModeTabId) && !FilterBuilder.FILTERS.get(creativeModeTabId).isEmpty() && (FilterBuilder.FILTERS.get(creativeModeTabId).enabled));
    }

    /**
     * Check whether the creative mode tab has filters or the filters are enabled.
     *
     * @param creativeModeTab the creative mode tab
     * @return a boolean value, true is available, vise versa
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static boolean isTabHasFilters(CreativeModeTab creativeModeTab) {
        return isTabHasFilters(creativeModeTab.getId());
    }
}