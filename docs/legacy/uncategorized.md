# Registering uncategorized filter <Badge type="warning" text="deprecated" />

::: tip
Filters API allows you to add filters for other mods (even vanilla). If so, registering the uncategorized filter to avoid some items disappearing is very important.
:::

You can register an uncategorized filter for the tab. All items in the tab which not belong to any filter will be categorized to it.

A tab can have a single uncategorized filter only. If there are no other filters or no items are uncategorized, this filter will not appear on the screen.

Call `FilterBuilder.registerUncategorizedItemsFilter(ItemGroup)` to register the uncategorized filter.

```java
Filter FILTER = FilterBuilder.registerUncategorizedItemsFilter(CREATIVE_MODE_TAB);
```