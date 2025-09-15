package com.medics.zmed.persistance.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column

@Entity
@Table(name = "users")
data class UserDao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name : String,
    @Column(unique = true)
    val email : String,
    @JsonIgnore
    val password : String,
    val accessToken : String? = null,
    val refreshToken : String? = null,

)