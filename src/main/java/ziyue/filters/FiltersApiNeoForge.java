package ziyue.filters;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(FiltersApi.MOD_ID)
public class FiltersApiNeoForge
{
    public FiltersApiNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onClientSetup);
    }

    public void onClientSetup(final FMLClientSetupEvent event) {
        FiltersApi.LOGGER.info("Filters API initialized!");
    }
}
