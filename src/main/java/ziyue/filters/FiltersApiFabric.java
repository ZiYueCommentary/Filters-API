package ziyue.filters;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class FiltersApiFabric implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        FiltersApi.LOGGER.info("Filters API initialized!");
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("1"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("2"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("3"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("4"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("5"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("6"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("7"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("8"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("9"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerFilter(ItemGroups.COMBAT, Text.literal("10"), () -> new ItemStack(Items.GLOW_ITEM_FRAME));
        FilterBuilder.registerUncategorizedItemsFilter(ItemGroups.COMBAT);
    }
}