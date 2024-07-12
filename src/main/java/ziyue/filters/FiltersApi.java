package ziyue.filters;

import net.minecraft.util.Identifier;
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
    public static final Identifier ICON_CHECK = new Identifier(FiltersApi.MOD_ID, "check");
    public static final Identifier ICON_CROSS = new Identifier(FiltersApi.MOD_ID, "cross");
    public static final Identifier ICON_UP = new Identifier(FiltersApi.MOD_ID, "up");
    public static final Identifier ICON_DOWN = new Identifier(FiltersApi.MOD_ID, "down");
    public static final Identifier ICON_WRENCH = new Identifier(FiltersApi.MOD_ID, "wrench");
}
