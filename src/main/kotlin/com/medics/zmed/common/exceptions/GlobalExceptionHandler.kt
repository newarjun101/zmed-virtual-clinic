package com.medics.zmed.common.exceptions

import com.medics.zmed.common.exceptions.customExceptions.InvalidTokenException
import com.medics.zmed.common.exceptions.customExceptions.UnauthorizedException
import com.medics.zmed.common.exceptions.customExceptions.UserNotFoundException
import com.medics.zmed.common.exceptions.model.ErrorResponse
import com.medics.zmed.common.exceptions.model.ResponseModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.HttpRequestMethodNotSupportedException

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ResponseModel> {
        val error = ResponseModel(errorResponse = ErrorResponse(
            message =  "Invalid request",
            status = HttpStatus.BAD_REQUEST.value(),
            description = ex.message?:""
        ))
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<ResponseModel> {
        val error = ResponseModel(errorResponse = ErrorResponse(
            message =  "User not found",
            status = HttpStatus.BAD_REQUEST.value(),
            description = ex.message?:""
        ))
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorize(ex: UnauthorizedException): ResponseEntity<ResponseModel> {
        val error =ResponseModel (errorResponse = ErrorResponse(
            message =  "Unauthorized request",
            status = HttpStatus.UNAUTHORIZED.value(),
            description = ex.message?:""
        ))
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)

    }

    @ExceptionHandler(NullPointerException::class)
    fun handleBodyNull(ex: NullPointerException) : ResponseEntity<ResponseModel> {
        val error = ResponseModel (errorResponse = ErrorResponse(
            message =  "Request body cannot be blank",
            status = HttpStatus.BAD_REQUEST.value(),
            description = ex.message?:""
        ))
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(ex: HttpRequestMethodNotSupportedException):  ResponseEntity<ResponseModel>  {
        val body = ResponseModel (errorResponse = ErrorResponse(
            message =  "Method Not Allowed",
            status = HttpStatus.METHOD_NOT_ALLOWED.value(),
            description = ex.message?:""
        ))
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body)
    }
  /*  @ExceptionHandler(InvalidTokenException::class)
    fun handleUnauthorize(ex: InvalidTokenException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = "User not found",
            status = HttpStatus.UNAUTHORIZED.value(),
            description = ex.message?:""
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)

    }
*/





    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFound(ex: NoHandlerFoundException): ResponseEntity<ResponseModel> {
        val error = ResponseModel(errorResponse = ErrorResponse(
            message = "Not Found",
            status = HttpStatus.NOT_FOUND.value(),
            description = "The requested URL ${ex.requestURL} was not found on this server"
        ))
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }


}