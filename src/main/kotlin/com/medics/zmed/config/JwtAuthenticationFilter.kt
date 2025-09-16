package com.medics.zmed.config

import com.medics.zmed.common.exceptions.model.ErrorResponse
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.component.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tools.jackson.databind.ObjectMapper

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            try {
                val username = jwtUtil.getEmailFromToken(token)

                val isRefreshEndpoint = request.requestURI.startsWith("/api/v1/auth/process/refresh-token")


                val isValid = jwtUtil.isTokenValid(token, isRefreshToken = isRefreshEndpoint)

                if (isValid) {
                    val auth = UsernamePasswordAuthenticationToken(username, null, listOf())
                    SecurityContextHolder.getContext().authentication = auth
                    val userId = jwtUtil.getUserId(token)

                    // Put userId in request attributes (accessible anywhere later)
                    request.setAttribute("userId", userId)
                } else {
                    writeUnauthorizedResponse(response, "Invalid or wrong type of token")
                    return
                }
            } catch (ex: Exception) {
                writeUnauthorizedResponse(response, "Invalid or expired token")
                return
            }
        } else if (!request.requestURI.startsWith("/api/v1/auth/process/refresh-token")) {
            writeUnauthorizedResponse(response, "Missing Authorization header")
            return
        }

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {


        val uri = request.requestURI
        return uri.startsWith("/api/v1/auth/process/")
                || uri.startsWith("/swagger-ui/")
                || uri.startsWith("/v3/api-docs")


    }

    private fun writeUnauthorizedResponse(response: HttpServletResponse, description: String) {
        val errorResponse = ResponseModel(
            errorResponse = ErrorResponse(
                message = "Unauthorized request",
                status = HttpStatus.UNAUTHORIZED.value(),
                description = description
            )
        )
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.use { writer ->
            writer.write(objectMapper.writeValueAsString(errorResponse))
        }
    }
}
