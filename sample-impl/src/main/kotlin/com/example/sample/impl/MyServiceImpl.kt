package com.example.sample.impl

import com.example.sample.api.MyService

class MyServiceImpl(private val greeting: String) : MyService {
    override fun greet(name: String): String = "$greeting, $name!"
}
