package ziyue.filters;

import net.fabricmc.api.ClientModInitializer;

public class FiltersApiFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersApi.LOGGER.info("Filters API initialized!");
    }
}