package ru.sbrf.dab2c.platform.starter.greeter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "sample.greeter")
data class GreeterProps(
    @DefaultValue("true") val enabled: Boolean,
    @DefaultValue("hello") val greeting: String,
)
