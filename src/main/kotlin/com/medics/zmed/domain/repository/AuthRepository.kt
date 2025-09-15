package com.medics.zmed.domain.repository

import com.medics.zmed.persistance.entity.UserDao
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

interface AuthRepository {
    fun onSignUp(user: UserDao): UserDao
    fun findByEmail(email: String): UserDao?

    fun findById(id: Long): UserDao?
    fun isUserExit(id: Long?=null): Boolean


    fun refreshToken(email: String,refreshToken: String,accessToken: String): Int?




}


@Repository
class AuthRepositoryImpl(
    private val springAuthRepository: SpringAuthRepository
) : AuthRepository {

    override fun onSignUp(user: UserDao): UserDao {
        return springAuthRepository.save(user)
    }

    override fun findByEmail(email: String): UserDao? {
        return springAuthRepository.findByEmail(email)
    }

    override fun findById(id: Long): UserDao? {
        return springAuthRepository.findById(id).getOrNull()
    }

    override fun isUserExit(id: Long?): Boolean {
        if(id== null) {
            throw IllegalArgumentException("User not found")
        }
        return springAuthRepository.existsById(id)
    }

    override fun refreshToken(email: String,refreshToken: String,accessToken: String): Int? {
        return springAuthRepository.updateTokens(
            email = email,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }


}

interface SpringAuthRepository : JpaRepository<UserDao, Long> {
    fun findByEmail(email: String): UserDao?

    @Modifying
    @Transactional
    @Query("UPDATE UserDao u SET u.accessToken = :accessToken, u.refreshToken = :refreshToken WHERE u.email = :email")
    fun updateTokens(
        @Param("email") email: String,
        @Param("accessToken") accessToken: String,
        @Param("refreshToken") refreshToken: String
    ): Int

}