package ziyue.filters.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
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

    public IconButton(int x, int y, Component component, OnPress onPress, ResourceLocation iconResource, int iconU, int iconV) {
        super(x, y, 20, 20, component, onPress);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bind(WIDGETS_LOCATION);
        GlStateManager._blendColor(1f, 1f, 1f, 1f);
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
        int offset = this.getYImage(this.isHovered);
        this.blit(poseStack, this.x, this.y, 0, 46 + offset * 20, this.width / 2, this.height);
        this.blit(poseStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + offset * 20, this.width / 2, this.height);
        if (!this.active) GlStateManager._blendColor(0.5f, 0.5f, 0.5f, 1f);
        Minecraft.getInstance().getTextureManager().bind(this.iconResource);
        this.blit(poseStack, this.x + 2, this.y + 2, this.iconU, this.iconV, 16, 16);
    }
}
