package com.medics.zmed.component

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") secret: String
) {

    private val secretKey: Key = Keys.hmacShaKeyFor(secret.toByteArray())

    private val accessTokenValidity =  1000 * 60 * 60 * 24L * 7 //1000 * 60 * 60L
    // 1 hour
    private val refreshTokenValidity = 1000 * 60 * 60 * 24L * 7 // 7 days

    fun generateAccessToken(email: String): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(email)
            .claim("token_type", "access")
            .setIssuedAt(now)
            .setExpiration(Date(now.time + accessTokenValidity))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(email: String): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(email)
            .claim("token_type", "refresh")
            .setIssuedAt(now)
            .setExpiration(Date(now.time + refreshTokenValidity))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }
    fun getEmailFromToken(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun isTokenValid(token: String,isRefreshToken: Boolean): Boolean {
        return try {

            val expectedTokenType : String = if (isRefreshToken) "refresh" else "access"

            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body

            val isExpired = claims.expiration.before(Date())
            val tokenType = claims["token_type"]?.toString() ?: return false

            !isExpired && tokenType == expectedTokenType
        } catch (e: Exception) {
            false
        }
    }
}
