package com.medics.zmed.persistance.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime


@Entity
@Table(name = "category")
data class CategoryDao(
    @Id
    val id: Long,
    val name: String,
    val description: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val imageUrl: String? = null
)