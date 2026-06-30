package ziyue.filters.hotswap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ziyue.filters.FiltersApi;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Reading configs of filters when resources are reloaded. This belongs to the modern filter implementation.
 *
 * @author ZiYueCommentary
 * @since 1.1.0
 */
public class HotswapConfigLoader implements SimpleSynchronousResourceReloadListener
{
    @Override
    public Identifier getFabricId() {
        return Identifier.of(FiltersApi.MOD_ID, "reload_filters");
    }

    @Override
    public void reload(ResourceManager manager) {
        HotswapFiltersConfig.PENDING_FILTERS.clear();
        final List<Resource> resources = manager.getAllResources(Identifier.of(FiltersApi.MOD_ID, "filters.json"));
        for (Resource resource : resources) {
            try (Reader reader = resource.getReader()) {
                final JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                for (Map.Entry<String, JsonElement> jsonTab : json.entrySet()) {
                    if (!jsonTab.getValue().isJsonObject()) continue;
                    final Optional<ItemGroup> creativeModeTab = Registries.ITEM_GROUP.getOrEmpty(Identifier.tryParse(jsonTab.getKey()));
                    if (creativeModeTab.isEmpty()) {
                        FiltersApi.LOGGER.warn("Tab {} not found! Skipping...", jsonTab.getKey());
                        continue;
                    }
                    final PendingFilterList list = HotswapFiltersConfig.PENDING_FILTERS.getOrDefault(creativeModeTab, new PendingFilterList());
                    final Set<String> usedId = new HashSet<>();
                    for (Map.Entry<String, JsonElement> filters : jsonTab.getValue().getAsJsonObject().entrySet()) {
                        final String id = filters.getKey();
                        final JsonObject config = filters.getValue().getAsJsonObject();
                        if (id.equals("uncategorized")) {
                            final Text uncategorizedTitle = Text.translatable(config.has("title") ?
                                    config.get("title").getAsString() : "filter.filters.uncategorized");
                            final Identifier uncategorizedIcon = Identifier.tryParse(config.has("icon") ?
                                    config.get("icon").getAsString() : "minecraft:barrier");
                            list.uncategorized = new PendingFilter(id, uncategorizedTitle, uncategorizedIcon, null);
                            continue;
                        }
                        final Text title = Text.translatable(config.get("title").getAsString());
                        final Identifier icon = Identifier.tryParse(config.get("icon").getAsString());
                        if (usedId.contains(id)) {
                            FiltersApi.LOGGER.warn("Duplicate filter {} in tab {}", id, jsonTab.getKey());
                            continue;
                        }
                        usedId.add(id);
                        list.add(new PendingFilter(id, title, icon, config.get("items").getAsJsonArray().asList().stream().map(item -> Identifier.tryParse(item.getAsString())).toList()));
                    }
                    HotswapFiltersConfig.PENDING_FILTERS.put(creativeModeTab, list);
                }
            } catch (IOException e) {
                FiltersApi.LOGGER.error("Error when loading filter configs!", e);
            }
        }
        FiltersApi.itemsCategorized = false;
    }
}
