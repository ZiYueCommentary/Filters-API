# Registering filters <Badge type="warning" text="deprecated" />

::: tip
Add Filters API as your project dependency first! Please read [Setting up](/setting-up.md)
:::

Call `FilterBuilder.registerFilter(ItemGroup, Text, Supplier)` to register a filter for the specific tab. The order of filters depends on the order of registering.

```java
Filter FILTER = FilterBuilder.registerFilter(CREATIVE_MODE_TAB, new TranslatableText("filter.modid.filter_name"), () -> new ItemStack(Items.ITEM));
```