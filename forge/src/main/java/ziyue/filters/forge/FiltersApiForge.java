package ziyue.filters.forge;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import ziyue.filters.FilterBuilder;
import ziyue.filters.FiltersApi;

@Mod(FiltersApi.MOD_ID)
public class FiltersApiForge
{
    public FiltersApiForge() {
        FiltersApi.LOGGER.info("Hello World!");
    }
}
