<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://img.shields.io/static/v1?style=for-the-badge&label=author&message=uinnn&color=informational"/>
</a>
<a href="https://github.com/uinnn/serializer-framework">
  <img align="center" src="https://img.shields.io/static/v1?style=for-the-badge&label=version&message=2.4.20&color=yellow"/>
</a>

# serializer-framework

### Note:
> This repository is a showcase from WalkMC Network serializer-framework.
> This will not work in your server.

### Supported Files 
* YAML
* JSON
* Protocol Buffers
* Minecraft NBT (.dat)
* Own Custom implementation of NBT

## Showcase:

### Modeling strucutre:

```kt
@Serializable
data class Person(val name: String, val age: Int)

@Serializable
data class Family(members: MutableList<Person> = ArrayList())
```

### YAML

```kt
object FamilyConfig : YamlFile<Family>(file = file, model = Family())

// non-extending way:
val familyConfig = yml(file, Family())
```

### JSON

```kt
object FamilyConfig : JsonFile<Family>(file = file, model = Family())

// non-extending way:
val familyConfig = json(file, Family())
```

### Protocol buffers

```kt
object FamilyConfig : ProtobufFile<Family>(file = file, model = Family())

// non-extending way:
val familyConfig = protobuf(file, Family())
```

### NBT

```kt
object FamilyConfig : NBTFile<Family>(file = file, model = Family())

// non-extending way:
val familyConfig = nbt(file, Family())
```

### Own custom implementation of NBT

```kt
object FamilyConfig : TagFile<Family>(file = file, model = Family())

// non-extending way:
val familyConfig = tag(file, Family())
```

### Examples:

```kt
val family: Family = FamilyConfig.data
val members = family.members
members.forEach(::println)
```

```kt
FamilyConfig.onReload {} // add reload listener
FamilyConfig.onLoad {} // add load listener
FamilyConfig.onSave {} // add save listener

FamilyConfig.load() // loads from file
FamilyConfig.save() // save to file
```

```kt
val config = yml(file, Family()) {
  onReload {
    println("family config reloaded")
  }
}

val family get() = config.data
val familyMembers get() = family.members
```





