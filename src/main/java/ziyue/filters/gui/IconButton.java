package ziyue.filters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Button with icon.
 *
 * @author ZiYueCommentary
 * @see <a href="https://github.com/MrCrayfish/Filters/blob/master/src/main/java/com/mrcrayfish/filters/gui/widget/button/IconButton.java">Filters Mod</a>
 * @since 1.0.0
 */

public class IconButton extends Button
{
    public static final WidgetSprites SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

    protected ResourceLocation iconResource;

    public IconButton(int x, int y, Component tooltip, OnPress onPress, ResourceLocation iconResource) {
        super(x, y, 20, 20, tooltip, onPress, DEFAULT_NARRATION);
        this.iconResource = iconResource;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_93658_, int p_93659_, float p_93660_) {
        RenderSystem.setShaderColor(1f, 1f, 1f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics.blitSprite(RenderType::guiTextured, SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        guiGraphics.blitSprite(RenderType::guiTextured, this.iconResource, this.getX() + 2, this.getY() + 2, 16, 16);
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}