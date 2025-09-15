package com.medics.zmed.domain.model.response_model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class DoctorResponseModel(

    val id: Long? = null,
    val name: String,
    val rating: Double? = null,
    @JsonIgnore
    @JsonProperty("total_appointment")
    val totalAppointment: Long? = null,
    @JsonIgnore
    val latitude: Double? = null,
    @JsonIgnore
    val longitude: Double? = null,
    val address: String? = null,
    val township: String? = null,
    val state: String? = null,
    val country: String? = null,
    @JsonProperty("year_of_experience")
    val yearOfExperience: Double? = null,
    val description: String? = null,
    @JsonProperty("image_url")
    val imageUrl: String? = null,
    val email: String? = null,
    @JsonProperty("primary_phone_number")
    val primaryPhoneNumber: String? = null,
    @JsonProperty("secondary_phone_number")
    val secondaryPhoneNumber: String? = null,
)