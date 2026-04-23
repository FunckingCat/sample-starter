package ru.sbrf.dab2c.platform.it

import ru.sbrf.dab2c.platform.farewell.api.FarewellService
import ru.sbrf.dab2c.platform.greeter.api.GreeterService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [BothServicesWiredTest.TestApp::class])
class BothServicesWiredTest {

    @SpringBootApplication
    class TestApp

    @Autowired
    lateinit var greeterService: GreeterService

    @Autowired
    lateinit var farewellService: FarewellService

    @Test
    fun `both services auto-wire in the same context`() {
        assertThat(greeterService.greet("world")).isEqualTo("hello, world!")
        assertThat(farewellService.farewell("world")).isEqualTo("goodbye, world!")
    }
}
