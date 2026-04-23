package ru.sbrf.dab2c.platform.starter.farewell

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "sample.farewell")
data class FarewellProps(
    @DefaultValue("true") val enabled: Boolean,
    @DefaultValue("goodbye") val farewell: String,
)
