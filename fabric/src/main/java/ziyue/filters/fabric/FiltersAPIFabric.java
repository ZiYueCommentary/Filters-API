package ziyue.filters.fabric;

import net.fabricmc.api.ClientModInitializer;
import ziyue.filters.FiltersApi;

public class FiltersAPIFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersApi.LOGGER.info("Hello World!");
    }
}
