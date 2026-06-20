package ziyue.filters.gui;

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
 * @since 1.0.0
 */

public class IconButton extends Button
{
    public static final WidgetSprites TEXTURES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

    protected ResourceLocation iconResource;

    public IconButton(int x, int y, Component tooltip, Button.OnPress onPress, ResourceLocation iconResource) {
        super(x, y, 20, 20, tooltip, onPress, DEFAULT_NARRATION);
        this.iconResource = iconResource;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blitSprite(RenderType::guiTextured, TEXTURES.get(this.active, this.isHovered), this.getX(), this.getY(), this.width, this.height);
        guiGraphics.blitSprite(RenderType::guiTextured, iconResource, this.getX() + 2, this.getY() + 2, 16, 16);
    }

    @Override
    public boolean isFocused() {
        return this.isHovered();
    }
}
