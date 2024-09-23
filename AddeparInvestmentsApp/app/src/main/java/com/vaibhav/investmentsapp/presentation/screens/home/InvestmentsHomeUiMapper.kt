package com.vaibhav.investmentsapp.presentation.screens.home

import com.vaibhav.investmentsapp.data.models.InvestmentsResponse
import com.vaibhav.investmentsapp.data.models.InvestmentsResponse.Success
import javax.inject.Inject

/**
 * Converts [InvestmentsResponse.Success] to [HomeScreenUiModel].
 */
class InvestmentsHomeUiMapper @Inject constructor() {
    fun convertToUiData(
        data: Success,
    ): HomeScreenUiModel {
        return HomeScreenUiModel(
            title = "Hello User!",
            investments = data.investments,
        )
    }
}