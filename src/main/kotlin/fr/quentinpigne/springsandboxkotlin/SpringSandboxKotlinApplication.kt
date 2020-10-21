package fr.quentinpigne.springsandboxkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringSandboxKotlinApplication

fun main(args: Array<String>) {
	runApplication<SpringSandboxKotlinApplication>(*args)
}
