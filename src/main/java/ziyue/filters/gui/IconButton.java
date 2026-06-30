package ziyue.filters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
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
    public static final ButtonTextures TEXTURES = new ButtonTextures(new Identifier("widget/button"), new Identifier("widget/button_disabled"), new Identifier("widget/button_highlighted"));

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
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawGuiTexture(TEXTURES.get(this.active, this.isSelected()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        context.drawTexture(this.iconResource, this.getX() + 2, this.getY() + 2, iconU, iconV, 16, 16);
    }

    @Override
    public boolean isSelected() {
        return this.isHovered();
    }
}
