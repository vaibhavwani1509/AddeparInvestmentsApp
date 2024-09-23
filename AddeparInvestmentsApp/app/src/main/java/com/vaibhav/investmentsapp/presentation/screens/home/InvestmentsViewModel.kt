package com.vaibhav.investmentsapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.investmentsapp.data.models.InvestmentsResponse
import com.vaibhav.investmentsapp.domain.models.Investment
import com.vaibhav.investmentsapp.domain.usecases.GetAllInvestmentsUseCase
import com.vaibhav.investmentsapp.presentation.screens.home.HomeUiState.Error
import com.vaibhav.investmentsapp.presentation.screens.home.HomeUiState.Loading
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
 * View model for home screen.
 */
@HiltViewModel
class InvestmentsViewModel @Inject constructor(
    private val getAllInvestments: GetAllInvestmentsUseCase,
    private val investmentsHomeUiMapper: InvestmentsHomeUiMapper,
    @Named("IODispatcher") private val ioDispatcher: CoroutineDispatcher,
    @Named("MainDispatcher") private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        _uiState.value = Loading
        viewModelScope.launch(ioDispatcher) {
            val uiData = when (val investments = getAllInvestments()) {
                is InvestmentsResponse.Error -> {
                    Error(investments.errorDescription)
                }

                is InvestmentsResponse.Success -> {
                    if (investments.investments.isEmpty()) {
                        getEmptyStateData()
                    } else {
                        HomeUiState.Success(
                            investmentsHomeUiMapper.convertToUiData(investments)
                        )
                    }
                }
            }

            withContext(mainDispatcher) {
                _uiState.value = uiData
            }
        }
    }

    private fun getEmptyStateData() =
        HomeUiState.Empty("No Investments Found!")
}

sealed class HomeUiState {
    class Empty(val emptyStateDescription: String) : HomeUiState()
    class Success(val data: HomeScreenUiModel) : HomeUiState()
    class Error(val errorDetails: String) : HomeUiState()
    data object Loading : HomeUiState()
}

data class HomeScreenUiModel(
    val title: String,
    val investments: List<Investment>,
)