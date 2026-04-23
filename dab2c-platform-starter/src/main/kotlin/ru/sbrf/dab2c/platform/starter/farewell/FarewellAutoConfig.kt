package ru.sbrf.dab2c.platform.starter.farewell

import ru.sbrf.dab2c.platform.farewell.api.FarewellService
import ru.sbrf.dab2c.platform.farewell.impl.FarewellServiceImpl
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(FarewellProps::class)
@ConditionalOnProperty(prefix = "sample.farewell", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class FarewellAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    fun farewellService(props: FarewellProps): FarewellService = FarewellServiceImpl(props.farewell)
}
