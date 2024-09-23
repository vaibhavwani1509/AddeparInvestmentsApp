package com.vaibhav.investmentsapp.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.investmentsapp.domain.usecases.GetInvestmentDetailsUseCase
import com.vaibhav.investmentsapp.presentation.screens.details.DetailsUiState.Error
import com.vaibhav.investmentsapp.presentation.screens.details.DetailsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * View model for investment details screen.
 */
@HiltViewModel
class InvestmentDetailsViewModel @Inject constructor(
    private val getInvestmentDetails: GetInvestmentDetailsUseCase,
    private val detailsUiDataConverter: InvestmentDetailsUiMapper,
    @Named("IODispatcher") private val ioDispatcher: CoroutineDispatcher,
    @Named("MainDispatcher") private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun fetchInvestmentDetails(name: String) {
        _uiState.value = DetailsUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            val investments = getInvestmentDetails(name)
            withContext(mainDispatcher) {
                _uiState.value = investments?.let {
                    Success(
                        data = detailsUiDataConverter.convertToUiData(it)
                    )
                } ?: Error(
                    errorDetails = "Error fetching investment details"
                )
            }
        }
    }
}

sealed class DetailsUiState {
    class Success(val data: InvestmentDetailsUiModel) : DetailsUiState()
    class Error(val errorDetails: String) : DetailsUiState()
    data object Loading : DetailsUiState()
}

data class InvestmentDetailsUiModel(
    val name: DetailsEntry,
    val ticker: DetailsEntry?,
    val value: DetailsEntry,
    val principal: DetailsEntry,
    val details: DetailsEntry,
)

data class DetailsEntry(
    val label: String,
    val value: String,
)