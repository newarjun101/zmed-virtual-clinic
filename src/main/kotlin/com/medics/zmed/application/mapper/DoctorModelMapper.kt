package com.medics.zmed.application.mapper

import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.domain.model.request_model.DoctorRequestModel
import com.medics.zmed.domain.model.response_model.DoctorResponseModel
import com.medics.zmed.persistance.entity.DoctorDao


fun DoctorDao.toDoctorResponseModel() : DoctorResponseModel {

    return DoctorResponseModel(
        id = this.id,
        name = this.name,
        rating = this.rating,
        totalAppointment = this.totalAppointment,
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address,
        township = this.township,
        state = this.state,
        country = this.country,
        yearOfExperience = this.yearOfExperience,
        description = this.description,
        imageUrl = this.imageUrl,
        email = this.email,
        primaryPhoneNumber = this.primaryPhoneNumber,
        secondaryPhoneNumber = this.secondaryPhoneNumber
    )
}

fun DoctorRequestModel.toDoctorResponseModel() : DoctorDao {

    return DoctorDao(
        name = this.name,
        rating = this.rating,
        totalAppointment = this.totalAppointment,
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address,
        township = this.township,
        state = this.state,
        country = this.country,
        yearOfExperience = this.yearOfExperience,
        description = this.description,
        imageUrl = this.imageUrl,
        email = this.email,
        primaryPhoneNumber = this.primaryPhoneNumber,
        secondaryPhoneNumber = this.secondaryPhoneNumber
    )
}