package ziyue.filters.hotswap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import ziyue.filters.FiltersApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.StreamSupport;

/**
 * Reading configs of filters when resources are reloaded. This belongs to the modern filter implementation.
 *
 * @author ZiYueCommentary
 * @since 1.1.0
 */
public class HotswapConfigLoader implements IdentifiableResourceReloadListener
{
    @Override
    public Identifier getFabricId() {
        return new Identifier(FiltersApi.MOD_ID, "reload_filters");
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            HotswapFiltersConfig.PENDING_FILTERS.clear();
            final List<Resource> resources;
            try {
                resources = manager.getAllResources(new Identifier(FiltersApi.MOD_ID, "filters.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (Resource resource : resources) {
                try (Reader reader = new InputStreamReader(resource.getInputStream())) {
                    final JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                    for (Map.Entry<String, JsonElement> jsonTab : json.entrySet()) {
                        if (!jsonTab.getValue().isJsonObject()) continue;
                        final Optional<ItemGroup> creativeModeTab = Arrays.stream(ItemGroup.GROUPS).filter(group -> group.getName().equals(jsonTab.getKey())).findAny();
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
                                final Text uncategorizedTitle = new TranslatableText(config.has("title") ?
                                        config.get("title").getAsString() : "filter.filters.uncategorized");
                                final Identifier uncategorizedIcon = Identifier.tryParse(config.has("icon") ?
                                        config.get("icon").getAsString() : "minecraft:barrier");
                                list.uncategorized = new PendingFilter(id, uncategorizedTitle, uncategorizedIcon, null);
                                continue;
                            }
                            final Text title = new TranslatableText(config.get("title").getAsString());
                            final Identifier icon = Identifier.tryParse(config.get("icon").getAsString());
                            if (usedId.contains(id)) {
                                FiltersApi.LOGGER.warn("Duplicate filter {} in tab {}", id, jsonTab.getKey());
                                continue;
                            }
                            usedId.add(id);
                            list.add(new PendingFilter(id, title, icon,
                                    StreamSupport.stream(config.get("items").getAsJsonArray().spliterator(), false)
                                            .map(item -> Identifier.tryParse(item.getAsString())).toList()));
                        }
                        HotswapFiltersConfig.PENDING_FILTERS.put(creativeModeTab, list);
                    }
                } catch (IOException e) {
                    FiltersApi.LOGGER.error("Error when loading filter configs!", e);
                }
            }
            FiltersApi.itemsCategorized = false;
            return null;
        }).thenCompose(synchronizer::whenPrepared).thenAcceptAsync((result) -> {
        }, applyExecutor);
    }
}
