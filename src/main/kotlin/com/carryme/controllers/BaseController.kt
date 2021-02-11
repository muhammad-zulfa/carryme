package com.carryme.controllers

import com.carryme.dto.response.BaseResponse
import com.carryme.dto.response.Status
import org.springframework.web.bind.annotation.RequestMapping


open class BaseController {
    fun successResponse(data: Any?): BaseResponse? {
        return successResponse(data, null)
    }

    fun successResponse(data: Any?, meta: Any?): BaseResponse? {
        return BaseResponse(
                data = data,
                status = Status.getCreatedSuccessStatus(),
                meta = (meta)
        )
    }

    fun createdResponse(data: Any?, meta: Any?): BaseResponse? {
        return BaseResponse(
                data = data,
                status = Status.getCreatedSuccessStatus(),
                meta = (meta)
        )
    }

    fun errorResponse(data: Any?, meta: Any?, errorStatus : Status): BaseResponse? {
        return BaseResponse(
                data = data,
                status = errorStatus,
                meta = (meta)
        )
    }

    fun errorNotFoundResponse(data: Any?, meta: Any?): BaseResponse? {
        return BaseResponse(
                data = data,
                status = Status.getErrorNotFoundStatus(),
                meta = (meta)
        )
    }

    fun errorUnauthorizedResponse(data: Any?, meta: Any?): BaseResponse? {
        return BaseResponse(
                data = data,
                status = Status.getUnauthorizedStatus(),
                meta = (meta)
        )
    }
}