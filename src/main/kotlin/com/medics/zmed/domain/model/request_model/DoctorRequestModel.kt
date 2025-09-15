package com.medics.zmed.domain.model.request_model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


data class DoctorRequestModel(

    val name : String,
    val rating : Double? =null,
    @JsonProperty("total_appointment")
    val totalAppointment : Long? = null,
    val latitude : Double? = null,
    val longitude : Double? = null,
    val address : String? = null,
    val township : String? = null,
    val state : String? = null,
    val country : String? = null,
    @JsonProperty("year_of_experience")
    val yearOfExperience : Double? = null,
    val description : String? = null,
    @JsonProperty("image_url")
    val imageUrl : String? = null,
    val email : String? = null,
    @JsonProperty("primary_phone_number")
    val primaryPhoneNumber: String? = null,
    @JsonProperty("secondary_phone_number")
    val secondaryPhoneNumber: String? = null,
)