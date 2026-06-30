package ziyue.filters.hotswap;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Configs of filters but not checked yet. Filters API will remove invalid configs from it.
 * This belongs to the modern filter implementation.
 *
 * @author ZiYueCommentary
 * @see ziyue.filters.FilterBuilder#FILTERS
 * @see ziyue.filters.Filter
 * @since 1.1.0
 */
public record PendingFilter(String id, Component title, ResourceLocation icon, List<ResourceLocation> items)
{
}
