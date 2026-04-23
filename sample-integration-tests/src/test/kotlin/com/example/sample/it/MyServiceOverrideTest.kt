package com.example.sample.it

import com.example.sample.api.MyService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@SpringBootTest(classes = [MyServiceOverrideTest.TestApp::class, MyServiceOverrideTest.OverrideConfig::class])
class MyServiceOverrideTest {

    @SpringBootApplication
    class TestApp

    @TestConfiguration
    class OverrideConfig {
        @Bean
        fun myService(): MyService = MyService { name -> "custom:$name" }
    }

    @Autowired
    lateinit var myService: MyService

    @Test
    fun `ConditionalOnMissingBean lets user bean win`() {
        assertThat(myService.greet("world")).isEqualTo("custom:world")
    }
}
