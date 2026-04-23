package ru.sbrf.dab2c.platform.it

import ru.sbrf.dab2c.platform.greeter.api.GreeterService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest(classes = [GreeterServiceIntegrationTest.TestApp::class])
class GreeterServiceIntegrationTest {

    @SpringBootApplication
    class TestApp

    @Autowired
    lateinit var greeterService: GreeterService

    @Test
    fun `auto-configures GreeterService with default greeting`() {
        assertThat(greeterService.greet("world")).isEqualTo("hello, world!")
    }
}

@SpringBootTest(classes = [GreeterServiceIntegrationTest.TestApp::class])
@TestPropertySource(properties = ["sample.greeter.greeting=hola"])
class GreeterServicePropertyBindingTest {

    @Autowired
    lateinit var greeterService: GreeterService

    @Test
    fun `binds sample_greeter_greeting from configuration`() {
        assertThat(greeterService.greet("world")).isEqualTo("hola, world!")
    }
}
