package ru.sbrf.dab2c.platform.farewell.impl

import ru.sbrf.dab2c.platform.farewell.api.FarewellService

class FarewellServiceImpl(private val farewell: String) : FarewellService {
    override fun farewell(name: String): String = "$farewell, $name!"
}
