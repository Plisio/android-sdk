package net.plisio.sdk.uimodels

import net.plisio.sdk.api.PlisioInvoiceDetails
import net.plisio.sdk.models.PlisioAmount
import net.plisio.sdk.models.PlisioCryptoCurrency
import net.plisio.sdk.models.PlisioCryptoCurrencyID
import net.plisio.sdk.models.PlisioInvoice
import net.plisio.sdk.models.PlisioShop

/** Plisio invoice payment step */
sealed interface PlisioPaymentStep {
    /** Default state when no invoice has been loaded yet */
    object Default : Initial {
        override val isLoading: Boolean
            get() = false
    }

    /** Invoice loading has started */
    interface Initial : PlisioPaymentStep {
        val isLoading: Boolean
    }

    /** Invoice has not been loaded because of an [error] */
    interface Error : PlisioPaymentStep {
        val error: Throwable
    }

    /** Invoice is refreshing */
    interface Loading : PlisioPaymentStep, PlisioPaymentStepWithInvoice

    /** User e-mail is required */
    interface UserEmail : PlisioPaymentStep, PlisioPaymentStepWithInvoice {
        /** Error message if e-mail couldn't be set */
        val error: String?

        /** Sets user e-mail */
        fun setUserEmail(email: String)
    }

    /** Currency selection if there are multiple currencies available */
    interface Currency : PlisioPaymentStep, PlisioPaymentStepWithInvoice {
        /** Available currencies */
        val currencies: List<PlisioCryptoCurrency>

        /** Selects one of the available [currencies] */
        fun setCurrency(currency: PlisioCryptoCurrencyID)
    }

    /** Main payment step */
    interface Payment : PlisioPaymentStep, PlisioPaymentStepWithInvoice {
        /** Whether invoice currency can be changed */
        val canChangeCurrency: Boolean

        /** Returns to the [Currency] step if [canChangeCurrency] is `true` */
        fun changeCurrency()
    }

    /** Payment is processing, waiting for confirmation */
    interface Confirmation : PlisioPaymentStep, PlisioPaymentStepWithInvoice

    /** Payment has been completed */
    interface Completion : PlisioPaymentStep, PlisioPaymentStepWithInvoice
}

/** [PlisioPaymentStep] with invoice details */
interface PlisioPaymentStepWithInvoice {
    val invoiceDetails: PlisioInvoiceDetails
    val invoice: PlisioInvoice
        get() = invoiceDetails.invoice
    val currency: PlisioCryptoCurrency
        get() = invoiceDetails.currency
    val shop: PlisioShop
        get() = invoiceDetails.shop
    val fullAmount: PlisioAmount
        get() = invoice.amount
    val pendingAmount: PlisioAmount
        get() = invoice.pendingAmount
    val receivedAmount: PlisioAmount
        get() = invoice.receivedAmount
    val unpaidAmount: PlisioAmount
        get() = invoice.unpaidAmount
    val formattedFullAmount: String
        get() = currency.formatAmount(fullAmount)
    val formattedPendingAmount: String
        get() = currency.formatAmount(pendingAmount)
    val formattedReceivedAmount: String
        get() = currency.formatAmount(receivedAmount)
    val formattedUnpaidAmount: String
        get() = currency.formatAmount(unpaidAmount)
    val formattedUnpaidAmountPlain: String
        get() = currency.formatAmount(unpaidAmount, withCurrency = false)
    val completion: PlisioInvoice.Completion
        get() = invoice.completion
}