package com.vaibhav.investmentsapp.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vaibhav.investmentsapp.domain.models.Investment
import com.vaibhav.investmentsapp.presentation.AppScreens
import com.vaibhav.investmentsapp.presentation.screens.home.HomeUiState.Empty
import com.vaibhav.investmentsapp.presentation.screens.home.HomeUiState.Error
import com.vaibhav.investmentsapp.presentation.screens.home.HomeUiState.Loading
import com.vaibhav.investmentsapp.presentation.screens.home.HomeUiState.Success

/**
 * Home screen composable.
 */
@Composable
fun InvestmentsHomeScreen(
    navController: NavController,
    viewModel: InvestmentsViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        when (uiState.value) {
            is Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp)
                )
            }

            is Error -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = (uiState.value as Error).errorDetails
                )
            }

            is Success -> {
                HomeContent(
                    data = (uiState.value as Success).data
                ) {
                    navController.navigate(AppScreens.Details.createRoute(it.name))
                }
            }

            is Empty -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = (uiState.value as Empty).emptyStateDescription
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    data: HomeScreenUiModel,
    onInvestmentClicked: (Investment) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleText(data.title)

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(data.investments) {
                InvestmentItem(it, onInvestmentClicked)
            }
        }
    }
}

@Composable
private fun TitleText(
    title: String,
) {
    Text(
        text = title,
        textAlign = TextAlign.Center,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun InvestmentItem(
    investment: Investment,
    onInvestmentClicked: (Investment) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onInvestmentClicked(investment) }
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = investment.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            investment.ticker?.let {
                Text(
                    text = "{ $it }",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    HomeContent(
        data = HomeScreenUiModel(
            title = "Hello User!",
            investments = listOf(
                Investment(
                    name = "Name",
                    ticker = "Ticker",
                    value = "value",
                    principal = "principal",
                    details = "details"
                )
            )
        ),
        onInvestmentClicked = {}
    )
}