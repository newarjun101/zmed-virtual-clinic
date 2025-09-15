package com.medics.zmed.persistance.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id


@Entity
@Table(name = "doctor")
data class DoctorDao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? =null,
    val name : String,
    val rating : Double? =null,
    val totalAppointment : Long? = null,
    val latitude : Double? = null,
    val longitude : Double? = null,
    val address : String? = null,
    val township : String? = null,
    val state : String? = null,
    val country : String? = null,
    val yearOfExperience : Double? = null,
    val description : String? = null,
    val imageUrl : String? = null,
    @Column(unique = true)
    val email : String? = null,
    val primaryPhoneNumber: String? = null,
    val secondaryPhoneNumber: String? = null,

)