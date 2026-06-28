# Adding items <Badge type="warning" text="deprecated" />

::: danger
The following codes are just examples. They will throw exceptions on the server. You should ensure that Filters API codes are executed only on the client.
:::

::: tip
The tab that has filters will display categorized items only, except that you have registered uncategorized filter.
:::

Call `Filter.addItems(Item...)` after registering items to add them to the filter.

## Fabric

```java
public static void registerItem(String path, Item item, Filter filter) {
    [...]
    filter.addItems(item);
}
```

## Forge

::: tip
You should add items to filters after `ITEMS.register()` is called.
:::

```java
public static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(MOD_ID, Registry.ITEM_REGISTRY);
public static final ArrayList<Tuple<Filter, Item>> FILTER_ITEMS = new ArrayList<>();

public MainForge() {
    [...]
    ITEMS.register();
}

public static void registerItem(String path, Item item, Filter filter) {
    [...]
    FILTER_ITEMS.add(new Tuple<>(filter, item));
}

@SubscribeEvent
public static void onClientSetupEvent(FMLClientSetupEvent event) {
    [...]
    // initialize filters
    FILTER_ITEMS.forEach(tuple -> tuple.getA().addItems(tuple.getB()));
}
```

Congratulations! Now you can find filters when opening the creative mode tab.