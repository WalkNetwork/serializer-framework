<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://github-readme-stats.vercel.app/api?username=uinnn&show_icons=true&theme=cobalt&hide_border=true"/>
</a>
<br>
<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://github-readme-stats.vercel.app/api/pin/?username=uinnn&repo=serializer-framework&show_icons=true&theme=cobalt&hide_border=truet"/>
</a>

# serializer-framework
Is a lightweight kotlin serializer library to standalone or spigot use!

### Objective
The Kotlin serialization library is really awesome, however certain things you have to do manually,
like writing the string to a file. With `serializer-framework` you can do this and much more!
Although this framework was planned and structured for use with Bukkit/Spigot, you can use it as a Standalone, thus making this library very extensible.
You can load, reload, save and clear the file easily! 
If you're a Bukkit/Spigot developer, you might be tired of always when getting a string value or a list of strings in your plugin configuration,
always having to translate color codes like '&' to '§' and vice- versa. With `serializer-framework`, you never need to do this again!

### Supported Files
* YAML
* JSON
* Protocol Buffers

### How To Use
If you've come this far and are curious to see how this framework works, I'll show you!
Let's assume you have a serializable class called Settings:

```kotlin
@Serializable
data class Settings(var name: String = "§auinnn")
```

Now let's suppose you want to transfer the class to a YAML file:
> Note: when creating any instance of a serial file will directly loads.

```kotlin
val configYaml by lazy { yaml(file, Settings::class) }
val configJson by lazy { json(file, Settings::class) }
val configProtoBuf by lazy { protobuuf(file, Settings::class) }

// with plugin (adds in the datafolder of the plugin)
val configYamlPlugin by lazy { plugin.yaml("settings" /* already add a .yaml extension */, Settings::class) }
// ...
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
  println("§aSerial File Loaded!")
}

// shortcut:
config.onLoad {
  println("§aSerial File Loaded!")
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
object ColorStrategy : Strategy // Strategy implements EncoderStrategy and DecoderStrategy {
  override fun encodeString(descriptor: SerialDescriptor, index: Int, value: String) = value.replace('§', '&')

  override fun decodeString(descriptor: SerialDescriptor, index: Int, value: String) = value.replace('&', '§')
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
To apply your custom strategy, you must create a StrategySerialFormatter.
Depending on the type of file you want, for example JSON or YAML are files of StringFormat type, however the ProtocolBuffer is a file of BinaryFormat type.
So if you want to implement your own strategy, you have to review or add support for one of these file types.

### Setup for creating custom Strategy:
In this example, we will use the StringFormat type:

1. Create a custom class that implements AlterableStringFormat:

```kotlin
class AlterableCustomFormat(
  override var serializersModule: SerializersModule,
  val model: StringFormat
) : AlterableStringFormat {
  override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
    return model.decodeFromString(deserializer, string)
  }

  override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
    return model.encodeToString(serializer, value)
  }
}
```

2. Create a lazy init property thats holds the AlterableCustomFormat class:

```kotlin
// in this example we use for YAML files. But you can do with JSON too.
val CustomYamlFormat by lazy {
  AlterableCustomFormat(
    FrameworkModule,
    Yaml(FrameworkModule, YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property))
  )
}
```

3. Create a lazy init property thats holds the StrategySerialFormat for your CustomYamlFormat:

```kotlin
val CustomYamlStrategyFormat by lazy {
  StrategyStringFormatter(CustomYamlFormat, ReverseBooleanStrategy /* encoder */, ReverseBooleanStrategy /* decoder */)
}
```

And ready! You already have your own custom strategy!

### Using your custom strategy
Now that you've created your own strategy, you might be wondering how to use it, right? Well, there are two ways:

### Using your custom format when init a serial file:

```kotlin
val config = yaml(file, Settings::class, CustomYamlStrategyFormat)
```

### Using with a already existent serial file:

```kotlin
config.format = CustomYamlStrategyFormat
```


## Setup for development
The `serializer-framework` is in the central maven repository. Thus making things very easy!

```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation("io.github.uinnn:serializer-framework:1.3.1") // replaces with the most recent version
}
```

### Final notes
The `serializer-framework` **NOT** contains the kotlin runtime, kotlin serialization and others needed to run this framework,
so you should implement them directly in your project.
To make your life easier, here is all the implementation of the libraries needed to run the framework:

```gradle
plugins {
  kotlin("jvm") version "1.5.20"
  kotlin("plugin.serialization") version "1.5.20"
}

dependencies {
  implementation(kotlin("stdlib-jdk8")) // the kotlin std lib with jdk8
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.20") // the kotlin reflect 1.5.20
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2") // the kotlin serialization core 1.2.2
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2") // the kotlin json serialization 1.2.2
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.2.2") // the kotlin protobuf serialization 1.2.2
  implementation("com.charleskorn.kaml:kaml:0.34.0") // the yaml serialization 0.34.0
}
```
