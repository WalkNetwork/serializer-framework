<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://img.shields.io/static/v1?style=for-the-badge&label=author&message=uinnn&color=informational"/>
</a>
<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://img.shields.io/static/v1?style=for-the-badge&label=version&message=1.5.2v&color=ff69b4"/>
</a>
<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://img.shields.io/static/v1?style=for-the-badge&label=maven-central&message=1.5.2&color=orange"/>
</a>
<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://img.shields.io/static/v1?style=for-the-badge&label=license&message=MIT License&color=success"/>
</a>

# serializer-framework
Is a lightweight kotlin serializer library to standalone or spigot use!

### Objective 📝
The Kotlin serialization library is really awesome, however certain things you have to do manually,
like writing the string to a file. With `serializer-framework` you can do this and much more!
Although this framework was planned and structured for use with Bukkit/Spigot, you can use it as a Standalone, thus making this library very extensible.
You can load, reload, save and clear the file easily! 
If you're a Bukkit/Spigot developer, you might be tired of always when getting a string value or a list of strings in your plugin configuration,
always having to translate color codes like '&' to '§' and vice- versa. With `serializer-framework`, you never need to do this again!

### Supported Files 
* YAML ✔️
* JSON ✔️
* Protocol Buffers ✔️
* Minecraft NBT (.dat) ✔️

### 1.5.2v Patch notes
* Now binary serial files encode/decode from byte arrays instead of hex string.
* Binary serial file size encoding decreased ~50%

### How To Use
#### You can see the dokka documentation [here](https://uinnn.github.io/serializer-framework/)

If you've come this far and are curious to see how this framework works, I'll show you!
Let's assume you have a serializable class called Settings:

```kotlin
@Serializable
data class Settings(var name: String = "uinnn")
```

Now let's suppose you want to transfer the class to a file:
> Note: when creating any instance of a serial file will directly loads.

```kotlin
// by yaml
val yamlConfig by lazy {
  yaml(file, Settings())
}

// by json
val jsonConfig by lazy {
  json(file, Settings())
}

// by protobuf
val protobufConfig by lazy {
  protobuf(file, Settings())
}

// by named binary tag (nbt, also .dat files)
val nbtConfig by lazy { 
  nbt(file, Settings())
}

// with plugin (adds in the datafolder of the plugin)
val pluginConfig by lazy { 
  plugin.yaml("settings" /* already add a .yaml extension */, Settings())
}
```

Getting the settings model class:

```kotlin
inline val settings get() = config.settings
val name = settings.name
```

Reloading a serial file:

```kotlin
config.reload()
```

Saving a serial file:

```kotlin
config.save()
```

Clearing a serial file:

```kotlin
config.clear()
```

Getting the raw content of the serial file:

```kotlin
config.content
```

Transforming to another file location:

```kotlin
config.transformFile(newFile)
```

### Observers
Observers is an event handler that will be triggered when a reaction occurs with the serial file.
There are currently five observers:

* LOAD
* RELOAD
* SAVE
* SAVE_MODEL
* CREATE

Registering a observer:

```kotlin
config.onObserve(ObserverKind.LOAD) {
  println("Serial File Loaded!")
}

// shortcut:
config.onLoad {
  println("Serial File Loaded!")
}
```

Triggering a observer:

```kotlin
config.observe(ObserverKind.LOAD)
```

### Module
`serializer-framework` comes with a standard serialization module, which implements the following serializers:

* ItemStack
* Enchantment
* Location
* MaterialData
* Player
* StringList (replaces all '&' to '§' and vice-versa)
* UUID
* World

The module can accessed by:
```kotlin
FrameworkModule
```

Let's assume you have your own serializer and want to integrate with the existing serializers
that FrameworkModule offers, you can do it in two ways:

### Append Way
The append way is how a module will add serializers without overwriting it, this is,
if you create your own serializer of some type that you already have in the default module,
like `Player`, your serializer will not be added, because one already exists.

```kotlin
config.appendModule(YourOwnSerializerModule)
```

### Overwrite Way
The overwrite way is similar to the append way, but this will overwrite if your module
contains a serializer type that already contains in the default module.

```kotlin
config.overwriteModule(YourOwnSerializerModule)
```

### Strategy
Strategy is a way to modify how Kotlin Serialization should encode or decode string, numbers, chars and booleans.
An example of how it works with a default strategy already defined in the framework:

```kotlin
object ColorStrategy : Strategy /* Strategy implements EncoderStrategy and DecoderStrategy */ {
  override fun encodeString(descriptor: SerialDescriptor, index: Int, value: String): String {
    return value.replace('§', '&')
  }

  override fun decodeString(descriptor: SerialDescriptor, index: Int, value: String): String {
    return value.replace('&', '§')
  }
}
```

Another example to reverses all booleans:

```kotlin
object ReverseBooleanStrategy : EncoderStrategy, DecoderStrategy {
  override fun encodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean): Boolean {
    return value.not()
  }

  override fun decodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean): Boolean {
    return value.not()
  }
}
```

### Applying your custom Strategy:
Has two ways to apply your custom strategy

1. Creating a separeted strategy format property:
```kotlin
// here we use for yaml format
val CustomYamlStrategyFormat by lazy {
  StrategyStringFormatter(DefaultYamlFormat, encoder = ReverseBooleanStrategy, decoder = ReverseBooleanStrategy)
}

// now using them:
config.format = CustomYamlStrategyFormat

// or when init:
val config = yaml(file, Settings::class, CustomYamlStrategyFormat)
```

2. Overwriting the old strategy:
```kotlin
config.format.encoder = ReverseBooleanStrategy
config.format.decoder = ReverseBooleanStrategy
```
And ready! You already applied your own custom strategy!

## Setup for development
The `serializer-framework` is in the central maven repository. Thus making things very easy!

### Gradle Kotlin DSL

```gradle
implementation("io.github.uinnn:serializer-framework:1.5.2")
```

### Gradle
```gradle
implementation 'io.github.uinnn:serializer-framework:1.5.2'
```

### Maven

```xml
<dependency>
  <groupId>io.github.uinnn</groupId>
  <artifactId>serializer-framework</artifactId>
  <version>1.5.2</version>
</dependency>
```

### Final notes
The `serializer-framework` **NOT** contains the kotlin runtime, kotlin serialization and others needed to run this framework,
so you should implement them directly in your project.
To make your life easier, here is all the implementation of the libraries needed to run the framework:

```gradle
plugins {
  kotlin("jvm") version "1.5.21"
  kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
  implementation(kotlin("stdlib-jdk8")) // the kotlin std lib with jdk8
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21") // the kotlin reflect 1.5.21
  
  // NOTE VERSIONS 1.5+ NOW ALREADY ADDS THE DEPENDENCIES BELOW AS API.
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2") // the kotlin serialization core 1.2.2
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2") // the kotlin json serialization 1.2.2
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.2.2") // the kotlin protobuf serialization 1.2.2
  implementation("com.charleskorn.kaml:kaml:0.35.0") // the yaml serialization 0.35.0
  implementation("net.benwoodworth.knbt:knbt:0.6.1") // the minecraft nbt (.dat) serialization 0.6.1
}
```
