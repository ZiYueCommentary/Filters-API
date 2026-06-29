# Sorting filters <Badge type="danger" text="unsupported" />

## 1.16~1.18.2

```java
Filter.FILTERS.get(CREATIVE_MODE_TAB.getIndex()).sort((a, b) -> foo());
```

## 1.19 or above

```java
Filter.FILTERS.get(CREATIVE_MODE_TAB).sort((a, b) -> bar())
```