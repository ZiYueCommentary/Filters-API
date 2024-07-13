package ziyue.filters;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.atomic.AtomicInteger;

@Mod(FiltersApi.MOD_ID)
public class FiltersApiForge
{
    public static boolean itemsCategorized = false;

    public FiltersApiForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        FilterBuilder.registerFilter(CreativeModeTab.TAB_FOOD, new TranslatableComponent("1"), () -> new ItemStack(Items.ACACIA_BOAT));
        FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTab.TAB_FOOD);
    }

    public void doClientStuff(final FMLClientSetupEvent event) {
        FiltersApi.LOGGER.info("Filters API initialized!");
    }

    @SubscribeEvent
    public void postScreenInit(final GuiScreenEvent.DrawScreenEvent.Post event) {
        if (itemsCategorized) return;

        AtomicInteger uncategorizedItems = new AtomicInteger(0);
        AtomicInteger uncategorizedFilters = new AtomicInteger(0);

        // collecting uncategorized items
        Registry.ITEM.forEach(item -> {
            CreativeModeTab itemGroup = item.getItemCategory();
            if (itemGroup != null) {
                if (FilterBuilder.isTabHasFilters(itemGroup)) {
                    FilterList filters = FilterBuilder.FILTERS.get(itemGroup.getId());
                    if ((filters.uncategorizedItems != null) && (!FilterBuilder.isItemCategorized(itemGroup, item))) {
                        filters.uncategorizedItems.addItems(item);
                        uncategorizedItems.getAndIncrement();
                    }
                }
            }
        });

        // adding uncategorized items filter to filter list
        FilterBuilder.FILTERS.forEach((tabId, filterList) -> {
            if ((filterList.uncategorizedItems != null) && (!filterList.uncategorizedItems.items.isEmpty())) {
                filterList.add(filterList.uncategorizedItems);
                uncategorizedFilters.getAndIncrement();
            }
        });

        FiltersApi.LOGGER.info("Found {} uncategorized items, added {} filters to the filter lists", uncategorizedItems.get(), uncategorizedFilters.get());

        itemsCategorized = true;
    }
}
