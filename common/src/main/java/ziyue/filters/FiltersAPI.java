package ziyue.filters;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ZiYueCommentary
 * @since 1.0.0
 * @see Filter
 */

@Environment(EnvType.CLIENT)
public class FiltersAPI
{
    public static final String MOD_ID = "filters";
    public static final Logger LOGGER = LogManager.getLogger("Filters API");
    public static final ResourceLocation ICONS = new ResourceLocation(FiltersAPI.MOD_ID, "textures/gui/filters.png");
}