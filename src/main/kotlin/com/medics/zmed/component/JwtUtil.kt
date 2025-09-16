package com.medics.zmed.component

import com.medics.zmed.application.mapper.commong_mapper.toJwtModelMapper
import com.medics.zmed.application.mapper.commong_mapper.writeValueAsString
import com.medics.zmed.common.extension.decryptAES
import com.medics.zmed.common.extension.encryptAES
import com.medics.zmed.domain.model.common_model.JwtModel
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secret: String
) {

    private val secretKey: Key = Keys.hmacShaKeyFor(secret.toByteArray())

    private val accessTokenValidity = 1000 * 60 * 60 * 24L * 7 //1000 * 60 * 60L

    // 1 hour
    private val refreshTokenValidity = 1000 * 60 * 60 * 24L * 7 // 7 days

    fun generateAccessToken(jwtModel: JwtModel): String {


        val now = Date()
        return Jwts.builder()
            .setSubject(jwtModel.writeValueAsString().encryptAES(secret))
            .claim("token_type", "access")
            .setIssuedAt(now)
            .setExpiration(Date(now.time + accessTokenValidity))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(jwtModel: JwtModel): String {
        val now = Date()
        return Jwts.builder()
            .setSubject(jwtModel.writeValueAsString().encryptAES(secret))
            .claim("token_type", "refresh")
            .setIssuedAt(now)
            .setExpiration(Date(now.time + refreshTokenValidity))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getDecryptedJwtParser(token: String): String {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        val encryptedJwtModel = claims.subject
        val jwtModelString = encryptedJwtModel.decryptAES(secret)

        print("arjun jwt ===> $jwtModelString")
        return jwtModelString
    }

    fun getEmailFromToken(token: String): String {
        return getDecryptedJwtParser(token).toJwtModelMapper().email
    }

    fun getJwtModel(token: String): JwtModel {
        return getDecryptedJwtParser(token).toJwtModelMapper()
    }

    fun getUserId(token: String): Long {
        return getDecryptedJwtParser(token).toJwtModelMapper().userId
    }

    fun isTokenValid(token: String, isRefreshToken: Boolean): Boolean {
        return try {

            val expectedTokenType: String = if (isRefreshToken) "refresh" else "access"

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
