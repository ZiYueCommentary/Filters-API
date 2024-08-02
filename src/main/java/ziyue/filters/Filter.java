package ziyue.filters;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import ziyue.filters.mixin.CreativeModeInventoryScreenMixin;
import ziyue.filters.mixin.EffectRenderingInventoryScreenMixin;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Filter for creative mode tab.
 * This filter support register for any creative mode tabs, including vanilla tabs.
 * The order of filters was depending on registering orders.
 *
 * @author ZiYueCommentary
 * @see CreativeModeInventoryScreenMixin
 * @see EffectRenderingInventoryScreenMixin
 * @see FilterBuilder
 * @since 1.0.0
 */

public class Filter extends Button
{
    public static final ResourceLocation TAB_SELECTED = ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/creative_inventory/tab_top_selected_2.png");
    public static final ResourceLocation TAB_UNSELECTED = ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/creative_inventory/tab_top_unselected_2.png");


    public Supplier<ItemStack> icon;
    public final List<Item> items;
    public boolean enabled = true;

    protected Filter(Component tooltip, Supplier<ItemStack> icon, List<Item> items) {
        super(0, 0, 32, 26, tooltip, Button::onPress, DEFAULT_NARRATION);
        this.icon = icon;
        this.items = items;
    }

    @Override
    public void onPress() {
        enabled = !enabled;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int p_93658_, int p_93659_, float p_93660_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, enabled ? TAB_SELECTED : TAB_UNSELECTED);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawRotatedTexture(graphics.pose().last().pose(), this.getX(), this.getY(), this.enabled ? 32 : 28);
        graphics.renderItem(icon.get(), this.getX() + 8, this.getY() + 5);
    }


    protected void drawRotatedTexture(Matrix4f pose, int x, int y, int width) {
        final float scaleX = 0.038524330F;
        final float scaleY = 0.031324208F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(pose, x, y + height, 0).setUv(((float) height * scaleX), 0);
        bufferBuilder.addVertex(pose, x + width, y + height, 0).setUv(((float) height * scaleX), ((float) width * scaleY));
        bufferBuilder.addVertex(pose, x + width, y, 0).setUv(0, ((float) width * scaleY));
        bufferBuilder.addVertex(pose, x, y, 0).setUv(0, 0);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
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
}
