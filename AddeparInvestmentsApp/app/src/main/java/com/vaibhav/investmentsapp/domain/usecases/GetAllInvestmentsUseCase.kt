package com.vaibhav.investmentsapp.domain.usecases

import com.vaibhav.investmentsapp.domain.apis.InvestmentsRepositoryApi
import javax.inject.Inject

/**
 * Use case to get all investments.
 */
class GetAllInvestmentsUseCase @Inject constructor(
    private val investmentsRepository: InvestmentsRepositoryApi,
) {

    suspend operator fun invoke() = investmentsRepository.getAllInvestments()
}