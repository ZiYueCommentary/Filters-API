package ziyue.filters;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ziyue.filters.mixin.CreativeModeInventoryScreenMixin;
import ziyue.filters.mixin.EffectRenderingInventoryScreenMixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static ziyue.filters.FiltersAPI.ICONS;

/**
 * Filter for creative mode tab.
 * This filter support register for any creative mode tabs, including vanilla tabs.
 * The order of filters was depending on registering orders.
 * Inspired by <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a> and <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>.
 *
 * @author ZiYueCommentary
 * @see CreativeModeInventoryScreenMixin
 * @see EffectRenderingInventoryScreenMixin
 * @see <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a>
 * @see <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>
 * @since 1.0.0
 */

@Environment(EnvType.CLIENT)
public class Filter extends Button
{
    public static final HashMap<Integer, FilterList> FILTERS = new HashMap<>();

    protected static final ResourceLocation TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    public Supplier<ItemStack> icon;
    public final List<Item> items;
    public boolean enabled = true;

    protected Filter(Component component, Supplier<ItemStack> icon, List<Item> items) {
        super(0, 0, 32, 28, component, Button::onPress, null);
        this.icon = icon;
        this.items = items;
    }

    @Override
    public void onPress() {
        enabled = !enabled;
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(TABS);

        GlStateManager._blendColor(1f, 1f, 1f, this.alpha);
        GlStateManager._disableLighting();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);

        int width = this.enabled ? 32 : 28;
        int textureX = 28;
        int textureY = this.enabled ? 32 : 0;
        this.drawRotatedTexture(poseStack.last().pose(), x, y, textureX, textureY, width);

        RenderSystem.enableRescaleNormal();
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.blitOffset = 100f;
        renderer.renderAndDecorateItem(icon.get(), x + 8, y + 6);
        renderer.renderGuiItemDecorations(mc.font, icon.get(), x + 8, y + 6);
        renderer.blitOffset = 0f;
    }

    protected void drawRotatedTexture(Matrix4f pose, int x, int y, int textureX, int textureY, int width) {
        float scaleX = 0.00390625F;
        float scaleY = 0.00390625F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose, x, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        tesselator.end();
    }

    public static void setOptionsButton(CreativeModeTab tab, Component tooltip, Button.OnPress onPress){
        Filter.setOptionsButton(tab, tooltip, onPress, ICONS, 64, 0);
    }

    public static void setOptionsButton(CreativeModeTab tab, Component tooltip, Button.OnPress onPress, ResourceLocation icon, int iconU, int iconV) {
        FilterList filters = Filter.FILTERS.get(tab.getId());
        filters.btnOptionsTooltip = tooltip;
        filters.btnOptionsOnPress = onPress;
        filters.btnOptionsIcon = icon;
        filters.btnOptionsIconU = iconU;
        filters.btnOptionsIconV = iconV;
    }

    /**
     * Adding items to the filter.
     *
     * @param items items
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public Filter addItems(Item... items) {
        this.items.addAll(Arrays.asList(items));
        return this;
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
        for (Filter filter : Filter.FILTERS.get(creativeModeTabId)) {
            if (filter.items.contains(item)) return true;
        }
        return false;
    }

    /**
     * @see #isItemCategorized(int, Item)
     * @since 1.0.0
     */
    public static boolean isItemCategorized(CreativeModeTab tab, Item item){
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
        return (Filter.FILTERS.containsKey(creativeModeTabId) && !Filter.FILTERS.get(creativeModeTabId).isEmpty() && (Filter.FILTERS.get(creativeModeTabId).enabled));
    }

    /**
     * @see #isTabHasFilters(int)
     * @since 1.0.0
     */
    public static boolean isTabHasFilters(CreativeModeTab tab){
        return isTabHasFilters(tab.getId());
    }
}
