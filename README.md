# link-router

In modular apps, we often can't address and initiate our screen component or whatever by their `Class` simply because our module we currently working on doesn't depends on the other module. To solve this problem we often create a "gateway"/"router"  for our classes. 

So what link-router does is just convert your URI to whatever you need

## Usage 

Define a `Router`

```kotlin
class StringRouter : Router<String>() {

    init {
        addEntry("nolambda://test/{a}/{b}", "https://test/{a}/{b}") {
            val first = it["a"]
            val second = it["b"]
            "$second came to the wrong neighborhood $first"
        }
    }

    override fun goTo(uri: String) {
        val text = resolve(uri)
        println(text)
    }
}
```

Get what you need 

```kotlin
stringRouter.resolve("nolambda://test/bro/you") shouldBe "you came to the wrong neighborhood bro"
```

For more sample, plese look at the `sample` module or the test i created. 

## License 

MIT @ Esa Firman


