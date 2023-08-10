package com.exzray.enxryl.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebSecurity
class EnxrylSecurityApplication

fun main(args: Array<String>) {
	runApplication<EnxrylSecurityApplication>(*args)
}
