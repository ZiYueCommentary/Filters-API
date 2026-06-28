# Setting up

::: info
Replace **VERSION_ID** with the version of Filters API desired, and **MC_ID** with the version of Minecraft.
:::

::: danger
Do not wrap FIlters API into your mods.
:::

## Fabric/Quilt

```groovy
repositories {
    maven { url = "https://maven.ziyuesinicization.site/releases/" }
}

dependencies {
    modApi("ziyue.filters:filters-fabric:VERSION_ID+MC_ID") {
        exclude(group: "net.fabricmc")
    }
}
```

## Forge

```groovy
repositories {
    maven { url = "https://maven.ziyuesinicization.site/releases/" }
}

dependencies {
    implementation(fg.deobf("ziyue.filters:filters-forge:VERSION_ID+MC_ID")) {
        exclude(group: "net.minecraftforge")
    }
}
```

## NeoForge

```groovy
repositories {
    maven { url = "https://maven.ziyuesinicization.site/releases/" }
}

dependencies {
    implementation("ziyue.filters:filters-neoforge:VERSION_ID+MC_ID")
}
```