package net.plisio.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import net.plisio.sdk.models.PlisioCryptoCurrencyID
import net.plisio.sdk.models.PlisioErrorWithResponseText
import net.plisio.sdk.models.PlisioInvoiceID
import net.plisio.sdk.models.PlisioNotFoundError

internal class PlisioAPI(
    private val logLevel: LogLevel,
    private val baseURL: String,
    private val additionalHeaders: Map<String, String>? = null
) {
    private val jsonSerializer = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val client = HttpClient(OkHttp) {
        expectSuccess = false
        defaultRequest {
            contentType(ContentType.Application.Json)
            header("parseErrors", "1")
            additionalHeaders?.forEach { (name, value) ->
                header(name, value)
            }
        }
        install(ContentNegotiation) {
            json(jsonSerializer)
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = logLevel
        }
    }

    private suspend inline fun <reified T> request(
        endpoint: String,
        requestBuilder: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        return try {
            val result = client.get("$baseURL/$endpoint", requestBuilder)
            when {
                result.status.isSuccess() -> {
                    Result.success(result.body())
                }

                result.status == HttpStatusCode.NotFound -> {
                    Result.failure(PlisioNotFoundError(result.request.url.toString()))
                }

                else -> {
                    try {
                        val error = result.body<ErrorResponse>().error
                        Result.failure(
                            when {
                                error.message != null && error.message.startsWith('{') -> error.copy(
                                    message = (jsonSerializer.decodeFromString<JsonObject>(error.message).values.firstOrNull() as? JsonArray)?.first()?.jsonPrimitive?.contentOrNull
                                )

                                else -> error
                            }
                        )
                    } catch (e: Throwable) {
                        Result.failure(PlisioErrorWithResponseText(e, result.bodyAsText()))
                    }
                }
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    internal suspend fun getInvoice(
        id: PlisioInvoiceID,
        viewKey: String
    ): Result<PlisioInvoiceDetails> {
        return request<InvoiceResponse>("invoices/${id.id.trim()}") {
            parameter("view_key", viewKey.trim())
        }.map(InvoiceResponse::invoiceDetails)
    }

    internal suspend fun setUserEmail(
        email: String,
        id: PlisioInvoiceID,
        viewKey: String
    ): Result<PlisioInvoiceDetails> {
        return request<InvoiceResponse>("invoices/email/${id.id.trim()}") {
            parameter("email", email.trim())
            parameter("view_key", viewKey.trim())
        }.map(InvoiceResponse::invoiceDetails)
    }

    internal suspend fun setCurrency(
        currency: PlisioCryptoCurrencyID,
        id: PlisioInvoiceID,
        viewKey: String
    ): Result<PlisioInvoiceDetails> {
        return request<InvoiceResponse>("invoices/switch/${id.id.trim()}") {
            parameter("psys_cid", currency.id.trim())
            parameter("view_key", viewKey.trim())
        }.map(InvoiceResponse::invoiceDetails)
    }
}