package net.plisio.sdk.viewmodels

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.plisio.sdk.PlisioClient
import net.plisio.sdk.api.PlisioInvoiceDetails
import net.plisio.sdk.models.PlisioCryptoCurrencyID
import net.plisio.sdk.models.PlisioInvoice
import net.plisio.sdk.models.PlisioInvoiceID
import net.plisio.sdk.models.PlisioNotFoundError
import net.plisio.sdk.uimodels.PlisioPaymentStep
import net.plisio.sdk.uimodels.PlisioPaymentStepWithInvoice
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

/**
 * Plisio payment [ViewModel]
 *
 * Use [Lifecycle.addObserver] to stop and start refreshing the invoice automatically on lifecycle events
 */
class PlisioPaymentViewModel : ViewModel(), DefaultLifecycleObserver {
    private var state = State(this)
    private val _paymentStep = MutableStateFlow(state.toFormStep())

    /** Current payment step */
    val paymentStep: StateFlow<PlisioPaymentStep>
        get() = _paymentStep.asStateFlow()

    /**
     * Loads an invoice by [id] and [viewKey]
     * @param id Invoice ID
     * @param [viewKey] The key returned when creating the invoice
     */
    fun loadInvoice(id: PlisioInvoiceID, viewKey: String) {
        if (id == state.initialInvoiceID && viewKey == state.initialInvoiceViewKey) {
            startRefreshing(
                id = state.currentInvoiceID,
                viewKey = state.currentInvoiceViewKey,
                loadImmediately = true
            )
        } else {
            state = state.copy(
                initialInvoiceID = id,
                initialInvoiceViewKey = viewKey,
                currencySelected = false
            )
            startRefreshing(
                id = id,
                viewKey = viewKey,
                loadImmediately = true
            )
        }
    }

    fun newInvoice(
        context: Context,
        api_key: String,
        currency: String,
        source_currency: String,
        source_amount: String,
        allowed_psys_cids: String,
        order_name: String,
        order_number: String,
        expire_min: Int
    ) {
        val sharedPrefs = context.getSharedPreferences("main", Context.MODE_PRIVATE)
        val currentTimestamp = System.currentTimeMillis()

        if (sharedPrefs.contains("paymentId") && sharedPrefs.contains("paymentViewKey") && sharedPrefs.getLong(
                "endTimestamp",
                Long.MAX_VALUE
            ) >= currentTimestamp
        ) {
            loadInvoice(sharedPrefs.getString("paymentId", "") ?: "", sharedPrefs.getString("paymentViewKey", "") ?: "")
        } else {
            viewModelScope.launch {
                update(
                    state.copy(
                        setUserEmailError = null,
                        isLoading = true,
                        error = null
                    )
                )

                PlisioClient.getNewInvoice(
                    api_key,
                    currency,
                    source_currency,
                    source_amount,
                    allowed_psys_cids,
                    order_name,
                    order_number,
                    expire_min.toString()
                )
                    .onSuccess {
                        val endTime = System.currentTimeMillis() + (expire_min * 60000)
                        sharedPrefs.edit().putString("paymentId", it.id).putString("paymentViewKey", it.viewKey)
                            .putLong("endTimestamp", endTime).apply()

                        val invoiceID = PlisioInvoiceID(it.id)

                        if (invoiceID == state.initialInvoiceID && it.viewKey == state.initialInvoiceViewKey) {
                            startRefreshing(
                                id = state.currentInvoiceID,
                                viewKey = state.currentInvoiceViewKey,
                                loadImmediately = true
                            )
                        } else {
                            state = state.copy(
                                initialInvoiceID = invoiceID,
                                initialInvoiceViewKey = it.viewKey,
                                currencySelected = false
                            )
                            startRefreshing(
                                id = invoiceID,
                                viewKey = it.viewKey,
                                loadImmediately = true
                            )
                        }
                    }.onFailure {
                        state = state.copy(
                            error(it)
                        )
                    }
            }
        }
    }

    /**
     * Loads an invoice by [id] and [viewKey]
     * @param id Invoice ID
     * @param [viewKey] The key returned when creating the invoice
     */
    fun loadInvoice(id: String, viewKey: String) {
        loadInvoice(id = PlisioInvoiceID(id), viewKey = viewKey)
    }

    private var refreshJob: Job? = null
    private fun startRefreshing(
        id: PlisioInvoiceID? = null,
        viewKey: String? = null,
        loadImmediately: Boolean = false
    ) {
        stopRefreshing()
        val invoiceID = id ?: state.currentInvoiceID
        val invoiceViewKey = viewKey ?: state.currentInvoiceViewKey
        if (invoiceID == null || invoiceID.id.isBlank() || invoiceViewKey.isNullOrBlank()) return
        refreshJob = viewModelScope.launch(Dispatchers.IO) {
            var delay = !loadImmediately
            while (isActive) {
                update(
                    state.copy(
                        currentInvoiceID = invoiceID,
                        currentInvoiceViewKey = invoiceViewKey,
                        isLoading = state.invoiceDetails == null,
                        error = null
                    )
                )
                if (delay) delay(10.seconds)
                delay = true
                val response = PlisioClient.getInvoice(invoiceID, invoiceViewKey)
                val invoiceDetails = response.getOrNull()
                update(
                    state.copy(
                        invoiceDetails = invoiceDetails,
                        currencySelected = state.currencySelected || !(invoiceDetails?.canChangeCurrency
                            ?: false),
                        isLoading = false,
                        error = response.exceptionOrNull()
                    )
                )
                when {
                    invoiceDetails == null -> break
                    invoiceDetails.invoice.isReplacedWithNewInvoice && invoiceDetails.activeInvoiceID != null -> {
                        startRefreshing(
                            invoiceDetails.activeInvoiceID,
                            invoiceDetails.invoice.viewKey,
                            loadImmediately = true
                        )
                        break
                    }

                    !invoiceDetails.invoice.status.isInProgress -> break
                }
            }
        }
    }

    private fun stopRefreshing() {
        refreshJob?.cancel()
        refreshJob = null
    }

    private fun setUserEmail(email: String) {
        val invoiceID = state.currentInvoiceID ?: return
        val invoiceViewKey = state.currentInvoiceViewKey ?: return
        stopRefreshing()
        viewModelScope.launch(Dispatchers.IO) {
            update(
                state.copy(
                    setUserEmailError = null,
                    isLoading = true,
                    error = null
                )
            )
            val response = PlisioClient.setUserEmail(email, invoiceID, invoiceViewKey)
            update(
                state.copy(
                    invoiceDetails = response.getOrNull() ?: state.invoiceDetails,
                    setUserEmailError = response.exceptionOrNull()?.message,
                    currencySelected = false,
                    isLoading = false,
                    error = null
                )
            )
            response.getOrNull()?.let { invoiceDetails ->
                startRefreshing(
                    id = invoiceDetails.invoice.id,
                    viewKey = invoiceDetails.invoice.viewKey
                )
            }
        }
    }

    private fun setCurrency(currency: PlisioCryptoCurrencyID) {
        val invoiceID = state.currentInvoiceID ?: return
        val invoiceViewKey = state.currentInvoiceViewKey ?: return
        viewModelScope.launch(Dispatchers.IO) {
            if (currency == state.invoiceDetails?.currency?.id) {
                update(state.copy(currencySelected = true))
                return@launch
            }
            stopRefreshing()
            update(
                state.copy(
                    isLoading = true,
                    error = null
                )
            )
            val response = PlisioClient.setCurrency(currency, invoiceID, invoiceViewKey)
            update(
                state.copy(
                    invoiceDetails = response.getOrNull() ?: state.invoiceDetails,
                    isLoading = response.isSuccess,
                    currencySelected = true,
                    error = response.exceptionOrNull()
                )
            )
            response.getOrNull()?.let { invoiceDetails ->
                startRefreshing(
                    id = invoiceDetails.invoice.id,
                    viewKey = invoiceDetails.invoice.viewKey
                )
            }
        }
    }

    private fun changeCurrency() {
        if (state.invoiceDetails?.canChangeCurrency != true) return
        viewModelScope.launch(Dispatchers.IO) {
            update(state.copy(currencySelected = false))
        }
    }

    private suspend fun update(s: State) {
        state = s
        _paymentStep.emit(state.toFormStep())
    }

    /** Resets internal state */
    fun reset() {
        viewModelScope.launch(Dispatchers.IO) {
            update(State(this@PlisioPaymentViewModel))
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        startRefreshing(loadImmediately = true)
    }

    override fun onPause(owner: LifecycleOwner) {
        stopRefreshing()
    }

    private data class State(
        val viewModel: PlisioPaymentViewModel,
        val initialInvoiceID: PlisioInvoiceID? = null,
        val initialInvoiceViewKey: String? = null,
        val currentInvoiceID: PlisioInvoiceID? = null,
        val currentInvoiceViewKey: String? = null,
        val invoiceDetails: PlisioInvoiceDetails? = null,
        val isLoading: Boolean = false,
        val currencySelected: Boolean = false,
        val setUserEmailError: String? = null,
        val error: Throwable? = null
    ) {
        fun toFormStep(): PlisioPaymentStep = when {
            invoiceDetails == null && error is PlisioNotFoundError && !PlisioClient.showErrorDetails -> Step.Initial(
                isLoading
            )

            error != null -> Step.Error(error)
            invoiceDetails == null -> Step.Initial(isLoading)
            isLoading || invoiceDetails.invoice.status == PlisioInvoice.Status.Undefined || invoiceDetails.invoice.status == PlisioInvoice.Status.CancelledDuplicate -> Step.Loading(
                invoiceDetails
            )

            invoiceDetails.invoice.status.isFinished -> Step.Completion(invoiceDetails)
            invoiceDetails.invoice.isConfirming -> Step.Confirmation(invoiceDetails)
            !invoiceDetails.invoice.isUserEmailSet -> Step.UserEmail(
                invoiceDetails,
                viewModel,
                setUserEmailError
            )

            !currencySelected && invoiceDetails.availableCurrencies.size > 1 -> Step.Currency(
                invoiceDetails,
                viewModel
            )

            else -> Step.Payment(invoiceDetails, viewModel)
        }
    }

    private sealed class Step(override val invoiceDetails: PlisioInvoiceDetails) :
        PlisioPaymentStepWithInvoice {
        data class Initial(override val isLoading: Boolean) : PlisioPaymentStep.Initial

        data class Error(override val error: Throwable) : PlisioPaymentStep.Error

        class Loading(invoiceDetails: PlisioInvoiceDetails) : Step(invoiceDetails),
            PlisioPaymentStep.Loading

        class UserEmail(
            invoiceDetails: PlisioInvoiceDetails,
            val viewModel: PlisioPaymentViewModel,
            override val error: String? = null
        ) : Step(invoiceDetails), PlisioPaymentStep.UserEmail {
            override fun setUserEmail(email: String) {
                viewModel.setUserEmail(email)
            }
        }

        class Currency(
            invoiceDetails: PlisioInvoiceDetails,
            val viewModel: PlisioPaymentViewModel
        ) : Step(invoiceDetails), PlisioPaymentStep.Currency {
            override val currencies = invoiceDetails.availableCurrencies
            override fun setCurrency(currency: PlisioCryptoCurrencyID) {
                viewModel.setCurrency(currency)
            }
        }

        class Payment(
            invoiceDetails: PlisioInvoiceDetails,
            val viewModel: PlisioPaymentViewModel
        ) : Step(invoiceDetails), PlisioPaymentStep.Payment {
            override val canChangeCurrency = invoiceDetails.canChangeCurrency
            override fun changeCurrency() {
                viewModel.changeCurrency()
            }
        }

        class Confirmation(invoiceDetails: PlisioInvoiceDetails) : Step(invoiceDetails),
            PlisioPaymentStep.Confirmation

        class Completion(invoiceDetails: PlisioInvoiceDetails) : Step(invoiceDetails),
            PlisioPaymentStep.Completion
    }
}