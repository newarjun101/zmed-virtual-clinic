package com.medics.zmed.controller

import com.medics.zmed.application.mapper.response_model_mapper.toSuccessModel
import com.medics.zmed.application.service.AppointmentService
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.domain.model.request_model.AppointmentRequestModel

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/v1/appointment")
@Tag(name = "Appointment Related API", description = "Endpoints to manage Appointments")
class AppointmentController(private val appointmentService: AppointmentService) {


    @PostMapping("/create-appointment")
    fun crateAppointment(@RequestBody body: AppointmentRequestModel? = null): ResponseEntity<ResponseModel> {
        if (body == null) {
            throw IllegalArgumentException("Request body is empty")
        }

        val responseModel = appointmentService.onCreateAppointment(body)
        return ResponseEntity.status(HttpStatus.OK).body(responseModel)

    }

    @GetMapping("/get-all-appointment-by-user-id")
    fun getAllAppointmentByUserId(
        @RequestParam(name = "user_id") userId: Long? = null,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ResponseModel> {
        val responseModel = appointmentService.getAllAppointmentByUserId(userId, pageSize = size, pageNumber = page)
        return ResponseEntity.status(HttpStatus.OK).body(responseModel.toSuccessModel())
    }

}