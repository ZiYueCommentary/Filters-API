package ziyue.filters.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import ziyue.filters.FilterBuilder;
import ziyue.filters.FiltersApi;

public class FiltersApiFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersApi.LOGGER.info("Hello World!");
    }
}
