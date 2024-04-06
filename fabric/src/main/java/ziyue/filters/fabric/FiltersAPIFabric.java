package ziyue.filters.fabric;

import net.fabricmc.api.ModInitializer;
import ziyue.filters.FiltersAPI;

public class FiltersAPIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FiltersAPI.LOGGER.info("Hello World!");
    }
}
