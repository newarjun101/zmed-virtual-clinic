package com.medics.zmed

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZmedApplication

fun main(args: Array<String>) {
	runApplication<ZmedApplication>(*args)
}
