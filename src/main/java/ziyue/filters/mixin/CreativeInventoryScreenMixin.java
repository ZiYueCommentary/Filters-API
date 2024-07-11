package ziyue.filters.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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
import ziyue.filters.gui.IconButton;

import java.util.Comparator;
import java.util.Map;

import static ziyue.filters.FiltersApi.ICONS;

/**
 * Render filters.
 *
 * @author ZiYueCommentary
 * @see Filter
 * @see AbstractInventoryScreenMixin
 * @since 1.0.0
 */

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler>
{
    @Shadow private static int selectedTab;

    @Shadow @Final private Map<Identifier, Tag<Item>> searchResultTags;

    @Shadow private float scrollPosition;

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
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
                filter.get(o).x = this.x - 28;
                filter.get(o).y = this.y + 29 * (o - filter.filterIndex) + 10;
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
            filter.btnScrollUp = new IconButton(this.x - 22, this.y - 12, new TranslatableText("button.filters.scroll_up").formatted(Formatting.WHITE), button -> filter.filterIndex--, ICONS, 0, 0);
            filter.btnScrollDown = new IconButton(this.x - 22, this.y + 127, new TranslatableText("button.filters.scroll_down").formatted(Formatting.WHITE), button -> filter.filterIndex++, ICONS, 16, 0);
            filter.btnEnableAll = new IconButton(this.x - 50, this.y + 10, new TranslatableText("button.filters.enable_all").formatted(Formatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = true), ICONS, 32, 0);
            filter.btnDisableAll = new IconButton(this.x - 50, this.y + 32, new TranslatableText("button.filters.disable_all").formatted(Formatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = false), ICONS, 48, 0);
            if (filter.btnReservedOnPress != null) {
                filter.btnReserved = new IconButton(this.x - 50, this.y + 54, filter.btnReservedTooltip, filter.btnReservedOnPress, filter.btnReservedIcon, filter.btnReservedIconU, filter.btnReservedIconV);
                this.addButton(filter.btnReserved);
            }
            this.addButton(filter.btnScrollUp);
            this.addButton(filter.btnScrollDown);
            this.addButton(filter.btnEnableAll);
            this.addButton(filter.btnDisableAll);

            filter.forEach(this::addButton);
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