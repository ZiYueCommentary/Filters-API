package ziyue.filters.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
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
    protected ResourceLocation iconResource;
    protected int iconU;
    protected int iconV;

    public IconButton(int x, int y, Component tooltip, OnPress onPress, ResourceLocation iconResource, int iconU, int iconV) {
        super(x, y, 20, 20, tooltip, onPress, DEFAULT_NARRATION);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    public void renderWidget(PoseStack matrices, int p_275505_, int p_275674_, float p_275696_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1f, 1f, 1f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int offset = this.getYImage(this.isHovered());
        blit(matrices, this.getX(), this.getY(), 0, 46 + offset * 20, this.width / 2, this.height);
        blit(matrices, this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + offset * 20, this.width / 2, this.height);
        RenderSystem.setShaderTexture(0, this.iconResource);
        blit(matrices, this.getX() + 2, this.getY() + 2, this.iconU, this.iconV, 16, 16);
    }

    public int getYImage(boolean hovered) {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (hovered) {
            i = 2;
        }

        return i;
    }
}
