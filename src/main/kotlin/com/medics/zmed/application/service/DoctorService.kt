package com.medics.zmed.application.service

import com.medics.zmed.application.mapper.response_model_mapper.toPaginationSuccessModel
import com.medics.zmed.application.mapper.response_model_mapper.toSuccessModel
import com.medics.zmed.application.mapper.toDoctorResponseModel
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.common.exceptions.model.ResponsePaginationModel
import com.medics.zmed.domain.model.request_model.DoctorRequestModel
import com.medics.zmed.domain.repository.DoctorRepository
import com.medics.zmed.persistance.entity.DoctorDao
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class DoctorService(private val doctorRepository: DoctorRepository) {

    fun getAllDoctor(pageNumber: Int, pageSize: Int): ResponsePaginationModel {
        val data = doctorRepository.getAllDoctors(pageNumber, pageSize)
        val doctorResponseModelList = data.content.map { it.toDoctorResponseModel() }
        return data.toPaginationSuccessModel(doctorResponseModelList)

    }

    fun getPopularDoctors(pageNumber: Int, pageSize: Int): ResponsePaginationModel {
        val data = doctorRepository.getPopularDoctors(pageNumber, pageSize)
        val doctorResponseModelList = data.content.map { it.toDoctorResponseModel() }
        return data.toPaginationSuccessModel(doctorResponseModelList)

    }


    fun saveDoctor(doctorRequestModel: List<DoctorRequestModel>): ResponseModel {
        try {
            val data = doctorRepository.saveDoctor(doctorRequestModel.map { it.toDoctorResponseModel() })
            if (!data) {
                throw IllegalArgumentException("Unexpected error happen")
            }
            return ResponseModel(message = "Success", data = true)
        } catch (error: Exception) {

            throw IllegalArgumentException("Save Doctor Error")
        }


    }


}