# Registering filters

To register filters, create config at `assets/filters/filters.json`.

```json
{
  "minecraft:combat": {
    "filter1": {
      "title": "Filter name",
      "icon": "minecraft:diamond_sword",
      "items": [
        "minecraft:diamond_helmet"
      ]
    }
  }
}
```

* `minecraft:combat` is ID (resource location) of the creative mode tab. It contains filters that belongs to it.
* `filter1` is ID of the filter. Filter's ID are unique inside the tab, so you can use same ID for different tabs. It
  contains the filter's config. You can add as much as you want.
* Inside the filter config, `title` is the tooltip that shows when hovering the filter. It can be literal text, or a
  translation key.
* `icon` is the ID (resource location) of the item that displays on the filter button.
* `items` is a JSON array, contains ID (resource location) of the item that belongs to this filter. Note that you can
  add items that not belongs to this tab originally.

When multiple configs try to add filters to the same tab, all filters of them will be added.

**All set! Just install Filters API in your game, and the filters will be shown on the tab.**