package com.vaibhav.investmentsapp.presentation.screens.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vaibhav.investmentsapp.presentation.theme.PurpleGrey80
import com.vaibhav.investmentsapp.presentation.screens.details.DetailsUiState.Error
import com.vaibhav.investmentsapp.presentation.screens.details.DetailsUiState.Loading
import com.vaibhav.investmentsapp.presentation.screens.details.DetailsUiState.Success

/**
 * Investment details screen composable.
 */
@Composable
fun InvestmentDetailsScreen(
    navController: NavController,
    investmentName: String,
    viewModel: InvestmentDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchInvestmentDetails(investmentName)
    }

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
                InvestmentDetailsContent(
                    data = (uiState.value as Success).data,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }

}

@Composable
fun InvestmentDetailsContent(
    data: InvestmentDetailsUiModel,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onBackPressed() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                text = data.name.value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                DetailItemWithTitle(
                    label = data.name.label,
                    data = data.name.value,
                )
                data.ticker?.let {
                    DetailItemWithTitle(
                        label = data.ticker.label,
                        data = data.ticker.value,
                    )
                }
                DetailItemWithTitle(
                    label = data.principal.label,
                    data = data.principal.value,
                )
                DetailItemWithTitle(
                    label = data.value.label,
                    data = data.value.value,
                )

                DetailsSection(
                    details = data.details
                )
            }
        }
    }
}

@Composable
private fun DetailsSection(
    modifier: Modifier = Modifier,
    details: DetailsEntry
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            text = details.label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )

        Text(
            modifier = Modifier,
            text = details.value,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun DetailItemWithTitle(
    modifier: Modifier = Modifier,
    label: String,
    data: String,
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Text(
                modifier = Modifier,
                text = data,
                fontSize = 16.sp,
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = PurpleGrey80
        )
    }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    InvestmentDetailsContent(
        data = InvestmentDetailsUiModel(
            name = DetailsEntry("Name", "Gold"),
            ticker = DetailsEntry("ticker", "ticker"),
            value = DetailsEntry("value", "value"),
            principal = DetailsEntry("principal", "principal"),
            details = DetailsEntry("details", "details")
        ),
        onBackPressed = {}
    )
}