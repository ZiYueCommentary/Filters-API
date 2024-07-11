package ziyue.filters.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
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
        super(x, y, 20, 20, tooltip, onPress);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(WIDGETS_TEXTURE);
        GlStateManager.blendColor(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_COLOR.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528, GlStateManager.SrcFactor.ONE.field_22545, GlStateManager.DstFactor.ZERO.field_22528);
        GlStateManager.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528);
        int offset = this.getYImage(this.isHovered());
        this.drawTexture(matrices, this.x, this.y, 0, 46 + offset * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + offset * 20, this.width / 2, this.height);
        if (!this.active) GlStateManager.blendColor(0.5f, 0.5f, 0.5f, 1f);
        MinecraftClient.getInstance().getTextureManager().bindTexture(this.iconResource);
        this.drawTexture(matrices, this.x + 2, this.y + 2, this.iconU, this.iconV, 16, 16);
    }
}
