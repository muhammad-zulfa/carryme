package com.carryme.dto.requests

import com.fasterxml.jackson.annotation.JsonProperty

class RouteRequestDto {

    var id: Long? = null

    var name: String? = null

    var origin: Long? = null

    var destination: Long? = null

    var eta: Int? = null

    var price: Int? = null

    @JsonProperty("operation_routes_id")
    var operationRoutesId: Long? = null

    @JsonProperty("assurance_fee")
    var assuranceFee: Int? = null

    @JsonProperty("retribution_fee")
    var retributionFee: Int? = null
}