package com.medics.zmed.persistance.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GenerationType
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import java.time.LocalDateTime


@Entity
@Table(name = "appointment")
data class AppointmentDao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "doctor_id", nullable = false)
    val doctorId: Long,
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "appointment_created_date", nullable = false)
    val appointmentCreatedDate: LocalDateTime,
    @Column(name = "appointment_updated_date", nullable = true)
    val appointmentUpdatedDate: LocalDateTime? =null,
    @Column(name = "appointment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    val appointmentStatus: AppointmentStatus,
    val note: String? = null,
    @Column(name = "appointment_date", nullable = false)
    val appointmentDate: String,
    @Column(name = "appointment_time", nullable = false)
    val appointmentTime: String,
)

enum class AppointmentStatus {
    PENDING, CONFIRM, CANCEL, DONE, EXPIRE
}