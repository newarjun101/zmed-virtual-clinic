package com.medics.zmed.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }  // Disable CSRF for API

            .authorizeHttpRequests { auth ->
                auth.requestMatchers(
                    "/api/v1/auth/process/**",    // public auth endpoints
                    "/v3/api-docs/**",    // swagger OpenAPI JSON
                    "/swagger-ui/**",     // swagger UI static files
                    "/swagger-ui.html"
                ).permitAll() // make public
                //  auth.requestMatchers("/api/v1/category/**").permitAll() /// make public
                auth.anyRequest().authenticated()                  // ALL others need JWT
            }
            .formLogin { it.disable() }  // Disable default login page
            .httpBasic { it.disable() }  // Disable browser basic auth

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.exceptionHandling {
            // Let Spring MVC handle unsupported methods
            it.accessDeniedHandler { _, response, _ ->
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed")
            }
        }
        return http.build()
    }
}
