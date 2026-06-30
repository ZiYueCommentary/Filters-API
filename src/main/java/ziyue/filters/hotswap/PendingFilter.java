package ziyue.filters.hotswap;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ziyue.filters.Filter;
import ziyue.filters.FilterBuilder;

import java.util.List;
import java.util.Objects;

/**
 * Configs of filters but not checked yet. Filters API will remove invalid configs from it.
 * This belongs to the modern filter implementation.
 *
 * @author ZiYueCommentary
 * @see FilterBuilder#FILTERS
 * @see Filter
 * @since 1.1.0
 */
public final class PendingFilter
{
    private final String id;
    private final Text title;
    private final Identifier icon;
    private final List<Identifier> items;

    public PendingFilter(String id, Text title, Identifier icon, List<Identifier> items) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.items = items;
    }

    public String id() {
        return id;
    }

    public Text title() {
        return title;
    }

    public Identifier icon() {
        return icon;
    }

    public List<Identifier> items() {
        return items;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        PendingFilter that = (PendingFilter) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.icon, that.icon) &&
                Objects.equals(this.items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, icon, items);
    }

    @Override
    public String toString() {
        return "PendingFilter[" +
                "id=" + id + ", " +
                "title=" + title + ", " +
                "icon=" + icon + ", " +
                "items=" + items + ']';
    }

}
