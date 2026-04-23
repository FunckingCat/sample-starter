package ru.sbrf.dab2c.platform.greeter.impl

import ru.sbrf.dab2c.platform.greeter.api.GreeterService

class GreeterServiceImpl(private val greeting: String) : GreeterService {
    override fun greet(name: String): String = "$greeting, $name!"
}
