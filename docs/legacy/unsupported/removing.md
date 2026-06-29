# Removing filters <Badge type="danger" text="unsupported" />

The default index of the filter depends on the order of registering.

## 1.16~1.18.2

```java
Filter.FILTERS.get(CREATIVE_MODE_TAB.getIndex()).remove(index);
```

## 1.19 or above

```java
Filter.FILTERS.get(CREATIVE_MODE_TAB).remove(index);
```