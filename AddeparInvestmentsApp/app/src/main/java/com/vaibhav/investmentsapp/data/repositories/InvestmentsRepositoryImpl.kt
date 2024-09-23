package com.vaibhav.investmentsapp.data.repositories

import android.content.Context
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.vaibhav.investmentsapp.data.models.InvestmentsResponse
import com.vaibhav.investmentsapp.data.models.InvestmentsResponse.Error
import com.vaibhav.investmentsapp.data.models.InvestmentsResponse.Success
import com.vaibhav.investmentsapp.domain.apis.InvestmentsRepositoryApi
import com.vaibhav.investmentsapp.domain.models.Investment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Implementation of [InvestmentsRepositoryApi]
 * - fetches investments from a JSON file.
 * - Checks the response for success and errors and returns an appropriate [InvestmentsResponse].
 */
class InvestmentsRepositoryImpl @Inject constructor(
    private val moshi: Moshi,
    @ApplicationContext private val context: Context,
    @Named("IODispatcher") private val ioDispatcher: CoroutineDispatcher,
) : InvestmentsRepositoryApi {

    private var cachedInvestments: List<Investment>? = null

    override suspend fun getAllInvestments(): InvestmentsResponse {
        return cachedInvestments?.let { Success(it) } ?: fetchInvestments()
    }

    override suspend fun getInvestmentDetails(name: String): Investment? {
        delay(500)
        val investments = getAllInvestments()
        return if (investments is Success) {
            investments.investments.firstOrNull { it.name == name }
        } else null
    }

    private suspend fun fetchInvestments(): InvestmentsResponse {
        delay(1000)
        return withContext(ioDispatcher) {
            try {
                val json = readJsonFile()

                val error: Error? = checkForErrorResponse(json)
                if (error != null) {
                    return@withContext error
                }

                val items: List<Investment>? = checkForSuccessResponse(json)

                if (items != null) {
                    cachedInvestments = items
                    Success(items)
                } else {
                    Error(
                        error = "MalformedJsonError",
                        errorDescription = "Error occurred while parsing your investments"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Error(
                    error = e.toString(),
                    errorDescription = "Something went wrong"
                )
            }
        }
    }

    private fun checkForSuccessResponse(json: String): List<Investment>? {
        val investments: Success? = try {
            val adapter = moshi.adapter(Success::class.java)
            adapter.fromJson(json)
        } catch (e: JsonDataException) {
            null
        }
        return investments?.investments
    }

    private fun checkForErrorResponse(json: String): Error? {
        val error: Error? = try {
            val adapter = moshi.adapter(Error::class.java)
            adapter.fromJson(json)
        } catch (e: Exception) {
            null
        }
        return error
    }

    private fun readJsonFile() =
        context.assets.open("investments.json").bufferedReader().use { it.readText() }
}