# Configuring reserved button

![](/reserved-button.png)

Filters API has a third button for all tabs. Usually, it can be used to open the options' menu.

::: info
The core of the button is `onPress`, the shows of the button depend on whether `onPress` is `null`.
:::

Call `FilterBuilder.setReservedButton(ItemGroup, Text, PressAction)` to add the "Reserved button".

```java
FilterBuilder.setReservedButton(CREATIVE_MODE_TAB, new TranslatableText("tooltip.modid.options"), button -> foo());
```