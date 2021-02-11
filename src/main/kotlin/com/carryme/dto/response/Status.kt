package com.carryme.dto.response

class Status(val code: String? = null,
             val message: String? = null,
             val type: String? = null) {
    companion object {
        fun getSuccessStatus(): Status {
            return Status(
                    code = "200",
                    message = "OK",
                    type = "Success"
            )
        }

        fun getCreatedSuccessStatus(): Status {
            return Status(
                    code = "200",
                    message = "OK",
                    type = "Success"
            )
        }

        fun getErrorNotFoundStatus(): Status {
            return Status(
                    code = "404",
                    message = "Not Found",
                    type = "Error"
            )
        }

        fun getInternalServerErrorStatus(message: String?): Status {
            return Status(
                    code = "500",
                    message = message,
                    type = "Error"
            )
        }

        fun getBadRequestStatus(message: String?): Status {
            return Status(
                    code = "400",
                    message = message,
                    type = "Error"
            )
        }

        fun getUnauthorizedStatus(): Status {
            return Status(
                    code = "403",
                    type = "Error"
            )
        }
    }
}