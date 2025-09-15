package com.medics.zmed.application.service

import com.medics.zmed.application.mapper.response_model_mapper.toPaginationSuccessModel
import com.medics.zmed.application.mapper.toAppointmentDao
import com.medics.zmed.application.mapper.toAppointmentResponseModel
import com.medics.zmed.application.mapper.toDoctorResponseModel
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.common.exceptions.model.ResponsePaginationModel
import com.medics.zmed.domain.model.request_model.AppointmentRequestModel
import com.medics.zmed.domain.repository.AppointmentRepository
import com.medics.zmed.domain.repository.AuthRepository
import com.medics.zmed.domain.repository.DoctorRepository
import org.springframework.stereotype.Service

@Service
class AppointmentService (
    private val appointmentService: AppointmentRepository,
    private val doctorRepository: DoctorRepository,
    private val authRepository: AuthRepository
) {



    fun onCreateAppointment(appointRequestModel: AppointmentRequestModel) : ResponseModel   {

        println("arjun ==> ${appointRequestModel}")
        if(appointRequestModel.appointmentTime.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid appointment time")
        }
        if(appointRequestModel.appointmentDate.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid appointment date")
        }
        if(!doctorRepository.isDoctorValid(appointRequestModel.doctorId)) {
           throw IllegalArgumentException("Doctor id not found")
        }

        if(!authRepository.isUserExit(appointRequestModel.doctorId)) {
            throw IllegalArgumentException("Doctor id not found")
        }

        val appointmentDao = appointRequestModel.toAppointmentDao()

        if(appointmentService.isDoctorAlreadyBooked(
                appointmentDao = appointmentDao
            )) {
            throw IllegalArgumentException("Doctor is already booked in the selected time")
        }
        if(appointmentService.isAppointmentAlreadyExit(
                appointmentDao = appointmentDao
            )) {
            throw IllegalArgumentException("You have already exit in selected time")
        }

        val isAppointCreated = appointmentService.createAppointment(appointRequestModel.toAppointmentDao())
        if(isAppointCreated) {
            return ResponseModel(message = "Success", data = true)
        }

        throw IllegalArgumentException("Something go wrong with server")
         //   return appointmentService.createAppointment(appointRequestModel.toAppointmentDao())
    }

    fun getAllAppointmentByUserId(userId: Long?= null,pageNumber: Int, pageSize: Int): ResponsePaginationModel {

        if (userId == null || userId < 0) {
            throw IllegalArgumentException("User id can't be null or less than zero")
        }
        val data = appointmentService.getAllAppointmentByUserId(userId=userId, pageNumber = pageNumber, pageSize =  pageSize)
        val appointmentResponseModel = data.content.map { it.toAppointmentResponseModel() }
        return data.toPaginationSuccessModel(appointmentResponseModel)

    }
}