package com.medics.zmed.controller

import com.medics.zmed.application.mapper.response_model_mapper.toSuccessModel
import com.medics.zmed.application.service.DoctorService
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.common.exceptions.model.ResponsePaginationModel
import com.medics.zmed.domain.model.request_model.DoctorRequestModel
import com.medics.zmed.domain.model.response_model.DoctorResponseModel
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag


@RestController
@RequestMapping("api/v1/doctor")
@Tag(name = "Doctor Related API", description = "Endpoints to manage doctors")
class DoctorController(private val doctorService: DoctorService) {


    @GetMapping("/get-popular-doctor")
    fun getPopularDoctor(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ResponseModel> {
        val paginationModel = doctorService.getPopularDoctors(pageNumber = page, pageSize = size)
        return ResponseEntity.status(HttpStatus.OK).body(paginationModel.toSuccessModel())

    }

    @PostMapping("/save-doctors")
    fun saveDoctor(@RequestBody doctorRequestModel: List<DoctorRequestModel>? = null): ResponseEntity<ResponseModel> {

        if (doctorRequestModel.isNullOrEmpty()) {
            throw IllegalArgumentException("Body cannot be null or blank")
        }
        val responseModel = doctorService.saveDoctor(doctorRequestModel)
        return ResponseEntity.status(HttpStatus.OK).body(responseModel)

    }

    @GetMapping("/get-all-doctor")
    fun getAllDoctors(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ResponseModel> {
        val paginationModel = doctorService.getAllDoctor(pageNumber = page, pageSize = size)
        return ResponseEntity.status(HttpStatus.OK).body(paginationModel.toSuccessModel())

    }


}