package ziyue.filters;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import ziyue.filters.mixin.CreativeInventoryScreenMixin;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Filter for creative mode tab.
 * This filter support register for any creative mode tabs, including vanilla tabs.
 * The order of filters was depending on registering orders.
 *
 * @author ZiYueCommentary
 * @see CreativeInventoryScreenMixin
 * @see ziyue.filters.mixin.AbstractInventoryScreenMixin
 * @see FilterBuilder
 * @since 1.0.0
 */

public class Filter extends ButtonWidget
{
    public static final Identifier CREATIVE_TABS_LOCATION = new Identifier("textures/gui/container/creative_inventory/tabs.png");

    public Supplier<ItemStack> icon;
    public final List<Item> items;
    public boolean enabled = true;

    public Filter(Text tooltip, Supplier<ItemStack> icon, List<Item> items) {
        super(0, 0, 32, 28, tooltip, ButtonWidget::onPress);
        this.icon = icon;
        this.items = items;
    }

    @Override
    public void onPress() {
        enabled = !enabled;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        minecraft.getTextureManager().bindTexture(CREATIVE_TABS_LOCATION);

        GlStateManager.blendColor(1f, 1f, 1f, this.alpha);
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528, GlStateManager.SrcFactor.ONE.field_22545, GlStateManager.DstFactor.ZERO.field_22528);
        GlStateManager.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528);

        int width = this.enabled ? 32 : 28;
        int textureX = 28;
        int textureY = this.enabled ? 32 : 0;
        this.drawRotatedTexture(matrices.peek().getModel(), x, y, textureX, textureY, width);

        RenderSystem.enableRescaleNormal();
        ItemRenderer renderer = minecraft.getItemRenderer();
        renderer.renderGuiItemIcon(icon.get(), x + 8, y + 6);
    }


    protected void drawRotatedTexture(Matrix4f pose, int x, int y, int textureX, int textureY, int width) {
        float scaleX = 0.00390625F;
        float scaleY = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(pose, x, y + height, 0f).texture(((float) (textureX + height) * scaleX), ((float) (textureY) * scaleY)).next();
        bufferBuilder.vertex(pose, x + width, y + height, 0f).texture(((float) (textureX + height) * scaleX), ((float) (textureY + width) * scaleY)).next();
        bufferBuilder.vertex(pose, x + width, y, 0f).texture(((float) (textureX) * scaleX), ((float) (textureY + width) * scaleY)).next();
        bufferBuilder.vertex(pose, x, y, 0f).texture(((float) (textureX) * scaleX), ((float) (textureY) * scaleY)).next();
        tessellator.draw();
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
