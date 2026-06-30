package ziyue.filters;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import ziyue.filters.hotswap.HotswapConfigLoader;

public class FiltersApiFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersApi.LOGGER.info("Filters API initialized!");
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new HotswapConfigLoader());
    }
}