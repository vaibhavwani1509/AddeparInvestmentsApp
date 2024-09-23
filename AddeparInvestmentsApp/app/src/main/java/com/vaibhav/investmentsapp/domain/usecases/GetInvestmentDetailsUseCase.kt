package com.vaibhav.investmentsapp.domain.usecases

import com.vaibhav.investmentsapp.domain.apis.InvestmentsRepositoryApi
import javax.inject.Inject

/**
 * Use case for getting investment details.
 */
class GetInvestmentDetailsUseCase @Inject constructor(
    private val investmentsRepository: InvestmentsRepositoryApi,
) {

    suspend operator fun invoke(name: String) = investmentsRepository.getInvestmentDetails(name)
}