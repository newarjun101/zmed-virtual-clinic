package com.medics.zmed.application.mapper

import com.medics.zmed.domain.model.request_model.CategoryRequestModel
import com.medics.zmed.domain.model.response_model.CategoryResponseModel
import com.medics.zmed.persistance.entity.CategoryDao
import java.time.LocalDateTime


fun CategoryDao.toCategoryResponseModel() : CategoryResponseModel = CategoryResponseModel(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl
)

fun List<CategoryDao>.toCategoryResponseModelList() : List<CategoryResponseModel> = this.map { it.toCategoryResponseModel() }


fun CategoryRequestModel.toCategoryDaoModel() : CategoryDao = CategoryDao(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)

fun List<CategoryRequestModel>.toCategoryDaoModelList() : List<CategoryDao> = this.map { it.toCategoryDaoModel() }