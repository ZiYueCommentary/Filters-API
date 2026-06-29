package ziyue.filters;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import ziyue.filters.hotswap.HotswapConfigLoader;

@Mod(FiltersApi.MOD_ID)
public class FiltersApiNeoForge
{
    public FiltersApiNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(HotswapConfigLoader::onRegisterReloadListeners);
    }

    public void onClientSetup(final FMLClientSetupEvent event) {
        FiltersApi.LOGGER.info("Filters API initialized!");
    }
}
