package ziyue.filters;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import static ziyue.filters.FiltersApi.ICON_WRENCH;

/**
 * @author ZiYueCommentary
 * @see Filter
 * @since 1.0.0
 */

public class FilterBuilder
{
    /**
     * The core of Filters API - the hashmap which stores all filters.
     * This field stores configs that are checked as valid, and ready to use.
     *
     * @since 1.0.0
     */
    public static final HashMap<ItemGroup, FilterList> FILTERS = new HashMap<>();

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerFilter(ItemGroup, Text, Supplier)
     * @since 1.0.0
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerFilter(RegistryKey<ItemGroup> creativeModeTab, Text filterName, Supplier<ItemStack> filterIcon) {
        return FilterBuilder.registerFilter(Registries.ITEM_GROUP.get(creativeModeTab), filterName, filterIcon);
    }

    /**
     * Register a filter for specific creative mode tab.
     *
     * @param creativeModeTab specific creative mode tab
     * @param filterName      name of the filter
     * @param filterIcon      icon for filter
     * @return Filter instance
     * @author ZiYueCommentary
     * @since 1.0.0
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerFilter(ItemGroup creativeModeTab, Text filterName, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.add(filter);
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(ItemGroup)
     * @since 1.0.0
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerUncategorizedItemsFilter(RegistryKey<ItemGroup> creativeModeTab) {
        return FilterBuilder.registerUncategorizedItemsFilter(Registries.ITEM_GROUP.get(creativeModeTab));
    }

    /**
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(ItemGroup, Text, Supplier)
     * @since 1.0.0
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerUncategorizedItemsFilter(ItemGroup creativeModeTab) {
        Filter filter = new Filter(Text.translatable("filter.filters.uncategorized"), () -> new ItemStack(Blocks.BARRIER), new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.uncategorizedItems = filter;
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(ItemGroup, Supplier)
     * @since 1.0.0
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerUncategorizedItemsFilter(RegistryKey<ItemGroup> creativeModeTab, Supplier<ItemStack> filterIcon) {
        return FilterBuilder.registerUncategorizedItemsFilter(Registries.ITEM_GROUP.get(creativeModeTab), filterIcon);
    }

    /**
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(ItemGroup, Text, Supplier)
     * @since 1.0.0
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerUncategorizedItemsFilter(ItemGroup creativeModeTab, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(Text.translatable("filter.filters.uncategorized"), filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.uncategorizedItems = filter;
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #registerUncategorizedItemsFilter(ItemGroup, Text, Supplier)
     * @since 1.0.0
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerUncategorizedItemsFilter(RegistryKey<ItemGroup> creativeModeTab, Text filterName, Supplier<ItemStack> filterIcon) {
        return FilterBuilder.registerUncategorizedItemsFilter(Registries.ITEM_GROUP.get(creativeModeTab), filterName, filterIcon);
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
     * @deprecated This function belongs to the legacy implementation and replaced by modern JSON one.
     * Please check the website documentation.
     */
    @Deprecated(since = "1.1.0")
    public static Filter registerUncategorizedItemsFilter(ItemGroup creativeModeTab, Text filterName, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
        filterList.uncategorizedItems = filter;
        FILTERS.put(creativeModeTab, filterList);
        return filter;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #setReservedButton(ItemGroup, Text, ButtonWidget.PressAction)
     * @since 1.0.0
     */
    public static void setReservedButton(RegistryKey<ItemGroup> creativeModeTab, Text tooltip, ButtonWidget.PressAction onPress) {
        FilterBuilder.setReservedButton(Registries.ITEM_GROUP.get(creativeModeTab), tooltip, onPress, ICON_WRENCH);
    }

    /**
     * @author ZiYueCommentary
     * @see #setReservedButton(ItemGroup, Text, ButtonWidget.PressAction, Identifier)
     * @since 1.0.0
     */
    public static void setReservedButton(ItemGroup creativeModeTab, Text tooltip, ButtonWidget.PressAction onPress) {
        FilterBuilder.setReservedButton(creativeModeTab, tooltip, onPress, ICON_WRENCH);
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #setReservedButton(ItemGroup, Text, ButtonWidget.PressAction, Identifier)
     * @since 1.0.0
     */
    public static void setReservedButton(RegistryKey<ItemGroup> creativeModeTab, Text tooltip, ButtonWidget.PressAction onPress, Identifier icon) {
        FilterBuilder.setReservedButton(Registries.ITEM_GROUP.get(creativeModeTab), tooltip, onPress, icon);
    }

    /**
     * Configure the third button on the left.
     *
     * @param creativeModeTab specific creative mode tab
     * @param tooltip         text when hovering the button
     * @param onPress         function when clicking the button, set this as null to make the button invisible
     * @param icon            the icon of the button
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static void setReservedButton(ItemGroup creativeModeTab, Text tooltip, ButtonWidget.PressAction onPress, Identifier icon) {
        FilterList filters = FilterBuilder.FILTERS.get(creativeModeTab);
        filters.btnReservedTooltip = tooltip;
        filters.btnReservedOnPress = onPress;
        filters.btnReservedIcon = icon;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #filtersVisibility(ItemGroup, boolean)
     * @since 1.0.0
     */
    public static void filtersVisibility(RegistryKey<ItemGroup> creativeModeTab, boolean visible) {
        FilterBuilder.filtersVisibility(Registries.ITEM_GROUP.get(creativeModeTab), visible);
    }

    /**
     * Disable or enable the filters of the tab.
     *
     * @param creativeModeTab the creative mode tab
     * @param visible         whether the filters are enabled
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static void filtersVisibility(ItemGroup creativeModeTab, boolean visible) {
        if (FilterBuilder.FILTERS.containsKey(creativeModeTab))
            FilterBuilder.FILTERS.get(creativeModeTab).enabled = visible;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #isItemCategorized(ItemGroup, Item)
     * @since 1.0.0
     */
    public static boolean isItemCategorized(RegistryKey<ItemGroup> creativeModeTab, Item item) {
        return FilterBuilder.isItemCategorized(Registries.ITEM_GROUP.get(creativeModeTab), item);
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
    public static boolean isItemCategorized(ItemGroup creativeModeTab, Item item) {
        for (Filter filter : FilterBuilder.FILTERS.get(creativeModeTab)) {
            if (filter.items.contains(item)) return true;
        }
        return false;
    }

    /**
     * @param creativeModeTab specific vanilla creative mode tab
     * @author ZiYueCommentary
     * @see #isTabHasFilters(ItemGroup)
     * @since 1.0.0
     */
    public static boolean isTabHasFilters(RegistryKey<ItemGroup> creativeModeTab) {
        return FilterBuilder.isTabHasFilters(Registries.ITEM_GROUP.get(creativeModeTab));
    }

    /**
     * Check whether the creative mode tab has filters or the filters are enabled.
     *
     * @param creativeModeTab the creative mode tab
     * @return a boolean value, true is available, vise versa
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static boolean isTabHasFilters(ItemGroup creativeModeTab) {
        return (FilterBuilder.FILTERS.containsKey(creativeModeTab) && !FilterBuilder.FILTERS.get(creativeModeTab).isEmpty() && (FilterBuilder.FILTERS.get(creativeModeTab).enabled));
    }
}