package ziyue.filters.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.filters.*;
import ziyue.filters.gui.IconButton;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static ziyue.filters.FiltersApi.*;

/**
 * Render filters.
 *
 * @author ZiYueCommentary
 * @see Filter
 * @since 1.0.0
 */

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu>
{
    @Shadow
    private static CreativeModeTab selectedTab;
    @Shadow
    @Final
    private Set<TagKey<Item>> visibleTags;
    @Shadow
    private float scrollOffs;
    @Unique
    private static boolean filters$itemsCategorized = false;

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    protected void afterConstruct(LocalPlayer player, FeatureFlagSet enabledFeatures, boolean displayOperatorCreativeTab, CallbackInfo ci) {
        if (!filters$itemsCategorized) {
            AtomicInteger uncategorizedItems = new AtomicInteger(0);
            AtomicInteger uncategorizedFilters = new AtomicInteger(0);

            // collecting uncategorized items
            BuiltInRegistries.ITEM.iterator().forEachRemaining(item -> CreativeModeTabs.allTabs().forEach(tab -> {
                if (FilterBuilder.isTabHasFilters(tab)) {
                    FilterList filters = FilterBuilder.FILTERS.get(tab);
                    if (filters.uncategorizedItems != null) {
                        List<Item> items = tab.getDisplayItems().stream().map(ItemStack::getItem).toList();
                        if (items.contains(item)) {
                            if (!FilterBuilder.isItemCategorized(tab, item)) {
                                filters.uncategorizedItems.addItems(item);
                                uncategorizedItems.getAndIncrement();
                            }
                        }
                    }
                }
            }));

            // adding uncategorized items filter to filter list
            FilterBuilder.FILTERS.forEach((tabId, filterList) -> {
                if ((filterList.uncategorizedItems != null) && (!filterList.uncategorizedItems.items.isEmpty())) {
                    filterList.add(filterList.uncategorizedItems);
                    uncategorizedFilters.getAndIncrement();
                }
            });

            FiltersApi.LOGGER.info("Found {} uncategorized items, added {} filters to the filter lists", uncategorizedItems.get(), uncategorizedFilters.get());

            filters$itemsCategorized = true;
        }
    }

    @Inject(at = @At("HEAD"), method = "render")
    protected void beforeRender(GuiGraphics p_283000_, int p_281317_, int p_282770_, float p_281295_, CallbackInfo ci) {
        FilterBuilder.FILTERS.forEach((map, filter1) -> filtersApi$showButtons(filter1, false));
        FilterBuilder.FILTERS.forEach((map, filter) -> filter.forEach(button -> button.visible = false));

        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;
        filtersApi$updateItems();
        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        filtersApi$showButtons(filter, true);
        for (int o = 0; o < filter.size(); o++) {
            if ((o >= filter.filterIndex) && (o < filter.filterIndex + 4)) {
                filter.get(o).setX(this.leftPos - 28);
                filter.get(o).setY(this.topPos + 27 * (o - filter.filterIndex) + 10);
                filter.get(o).visible = true;
            } else filter.get(o).visible = false;
        }
        filter.btnScrollUp.active = filter.filterIndex > 0;
        filter.btnScrollDown.active = filter.filterIndex + 4 < filter.size();
    }

    @Inject(at = @At("TAIL"), method = "render")
    protected void afterRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float p_281295_, CallbackInfo ci) {
        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;

        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        if (filter.btnScrollUp.isHovered())
            guiGraphics.renderTooltip(this.font, filter.btnScrollUp.getMessage(), mouseX, mouseY);
        if (filter.btnScrollDown.isHovered())
            guiGraphics.renderTooltip(this.font, filter.btnScrollDown.getMessage(), mouseX, mouseY);
        if (filter.btnEnableAll.isHovered())
            guiGraphics.renderTooltip(this.font, filter.btnEnableAll.getMessage(), mouseX, mouseY);
        if (filter.btnDisableAll.isHovered())
            guiGraphics.renderTooltip(this.font, filter.btnDisableAll.getMessage(), mouseX, mouseY);
        if (filter.btnReserved != null && filter.btnReserved.isHovered() && filter.btnReservedTooltip != null) {
            guiGraphics.renderTooltip(this.font, filter.btnReservedTooltip, mouseX, mouseY);
        }

        filter.forEach(filter1 -> {
            if (filter1.isHovered()) guiGraphics.renderTooltip(this.font, filter1.getMessage(), mouseX, mouseY);
        });
    }

    @Inject(at = @At("TAIL"), method = "init")
    protected void afterInit(CallbackInfo ci) {
        FilterBuilder.FILTERS.forEach((map, filter) -> {
            filter.btnScrollUp = new IconButton(this.leftPos - 22, this.topPos - 12, Component.translatable("button.filters.scroll_up").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex--, ICON_UP);
            filter.btnScrollDown = new IconButton(this.leftPos - 22, this.topPos + 119, Component.translatable("button.filters.scroll_down").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex++, ICON_DOWN);
            filter.btnEnableAll = new IconButton(this.leftPos - 50, this.topPos + 10, Component.translatable("button.filters.enable_all").withStyle(ChatFormatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = true), ICON_CHECK);
            filter.btnDisableAll = new IconButton(this.leftPos - 50, this.topPos + 32, Component.translatable("button.filters.disable_all").withStyle(ChatFormatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = false), ICON_CROSS);
            if (filter.btnReservedOnPress != null) {
                filter.btnReserved = new IconButton(this.leftPos - 50, this.topPos + 54, filter.btnReservedTooltip, filter.btnReservedOnPress, filter.btnReservedIcon);
                this.addRenderableWidget(filter.btnReserved);
            }
            this.addRenderableWidget(filter.btnScrollUp);
            this.addRenderableWidget(filter.btnScrollDown);
            this.addRenderableWidget(filter.btnEnableAll);
            this.addRenderableWidget(filter.btnDisableAll);

            filter.forEach(filter1 -> this.addRenderableWidget(filter1)); // don't replace it or the game will crash
        });
    }

    @Unique
    public void filtersApi$showButtons(FilterList list, boolean visible) {
        if (list.size() > 4) {
            list.btnScrollUp.visible = visible;
            list.btnScrollDown.visible = visible;
        } else {
            list.btnScrollUp.visible = false;
            list.btnScrollDown.visible = false;
        }
        if (list.btnReserved != null) {
            if (list.btnReservedOnPress != null) {
                list.btnReserved.visible = visible;
            } else {
                list.btnReserved.visible = false;
            }
        }
        list.btnEnableAll.visible = visible;
        list.btnDisableAll.visible = visible;
    }

    @Unique
    public void filtersApi$updateItems() {
        this.visibleTags.clear();
        this.menu.items.clear(); // clear the tab
        FilterBuilder.FILTERS.get(selectedTab).forEach(
                filter -> {
                    if (filter.enabled) {
                        filter.items.forEach(item -> this.menu.items.add(new ItemStack(item))); // add items
                    }
                }
        );
        this.menu.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem()))); // sort items
        float previousOffset = this.scrollOffs;
        this.menu.scrollTo(0.0f); // refresh (maybe?)
        this.scrollOffs = previousOffset;
        this.menu.scrollTo(previousOffset);
    }
}