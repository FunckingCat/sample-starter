package com.example.sample.starter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "sample")
data class MyProps(
    @DefaultValue("true") val enabled: Boolean,
    @DefaultValue("hello") val greeting: String,
)
