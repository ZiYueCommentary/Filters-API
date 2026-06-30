package ziyue.filters.hotswap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ziyue.filters.FiltersApi;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Reading configs of filters when resources are reloaded. This belongs to the modern filter implementation.
 *
 * @author ZiYueCommentary
 * @since 1.1.0
 */
public class HotswapConfigLoader
{
    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((barrier, manager, filler1, filler2, backgroundExecutor, gameExecutor) ->
                CompletableFuture.supplyAsync(() -> {
                    HotswapFiltersConfig.PENDING_FILTERS.clear();
                    final List<Resource> resources = manager.getResourceStack(new ResourceLocation(FiltersApi.MOD_ID, "filters.json"));
                    for (Resource resource : resources) {
                        try (Reader reader = resource.openAsReader()) {
                            final JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                            for (Map.Entry<String, JsonElement> jsonTab : json.entrySet()) {
                                if (!jsonTab.getValue().isJsonObject()) continue;
                                final Optional<CreativeModeTab> creativeModeTab = BuiltInRegistries.CREATIVE_MODE_TAB.getOptional(new ResourceLocation(jsonTab.getKey()));
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
                                        final Component uncategorizedTitle = Component.translatable(config.has("title") ?
                                                config.get("title").getAsString() : "filter.filters.uncategorized");
                                        final ResourceLocation uncategorizedIcon = new ResourceLocation(config.has("icon") ?
                                                config.get("icon").getAsString() : "minecraft:barrier");
                                        list.uncategorized = new PendingFilter(id, uncategorizedTitle, uncategorizedIcon, null);
                                        continue;
                                    }
                                    final Component title = Component.translatable(config.get("title").getAsString());
                                    final ResourceLocation icon = new ResourceLocation(config.get("icon").getAsString());
                                    if (usedId.contains(id)) {
                                        FiltersApi.LOGGER.warn("Duplicate filter {} in tab {}", id, jsonTab.getKey());
                                        continue;
                                    }
                                    usedId.add(id);
                                    list.add(new PendingFilter(id, title, icon, config.get("items").getAsJsonArray().asList().stream().map(item -> new ResourceLocation(item.getAsString())).toList()));
                                }
                                HotswapFiltersConfig.PENDING_FILTERS.put(creativeModeTab, list);
                            }
                        } catch (IOException e) {
                            FiltersApi.LOGGER.error("Error when loading filter configs!", e);
                        }
                    }
                    FiltersApi.itemsCategorized = false;
                    return null;
                }).thenCompose(barrier::wait).thenAcceptAsync(value -> {
                }));
    }
}
