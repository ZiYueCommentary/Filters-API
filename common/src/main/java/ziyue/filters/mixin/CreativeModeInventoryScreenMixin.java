package ziyue.filters.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
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
import ziyue.filters.FilterBuilder;
import ziyue.filters.gui.IconButton;
import ziyue.filters.Filter;
import ziyue.filters.FilterList;

import java.util.Comparator;
import java.util.Map;
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
    @Shadow private static int selectedTab;

    @Shadow @Final private Set<TagKey<Item>> visibleTags;

    @Shadow private float scrollOffs;

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(at = @At("HEAD"), method = "render")
    protected void beforeRender(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        FilterBuilder.FILTERS.forEach((map, filter1) -> filtersApi$showButtons(filter1, false));
        FilterBuilder.FILTERS.forEach((map, filter) -> filter.forEach(button -> button.visible = false));

        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;
        filtersApi$updateItems();
        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        filtersApi$showButtons(filter, true);
        for (int o = 0; o < filter.size(); o++) {
            if ((o >= filter.filterIndex) && (o < filter.filterIndex + 4)) {
                filter.get(o).x = leftPos - 28;
                filter.get(o).y = topPos + 29 * (o - filter.filterIndex) + 10;
                filter.get(o).visible = true;
            } else filter.get(o).visible = false;
        }
        filter.btnScrollUp.active = filter.filterIndex > 0;
        filter.btnScrollDown.active = filter.filterIndex + 4 < filter.size();
    }

    @Inject(at = @At("TAIL"), method = "render")
    protected void afterRender(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        if (!FilterBuilder.isTabHasFilters(selectedTab)) return;

        FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
        if (filter.btnScrollUp.isHoveredOrFocused()) renderTooltip(poseStack, filter.btnScrollUp.getMessage(), i, j);
        if (filter.btnScrollDown.isHoveredOrFocused()) renderTooltip(poseStack, filter.btnScrollDown.getMessage(), i, j);
        if (filter.btnEnableAll.isHoveredOrFocused()) renderTooltip(poseStack, filter.btnEnableAll.getMessage(), i, j);
        if (filter.btnDisableAll.isHoveredOrFocused()) renderTooltip(poseStack, filter.btnDisableAll.getMessage(), i, j);
        if (filter.btnOptions != null && filter.btnOptions.isHoveredOrFocused() && filter.btnReservedTooltip != null)
            renderTooltip(poseStack, filter.btnReservedTooltip, i, j);

        filter.forEach(filter1 -> {
            if (filter1.isHoveredOrFocused()) renderTooltip(poseStack, filter1.getMessage(), i, j);
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
                filter.btnOptions = new IconButton(this.leftPos - 50, this.topPos + 54, filter.btnReservedTooltip, filter.btnReservedOnPress, filter.btnReservedIcon, filter.btnReservedIconU, filter.btnReservedIconV);
                addRenderableWidget(filter.btnOptions);
            }
            addRenderableWidget(filter.btnScrollUp);
            addRenderableWidget(filter.btnScrollDown);
            addRenderableWidget(filter.btnEnableAll);
            addRenderableWidget(filter.btnDisableAll);

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
        if (list.btnOptions != null) {
            if (list.btnReservedOnPress != null) {
                list.btnOptions.visible = visible;
            } else {
                list.btnOptions.visible = false;
            }
        }
        list.btnEnableAll.visible = visible;
        list.btnDisableAll.visible = visible;
    }

    @Unique
    protected void filtersApi$updateItems() {
        visibleTags.clear();
        menu.items.clear(); // clear the tab
        FilterBuilder.FILTERS.get(selectedTab).forEach(
                filter -> {
                    if (filter.enabled) {
                        filter.items.forEach(item -> menu.items.add(new ItemStack(item))); // add items
                    }
                }
        );
        menu.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem()))); // sort items
        float previousOffset = this.scrollOffs;
        this.menu.scrollTo(0.0f); // refresh (maybe?)
        this.scrollOffs = previousOffset;
        this.menu.scrollTo(previousOffset);
    }
}