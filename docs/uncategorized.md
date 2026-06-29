# Registering uncategorized filter

You can register an uncategorized filter for the tab. All items in the tab which not belong to any filter will be categorized to it.

A tab can have a single uncategorized filter only. If there are no other filters or no items are uncategorized, this filter will not appear on the screen.

```json{10-12}
{
  "minecraft:combat": {
    "filter1": {
      "title": "Filter name",
      "icon": "minecraft:diamond_sword",
      "items": [
        "minecraft:diamond_helmet"
      ]
    },
    "uncategorized": {
      
    }
  }
}
```

**Just add the highlighted section into your config, the uncategorized filter are set!** You can also customize `title` and `icon`, but not `items`.