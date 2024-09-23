package com.vaibhav.investmentsapp.domain.apis

import com.vaibhav.investmentsapp.data.models.InvestmentsResponse
import com.vaibhav.investmentsapp.domain.models.Investment

/**
 * Interface for Investment repository.
 */
interface InvestmentsRepositoryApi {

    suspend fun getAllInvestments(): InvestmentsResponse

    suspend fun getInvestmentDetails(name: String): Investment?
}