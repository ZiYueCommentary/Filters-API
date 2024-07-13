package ziyue.filters.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import java.util.Set;

import static ziyue.filters.FiltersApi.ICONS;

/**
 * Render filters.
 *
 * @author ZiYueCommentary
 * @see Filter
 * @see EffectRenderingInventoryScreenMixin
 * @since 1.0.0
 */

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu>
{
    @Shadow @Final private Set<TagKey<Item>> visibleTags;

    @Shadow private static int selectedTab;

    @Shadow private float scrollOffs;

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu p_98701_, Inventory p_98702_, Component p_98703_) {
        super(p_98701_, p_98702_, p_98703_);
    }

    @Inject(at = @At("HEAD"), method = "render")
    protected void beforeRender(PoseStack p_98577_, int p_98578_, int p_98579_, float p_98580_, CallbackInfo ci) {
        FilterBuilder.FILTERS.forEach((map, filter1) -> filtersApi$showButtons(filter1, false));
        FilterBuilder.FILTERS.forEach((map, filter) -> filter.forEach(button -> button.visible = false));

        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;
        filtersApi$updateItems();
        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        filtersApi$showButtons(filter, true);
        for (int o = 0; o < filter.size(); o++) {
            if ((o >= filter.filterIndex) && (o < filter.filterIndex + 4)) {
                filter.get(o).x = this.leftPos - 28;
                filter.get(o).y = this.topPos + 29 * (o - filter.filterIndex) + 10;
                filter.get(o).visible = true;
            } else filter.get(o).visible = false;
        }
        filter.btnScrollUp.active = filter.filterIndex > 0;
        filter.btnScrollDown.active = filter.filterIndex + 4 < filter.size();
    }

    @Inject(at = @At("TAIL"), method = "render")
    protected void afterRender(PoseStack matrices, int mouseX, int mouseY, float p_98580_, CallbackInfo ci) {
        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;

        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        if (filter.btnScrollUp.isHovered())
            this.renderTooltip(matrices, filter.btnScrollUp.getMessage(), mouseX, mouseY);
        if (filter.btnScrollDown.isHovered())
            this.renderTooltip(matrices, filter.btnScrollDown.getMessage(), mouseX, mouseY);
        if (filter.btnEnableAll.isHovered())
            this.renderTooltip(matrices, filter.btnEnableAll.getMessage(), mouseX, mouseY);
        if (filter.btnDisableAll.isHovered())
            this.renderTooltip(matrices, filter.btnDisableAll.getMessage(), mouseX, mouseY);
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
            filter.btnScrollUp = new IconButton(this.leftPos - 22, this.topPos - 12, new TranslatableComponent("button.filters.scroll_up").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex--, ICONS, 0, 0);
            filter.btnScrollDown = new IconButton(this.leftPos - 22, this.topPos + 127, new TranslatableComponent("button.filters.scroll_down").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex++, ICONS, 16, 0);
            filter.btnEnableAll = new IconButton(this.leftPos - 50, this.topPos + 10, new TranslatableComponent("button.filters.enable_all").withStyle(ChatFormatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = true), ICONS, 32, 0);
            filter.btnDisableAll = new IconButton(this.leftPos - 50, this.topPos + 32, new TranslatableComponent("button.filters.disable_all").withStyle(ChatFormatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = false), ICONS, 48, 0);
            if (filter.btnReservedOnPress != null) {
                filter.btnReserved = new IconButton(this.leftPos - 50, this.topPos + 54, filter.btnReservedTooltip, filter.btnReservedOnPress, filter.btnReservedIcon, filter.btnReservedIconU, filter.btnReservedIconV);
                this.addRenderableWidget(filter.btnReserved);
            }
            this.addRenderableWidget(filter.btnScrollUp);
            this.addRenderableWidget(filter.btnScrollDown);
            this.addRenderableWidget(filter.btnEnableAll);
            this.addRenderableWidget(filter.btnDisableAll);

            filter.forEach(this::addRenderableWidget);
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
        visibleTags.clear();
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