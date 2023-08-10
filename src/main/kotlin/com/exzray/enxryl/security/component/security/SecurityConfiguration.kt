package com.exzray.enxryl.security.component.security

import com.exzray.enxryl.security.service.token.TokenProviderService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository

@Configuration
class SecurityConfiguration(
    private val reactiveUserDetailsService: ReactiveUserDetailsService,
    private val tokenProviderService: TokenProviderService
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        entryPoint: ServerAuthenticationEntryPointImpl
    ): SecurityWebFilterChain {
        http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .logout { it.disable() }

        http
            .exceptionHandling {
                it.authenticationEntryPoint(entryPoint)
            }
            .authorizeExchange {
                it
                    .pathMatchers(HttpMethod.OPTIONS)
                    .permitAll()
            }
            .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
            .authorizeExchange {
                it
                    .pathMatchers(
                        "/api/v1/authentication/login",
                        "/api/v1/account/register-merchant",
                        "/api/v1/account/register-user"
                    )
                    .permitAll()
            }
            .authorizeExchange {
                it
                    .anyExchange()
                    .authenticated()
            }

        return http.build()
    }

    @Bean
    fun webFilter(): AuthenticationWebFilter {
        val authenticationWebFilter = AuthenticationWebFilter(tokenReactiveAuthenticationManager())
        authenticationWebFilter.setAuthenticationConverter(TokenAuthenticationConverter(reactiveUserDetailsService, tokenProviderService))
        authenticationWebFilter.setRequiresAuthenticationMatcher(TokenHeadersExchangeMatcher())
        authenticationWebFilter.setSecurityContextRepository(WebSessionServerSecurityContextRepository())

        return authenticationWebFilter
    }

    @Bean
    fun tokenReactiveAuthenticationManager(): TokenReactiveAuthenticationManager {
        return TokenReactiveAuthenticationManager(reactiveUserDetailsService, passwordEncoder())
    }
}
