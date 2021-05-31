package com.carryme.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigInteger

class DashboardDTO(
    @JsonProperty("sales_count")
    var salesCount: List<BigInteger>,
    @JsonProperty("sales_perday")
    var salesPerDay: List<BigInteger>,
    @JsonProperty("new_users")
    var newUsers: List<BigInteger>,
    @JsonProperty("date_axis")
    var dateAxis: MutableList<String>,
    )