package ziyue.filters.hotswap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
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
@EventBusSubscriber(modid = FiltersApi.MOD_ID)
public class HotswapConfigLoader
{
    @SubscribeEvent
    public static void onRegisterReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(ResourceLocation.fromNamespaceAndPath(FiltersApi.MOD_ID, "filters"),
                (barrier, manager, backgroundExecutor, gameExecutor) ->
                        CompletableFuture.supplyAsync(() -> {
                            HotswapFiltersConfig.PENDING_FILTERS.clear();
                            final List<Resource> resources = manager.getResourceStack(ResourceLocation.fromNamespaceAndPath(FiltersApi.MOD_ID, "filters.json"));
                            for (Resource resource : resources) {
                                try (Reader reader = resource.openAsReader()) {
                                    final JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                                    for (Map.Entry<String, JsonElement> jsonTab : json.entrySet()) {
                                        if (!jsonTab.getValue().isJsonObject()) continue;
                                        final Optional<Holder.Reference<CreativeModeTab>> creativeModeTab = BuiltInRegistries.CREATIVE_MODE_TAB.get(ResourceLocation.parse(jsonTab.getKey()));
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
                                                final ResourceLocation uncategorizedIcon = ResourceLocation.parse(config.has("icon") ?
                                                        config.get("icon").getAsString() : "minecraft:barrier");
                                                list.uncategorized = new PendingFilter(id, uncategorizedTitle, uncategorizedIcon, null);
                                                continue;
                                            }
                                            final Component title = Component.translatable(config.get("title").getAsString());
                                            final ResourceLocation icon = ResourceLocation.parse(config.get("icon").getAsString());
                                            if (usedId.contains(id)) {
                                                FiltersApi.LOGGER.warn("Duplicate filter {} in tab {}", id, jsonTab.getKey());
                                                continue;
                                            }
                                            usedId.add(id);
                                            list.add(new PendingFilter(id, title, icon, config.get("items").getAsJsonArray().asList().stream().map(item -> ResourceLocation.parse(item.getAsString())).toList()));
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
