package ziyue.filters;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import ziyue.filters.hotswap.HotswapConfigLoader;

public class FiltersApiFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersApi.LOGGER.info("Filters API initialized!");
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HotswapConfigLoader());
    }
}