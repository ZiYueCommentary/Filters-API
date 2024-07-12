package ziyue.filters;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;

public class FiltersApiFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersApi.LOGGER.info("Filters API initialized!");
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("1"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("2"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("3"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("4"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("5"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("6"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("7"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("8"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("9"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroup.COMBAT, new LiteralText("10"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerUncategorizedItemsFilter(ItemGroup.COMBAT);
    }
}