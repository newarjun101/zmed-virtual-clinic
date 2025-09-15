package com.medics.zmed.application.mapper.response_model_mapper

import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.common.exceptions.model.ResponsePaginationModel
import org.springframework.data.domain.Page

fun Any.toSuccessModel(message: String? = null, description: String? = null): ResponseModel {

    return ResponseModel(data = this, message = message)

}

 fun <T : Any> Page<T>.toPaginationSuccessModel(data: Any? = null): ResponsePaginationModel {
    return ResponsePaginationModel(
        data = data,
        pageSize = pageable.pageSize, pageNumber = pageable.pageNumber, isLast = this.isLast, isFirst = isFirst
    )

}


fun  Any.toPaginationEmptySuccessModel(data: Any? = null): ResponsePaginationModel {
    return ResponsePaginationModel(
        data = data,
    )

}


fun <T, R> List<T>.mapTo(transform: (T) -> R): List<R> {
    return this.map(transform)
}
