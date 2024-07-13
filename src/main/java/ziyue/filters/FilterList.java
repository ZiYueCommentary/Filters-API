package ziyue.filters;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import ziyue.filters.gui.IconButton;

import java.util.ArrayList;

/**
 * Filter list for creative mode tabs.
 * This list including unique buttons and index of tabs. This feature can separate button states and scrolling states with other tabs.
 *
 * @author ZiYueCommentary
 * @see ArrayList
 * @since 1.0.0
 */

public class FilterList extends ArrayList<Filter>
{
    public IconButton btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll, btnReserved;
    public int filterIndex = 0;
    public boolean enabled = true;

    public Filter uncategorizedItems = null;
    public Button.IPressable btnReservedOnPress = null;
    public ITextComponent btnReservedTooltip = null;
    public ResourceLocation btnReservedIcon = null;
    public int btnReservedIconU = 0, btnReservedIconV = 0;

    /**
     * Creating an empty filter.
     *
     * @return an empty filter
     * @author ZiYueCommentary
     * @since 1.0.0
     */
    public static FilterList empty() {
        return new FilterList();
    }
}