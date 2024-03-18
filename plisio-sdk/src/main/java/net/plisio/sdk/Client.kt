package net.plisio.sdk

import io.ktor.client.plugins.logging.LogLevel
import net.plisio.sdk.api.PlisioAPI
import net.plisio.sdk.api.PlisioInvoiceDetails
import net.plisio.sdk.models.PlisioCryptoCurrencyID
import net.plisio.sdk.models.PlisioInvoiceID

/**
 * Plisio API client
 */
object PlisioClient {
    /** Default Plisio API base URL */
    const val DefaultBaseURL = "https://api.plisio.net/api/v1"

    /**
     * Changes Plisio API client configuration
     * @param enableLogging Whether Plisio API requests should be logged
     * @param showErrorDetails Should error details be shown in the default UI
     * @param baseURL Plisio API URL
     * @param additionalHeaders Additional headers for Plisio API requests
     */
    @JvmStatic
    fun configure(
        enableLogging: Boolean = false,
        showErrorDetails: Boolean = false,
        baseURL: String = DefaultBaseURL,
        additionalHeaders: Map<String, String>? = null
    ) {
        api = PlisioAPI(
            logLevel = if (enableLogging) LogLevel.BODY else LogLevel.NONE,
            baseURL = baseURL,
            additionalHeaders = additionalHeaders
        )
        this.showErrorDetails = showErrorDetails
    }

    /**
     * Gets invoice details by invoice ID
     * @param id ID of the invoice
     * @param viewKey The key returned after creating the invoice by `/api/v1/invoices/new`
     */
    suspend fun getInvoice(id: PlisioInvoiceID, viewKey: String): Result<PlisioInvoiceDetails> = api.getInvoice(id, viewKey)

    /**
     * Sets user's e-mail address for invoice
     * @param email User e-mail
     * @param id ID of the invoice
     * @param viewKey The key returned after creating the invoice by `/api/v1/invoices/new`
     */
    suspend fun setUserEmail(
        email: String,
        id: PlisioInvoiceID,
        viewKey: String
    ): Result<PlisioInvoiceDetails> = api.setUserEmail(email, id, viewKey)

    /**
     * Changes currency for invoice
     * @param currency ID of the currency
     * @param id ID of the invoice
     * @param viewKey The key returned after creating the invoice by `/api/v1/invoices/new`
     */
    suspend fun setCurrency(
        currency: PlisioCryptoCurrencyID,
        id: PlisioInvoiceID,
        viewKey: String
    ): Result<PlisioInvoiceDetails> = api.setCurrency(currency, id, viewKey)

    private var api = PlisioAPI(logLevel = LogLevel.NONE, baseURL = DefaultBaseURL)
    var showErrorDetails: Boolean = false
        private set
}
