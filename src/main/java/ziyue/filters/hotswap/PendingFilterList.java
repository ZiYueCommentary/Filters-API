package ziyue.filters.hotswap;

import java.util.ArrayList;

/**
 * Filter list but not checked yet. Filters API will remove invalid configs from it.
 * This belongs to the modern filter implementation.
 *
 * @author ZiYueCommentary
 * @see ziyue.filters.FilterList
 * @since 1.1.0
 */
public class PendingFilterList extends ArrayList<PendingFilter>
{
    public PendingFilter uncategorized = null;
}
