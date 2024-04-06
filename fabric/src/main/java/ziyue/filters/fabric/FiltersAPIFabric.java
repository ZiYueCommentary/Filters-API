package ziyue.filters.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import ziyue.filters.FiltersAPI;

public class FiltersAPIFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersAPI.LOGGER.info("Hello World!");
    }
}
