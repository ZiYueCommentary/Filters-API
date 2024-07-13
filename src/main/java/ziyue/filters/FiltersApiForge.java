package ziyue.filters;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FiltersApi.MOD_ID)
public class FiltersApiForge
{
    public FiltersApiForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void doClientStuff(final FMLClientSetupEvent event) {
        FiltersApi.LOGGER.info("Filters API initialized!");
    }
}
