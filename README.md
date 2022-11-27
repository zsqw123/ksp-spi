# KSP SPI

一个基于 ksp 实现的 spi，但它并没有达到理想中我要的样子，无法实现模块间的解耦。

## 使用示例

首先我们编写一个 SPI 的实现：

```kotlin
@Spi
interface UserService {
    val name: String
    fun login(): Boolean
}
```

然后我们可以在其他模块实现这个接口，对其有不同实现，最后通过 `SpiLoader.getAllImpl` 来拿到其全部实现，正如下面的示例代码：
```kotlin
// module 1
@SpiImpl
class AnonymousUserService : UserService {
    override val name: String = "Anonymous"
    override fun login(): Boolean = false
}

// module 2
@SpiImpl
class BobUserService : UserService {
    override val name: String = "Bob"
    override fun login(): Boolean = true
}

// main module
val allUsers = SpiLoader.getAllImpl<UserService>()
val outputContent = allUsers.joinToString("\n") {
    "${it.name}, login success: ${it.login()}"
}
println(outputContent)
```

它将会输出：
```text
Bob, login success: true
Anonymous, login success: false
```

但它是有局限的，基于 ksp 的它虽然实现了简单的服务查找，但是没能实现解耦，因为其他模块在编译的时候也是需要依赖实现类的，
如果不依赖编译则无法通过，所以这是一种不完美的方案。