package com.vaibhav.investmentsapp.data.models

import com.squareup.moshi.JsonClass
import com.vaibhav.investmentsapp.domain.models.Investment

/**
 * Response model for Investments API
 */
sealed class InvestmentsResponse {

    @JsonClass(generateAdapter = true)
    class Success(
        val investments: List<Investment>
    ): InvestmentsResponse()

    @JsonClass(generateAdapter = true)
    class Error(
        val error: String,
        val errorDescription: String,
    ): InvestmentsResponse()
}