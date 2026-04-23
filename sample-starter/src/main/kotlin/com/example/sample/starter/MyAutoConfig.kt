package com.example.sample.starter

import com.example.sample.api.MyService
import com.example.sample.impl.MyServiceImpl
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(MyProps::class)
@ConditionalOnProperty(prefix = "sample", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class MyAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    fun myService(props: MyProps): MyService = MyServiceImpl(props.greeting)
}
