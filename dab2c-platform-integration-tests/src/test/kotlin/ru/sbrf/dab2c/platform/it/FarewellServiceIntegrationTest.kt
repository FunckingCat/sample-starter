package ru.sbrf.dab2c.platform.it

import ru.sbrf.dab2c.platform.farewell.api.FarewellService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest(classes = [FarewellServiceIntegrationTest.TestApp::class])
class FarewellServiceIntegrationTest {

    @SpringBootApplication
    class TestApp

    @Autowired
    lateinit var farewellService: FarewellService

    @Test
    fun `auto-configures FarewellService with default farewell`() {
        assertThat(farewellService.farewell("world")).isEqualTo("goodbye, world!")
    }
}

@SpringBootTest(classes = [FarewellServiceIntegrationTest.TestApp::class])
@TestPropertySource(properties = ["sample.farewell.farewell=adios"])
class FarewellServicePropertyBindingTest {

    @Autowired
    lateinit var farewellService: FarewellService

    @Test
    fun `binds sample_farewell_farewell from configuration`() {
        assertThat(farewellService.farewell("world")).isEqualTo("adios, world!")
    }
}
