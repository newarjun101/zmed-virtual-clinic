package com.medics.zmed.domain.model.request_model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class AppointmentRequestModel(
    @JsonProperty( "doctor_id")
    val doctorId : Long? = null,
    @JsonProperty( "user_id",)
    val userId : Long? = null,
    @JsonProperty( "appointment_created-date",)
    val appointmentCreatedDate : LocalDateTime?= null,
    @JsonProperty( "appointment_updated-date",)
    val appointmentUpdatedDate : LocalDateTime?= null,
    @JsonProperty( "appointment_status",)
    val appointmentStatus: String? = null,
    val note : String? = null,
    @JsonProperty( "appointment_date",)
    val appointmentDate : String? = null,
    @JsonProperty("appointment_time")
    val appointmentTime: String? = null
)
