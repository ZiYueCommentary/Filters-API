package ziyue.filters;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ziyue.filters.hotswap.HotswapConfigLoader;

@Mod(FiltersApi.MOD_ID)
public class FiltersApiForge
{
    public FiltersApiForge() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::doClientStuff);
        bus.addListener(HotswapConfigLoader::onRegisterReloadListeners);
    }

    public void doClientStuff(final FMLClientSetupEvent event) {
        FiltersApi.LOGGER.info("Filters API initialized!");
    }
}
