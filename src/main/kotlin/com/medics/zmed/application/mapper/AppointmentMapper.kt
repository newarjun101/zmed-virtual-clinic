package com.medics.zmed.application.mapper

import com.medics.zmed.domain.model.request_model.AppointmentRequestModel
import com.medics.zmed.domain.model.response_model.AppointmentResponseModel
import com.medics.zmed.persistance.entity.AppointmentDao
import com.medics.zmed.persistance.entity.AppointmentStatus
import java.time.LocalDateTime

fun AppointmentRequestModel.toAppointmentDao() : AppointmentDao {
    return AppointmentDao(
        doctorId = this.doctorId!!,
        userId = this.userId!!,
        appointmentCreatedDate = LocalDateTime.now(),
        appointmentUpdatedDate = LocalDateTime.now(),
        appointmentStatus = AppointmentStatus.PENDING,
        note = this.note,
        appointmentDate = this.appointmentDate!!,
        appointmentTime = this.appointmentTime !!
    )
}


fun AppointmentDao.toAppointmentResponseModel() : AppointmentResponseModel {
    return AppointmentResponseModel(
        doctorId = this.doctorId,
        userId = this.userId,
        id = this.id,
        appointmentCreatedDate = LocalDateTime.now(),
        appointmentUpdatedDate = LocalDateTime.now(),
        appointmentStatus = AppointmentStatus.PENDING.name.uppercase(),
        note = this.note,
        appointmentDate = this.appointmentDate,
        appointmentTime = this.appointmentTime
    )
}