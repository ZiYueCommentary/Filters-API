package ziyue.filters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Button with icon.
 *
 * @author ZiYueCommentary
 * @see <a href="https://github.com/MrCrayfish/Filters/blob/master/src/main/java/com/mrcrayfish/filters/gui/widget/button/IconButton.java">Filters Mod</a>
 * @since 1.0.0
 */

public class IconButton extends ButtonWidget
{
    protected Identifier iconResource;
    protected int iconU;
    protected int iconV;

    public IconButton(int x, int y, Text tooltip, PressAction onPress, Identifier iconResource, int iconU, int iconV) {
        super(x, y, 20, 20, tooltip, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        context.drawNineSlicedTexture(WIDGETS_TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getYImage(this.isHovered()));
        context.drawTexture(this.iconResource, this.getX() + 2, this.getY() + 2, iconU, iconV, 16, 16);
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    public int getYImage(boolean hovered) {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (hovered) {
            i = 2;
        }

        return 46 + i * 20;
    }
}
