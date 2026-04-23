package com.example.sample.it

import com.example.sample.api.MyService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest(classes = [MyServiceIntegrationTest.TestApp::class])
class MyServiceIntegrationTest {

    @SpringBootApplication
    class TestApp

    @Autowired
    lateinit var myService: MyService

    @Test
    fun `auto-configures MyService with default greeting`() {
        assertThat(myService.greet("world")).isEqualTo("hello, world!")
    }
}

@SpringBootTest(classes = [MyServiceIntegrationTest.TestApp::class])
@TestPropertySource(properties = ["sample.greeting=hola"])
class MyServicePropertyBindingTest {

    @Autowired
    lateinit var myService: MyService

    @Test
    fun `binds sample_greeting from configuration`() {
        assertThat(myService.greet("world")).isEqualTo("hola, world!")
    }
}
