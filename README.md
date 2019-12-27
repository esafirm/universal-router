# Universal Router

> *Router for every ocassion ~*

Universal router comes with two flavor, the core module which basically a link router that can convert your URI to whatever you need. And the Android module which more opionated to how you can use it to help you solve your navigation problem

## Core

It basically consist of two router
1. `SimpleRouter` which route `Any` type of object to anything you need
2. `UrlRouter` which takes URI instead of object

### Some Examples

```kotlin
// Define router
class StringRouter : UrlRouter<String>() {

    init {
        addEntry("nolambda://test/{a}/{b}", "https://test/{a}/{b}") {
            val first = it["a"]
            val second = it["b"]
            "$second came to the wrong neighborhood $first"
        }
    }
}

// Call router
// This will return string "you can to the wrong neighborhood yo"
StringRouter().resolve("nolambda://test/yo/you") 
```

For more sample, plese look at the `sample` module or the test i created. 

## Android

Basically with just the `core` module you already can have a navigation system in your modular structured application (think dynamic module use case). The easiest way would be creating a `Singleton` router in your "core" module and then add entries in every other module, but this can get quite messy sometimes

So this is when the android router module comes in

```kotlin

```

## License 

MIT @ Esa Firman


