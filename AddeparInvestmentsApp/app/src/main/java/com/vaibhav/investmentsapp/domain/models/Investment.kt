package com.vaibhav.investmentsapp.domain.models

import com.squareup.moshi.JsonClass

/**
 * Model class for investment.
 */
@JsonClass(generateAdapter = true)
data class Investment(
    val name: String,
    val ticker: String?,
    val value: String,
    val principal: String,
    val details: String,
)
