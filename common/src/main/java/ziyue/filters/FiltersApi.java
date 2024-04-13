package ziyue.filters;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @since 1.0.0
 * @see Filter
 */

@Environment(EnvType.CLIENT)
public class FiltersApi
{
    public static final String MOD_ID = "filters";
    public static final Logger LOGGER = LogManager.getLogger("Filters API");
    public static final ResourceLocation ICONS = new ResourceLocation(FiltersApi.MOD_ID, "textures/gui/filters.png");

    /**
     * The hashmap which stores all filters.
     */
    public static final HashMap<Integer, FilterList> FILTERS = new HashMap<>();

    protected static final ResourceLocation TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    /**
     * @see #setOptionsButton(CreativeModeTab, Component, Button.OnPress, ResourceLocation, int, int)
     * @since 1.0.0
     */
    public static void setOptionsButton(CreativeModeTab tab, Component tooltip, Button.OnPress onPress) {
        FiltersApi.setOptionsButton(tab, tooltip, onPress, ICONS, 64, 0);
    }

    /**
     * Configuring the third button on the left.
     *
     * @param tab     specific creative mode tab
     * @param tooltip text when hovering the button
     * @param onPress function when clicking the button, set this as null to make the button invisible
     * @param icon    the icon of the button
     * @param iconU   iconU
     * @param iconV   iconV
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static void setOptionsButton(CreativeModeTab tab, Component tooltip, Button.OnPress onPress, ResourceLocation icon, int iconU, int iconV) {
        FilterList filters = FiltersApi.FILTERS.get(tab.getId());
        filters.btnOptionsTooltip = tooltip;
        filters.btnOptionsOnPress = onPress;
        filters.btnOptionsIcon = icon;
        filters.btnOptionsIconU = iconU;
        filters.btnOptionsIconV = iconV;
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
     * Check whether the item is categorized in specific creative mode tab.
     *
     * @param creativeModeTabId the id of the creative mode tab
     * @param item              the item
     * @return a boolean value, true is categorized, vise versa
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static boolean isItemCategorized(int creativeModeTabId, Item item) {
        for (Filter filter : FiltersApi.FILTERS.get(creativeModeTabId)) {
            if (filter.items.contains(item)) return true;
        }
        return false;
    }

    /**
     * @see #isItemCategorized(int, Item)
     * @since 1.0.0
     */
    public static boolean isItemCategorized(CreativeModeTab tab, Item item) {
        return isItemCategorized(tab.getId(), item);
    }

    /**
     * Check whether the creative mode tab has filters or the filters are enabled.
     *
     * @param creativeModeTabId the id of the creative mode tab
     * @return a boolean value, true is available, vise versa
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static boolean isTabHasFilters(int creativeModeTabId) {
        return (FiltersApi.FILTERS.containsKey(creativeModeTabId) && !FiltersApi.FILTERS.get(creativeModeTabId).isEmpty() && (FiltersApi.FILTERS.get(creativeModeTabId).enabled));
    }

    /**
     * @see #isTabHasFilters(int)
     * @since 1.0.0
     */
    public static boolean isTabHasFilters(CreativeModeTab tab) {
        return isTabHasFilters(tab.getId());
    }
}