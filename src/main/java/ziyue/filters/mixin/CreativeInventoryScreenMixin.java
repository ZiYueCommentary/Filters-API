package ziyue.filters.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.filters.Filter;
import ziyue.filters.FilterBuilder;
import ziyue.filters.FilterList;
import ziyue.filters.FiltersApi;
import ziyue.filters.gui.IconButton;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static ziyue.filters.FiltersApi.ICONS;

/**
 * Render filters.
 *
 * @author ZiYueCommentary
 * @see Filter
 * @since 1.0.0
 */

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler>
{
    @Shadow private static ItemGroup selectedTab;

    @Shadow @Final private Set<TagKey<Item>> searchResultTags;

    @Shadow private float scrollPosition;

    @Unique
    private static boolean filtersAPI$itemsCategorized = false;

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void afterInit(PlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled, CallbackInfo ci) {
        if (!filtersAPI$itemsCategorized) {
            AtomicInteger uncategorizedItems = new AtomicInteger(0);
            AtomicInteger uncategorizedFilters = new AtomicInteger(0);

            // collecting uncategorized items
            Registries.ITEM.iterator().forEachRemaining(item -> ItemGroups.getGroups().forEach(tab -> {
                if (FilterBuilder.isTabHasFilters(tab)) {
                    FilterList filters = FilterBuilder.FILTERS.get(tab);
                    if (filters.uncategorizedItems != null) {
                        List<Item> items = tab.getDisplayStacks().stream().map(ItemStack::getItem).toList();
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
            filtersAPI$itemsCategorized = true;
        }
    }

    @Inject(at = @At("HEAD"), method = "render")
    protected void beforeRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        FilterBuilder.FILTERS.forEach((map, filter1) -> filtersApi$showButtons(filter1, false));
        FilterBuilder.FILTERS.forEach((map, filter) -> filter.forEach(button -> button.visible = false));

        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;
        filtersApi$updateItems();
        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        filtersApi$showButtons(filter, true);
        for (int o = 0; o < filter.size(); o++) {
            if ((o >= filter.filterIndex) && (o < filter.filterIndex + 4)) {
                filter.get(o).setX(this.x - 28);
                filter.get(o).setY(this.y + 27 * (o - filter.filterIndex) + 10);
                filter.get(o).visible = true;
            } else filter.get(o).visible = false;
        }
        filter.btnScrollUp.active = filter.filterIndex > 0;
        filter.btnScrollDown.active = filter.filterIndex + 4 < filter.size();
    }

    @Inject(at = @At("TAIL"), method = "render")
    protected void afterRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;

        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        if (filter.btnScrollUp.isHovered()) this.renderTooltip(matrices, filter.btnScrollUp.getMessage(), mouseX, mouseY);
        if (filter.btnScrollDown.isHovered()) this.renderTooltip(matrices, filter.btnScrollDown.getMessage(), mouseX, mouseY);
        if (filter.btnEnableAll.isHovered()) this.renderTooltip(matrices, filter.btnEnableAll.getMessage(), mouseX, mouseY);
        if (filter.btnDisableAll.isHovered()) this.renderTooltip(matrices, filter.btnDisableAll.getMessage(), mouseX, mouseY);
        if (filter.btnReserved != null && filter.btnReserved.isHovered() && filter.btnReservedTooltip != null) {
            this.renderTooltip(matrices, filter.btnReservedTooltip, mouseX, mouseY);
        }

        filter.forEach(filter1 -> {
            if (filter1.isHovered()) this.renderTooltip(matrices, filter1.getMessage(), mouseX, mouseY);
        });
    }

    @Inject(at = @At("TAIL"), method = "init")
    protected void afterInit(CallbackInfo ci) {
        FilterBuilder.FILTERS.forEach((map, filter) -> {
            filter.btnScrollUp = new IconButton(this.x - 22, this.y - 12, Text.translatable("button.filters.scroll_up").formatted(Formatting.WHITE), button -> filter.filterIndex--, ICONS, 0, 0);
            filter.btnScrollDown = new IconButton(this.x - 22, this.y + 119, Text.translatable("button.filters.scroll_down").formatted(Formatting.WHITE), button -> filter.filterIndex++, ICONS, 16, 0);
            filter.btnEnableAll = new IconButton(this.x - 50, this.y + 10, Text.translatable("button.filters.enable_all").formatted(Formatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = true), ICONS, 32, 0);
            filter.btnDisableAll = new IconButton(this.x - 50, this.y + 32, Text.translatable("button.filters.disable_all").formatted(Formatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = false), ICONS, 48, 0);
            if (filter.btnReservedOnPress != null) {
                filter.btnReserved = new IconButton(this.x - 50, this.y + 54, filter.btnReservedTooltip, filter.btnReservedOnPress, filter.btnReservedIcon, filter.btnReservedIconU, filter.btnReservedIconV);
                this.addDrawableChild(filter.btnReserved);
            }
            this.addDrawableChild(filter.btnScrollUp);
            this.addDrawableChild(filter.btnScrollDown);
            this.addDrawableChild(filter.btnEnableAll);
            this.addDrawableChild(filter.btnDisableAll);

            filter.forEach(this::addDrawableChild);
        });
    }

    @Unique
    protected void filtersApi$showButtons(FilterList list, boolean visible) {
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
    protected void filtersApi$updateItems() {
        searchResultTags.clear();
        this.handler.itemList.clear(); // clear the tab
        FilterBuilder.FILTERS.get(selectedTab).forEach(
                filter -> {
                    if (filter.enabled) {
                        filter.items.forEach(item -> this.handler.itemList.add(new ItemStack(item))); // add items
                    }
                }
        );
        this.handler.itemList.sort(Comparator.comparingInt(o -> Item.getRawId(o.getItem()))); // sort items
        float previousOffset = this.scrollPosition;
        this.handler.scrollItems(0.0f); // refresh (maybe?)
        this.scrollPosition = previousOffset;
        this.handler.scrollItems(previousOffset);
    }
}