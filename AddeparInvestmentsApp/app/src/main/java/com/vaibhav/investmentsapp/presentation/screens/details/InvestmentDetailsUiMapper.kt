package com.vaibhav.investmentsapp.presentation.screens.details

import com.vaibhav.investmentsapp.domain.models.Investment
import javax.inject.Inject

/**
 * Converts [Investment] to [InvestmentDetailsUiModel].
 */
class InvestmentDetailsUiMapper @Inject constructor() {
    fun convertToUiData(
        data: Investment,
    ): InvestmentDetailsUiModel {
        return InvestmentDetailsUiModel(
            name = DetailsEntry("Investment Name", data.name),
            ticker = data.ticker?.let { DetailsEntry("Ticker", it) },
            value = DetailsEntry("Value Invested", data.value),
            principal = DetailsEntry("Principal Invested", data.principal),
            details = DetailsEntry("Details", data.details),
        )
    }
}