package ru.sbrf.dab2c.platform.starter.greeter

import ru.sbrf.dab2c.platform.greeter.api.GreeterService
import ru.sbrf.dab2c.platform.greeter.impl.GreeterServiceImpl
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(GreeterProps::class)
@ConditionalOnProperty(prefix = "sample.greeter", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class GreeterAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    fun greeterService(props: GreeterProps): GreeterService = GreeterServiceImpl(props.greeting)
}
