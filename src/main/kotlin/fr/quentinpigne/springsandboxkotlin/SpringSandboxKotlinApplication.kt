package fr.quentinpigne.springsandboxkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class SpringSandboxKotlinApplication

fun main(args: Array<String>) {
	runApplication<SpringSandboxKotlinApplication>(*args)
}
