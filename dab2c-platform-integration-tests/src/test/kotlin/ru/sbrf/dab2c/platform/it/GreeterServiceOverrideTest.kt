package ru.sbrf.dab2c.platform.it

import ru.sbrf.dab2c.platform.greeter.api.GreeterService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@SpringBootTest(classes = [GreeterServiceOverrideTest.TestApp::class, GreeterServiceOverrideTest.OverrideConfig::class])
class GreeterServiceOverrideTest {

    @SpringBootApplication
    class TestApp

    @TestConfiguration
    class OverrideConfig {
        @Bean
        fun greeterService(): GreeterService = GreeterService { name -> "custom:$name" }
    }

    @Autowired
    lateinit var greeterService: GreeterService

    @Test
    fun `ConditionalOnMissingBean lets user bean win`() {
        assertThat(greeterService.greet("world")).isEqualTo("custom:world")
    }
}
