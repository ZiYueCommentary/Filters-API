package ziyue.filters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
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
    protected static final WidgetSprites SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

    protected ResourceLocation iconResource;

    public IconButton(int x, int y, Component tooltip, OnPress onPress, ResourceLocation iconResource) {
        super(x, y, 20, 20, tooltip, onPress, DEFAULT_NARRATION);
        this.iconResource = iconResource;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int p_282682_, int p_281714_, float p_282542_) {
        graphics.setColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        graphics.blitSprite(SPRITES.get(this.active, this.isHovered), this.getX(), this.getY(), this.width, this.height);
        graphics.blitSprite(this.iconResource, this.getX() + 2, this.getY() + 2, 16, 16);
    }
}
