package ziyue.filters;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Inspired by <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a> and <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>.
 *
 * @author ZiYueCommentary
 * @see FilterBuilder
 * @see <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a>
 * @see <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>
 * @since 1.0.0
 */

public class FiltersApi
{
    public static final String MOD_ID = "filters";
    public static final Logger LOGGER = LogManager.getLogger("Filters API");
    public static final ResourceLocation ICON_CHECK = new ResourceLocation(FiltersApi.MOD_ID, "check");
    public static final ResourceLocation ICON_CROSS = new ResourceLocation(FiltersApi.MOD_ID, "cross");
    public static final ResourceLocation ICON_UP = new ResourceLocation(FiltersApi.MOD_ID, "up");
    public static final ResourceLocation ICON_DOWN = new ResourceLocation(FiltersApi.MOD_ID, "down");
    public static final ResourceLocation ICON_WRENCH = new ResourceLocation(FiltersApi.MOD_ID, "wrench");
}
