package net.plisio.sdk.models

import androidx.annotation.StringRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.plisio.sdk.R

/** Invoice ID */
@JvmInline
@Serializable
value class PlisioInvoiceID(val id: String) {
    override fun toString(): String = id
}

/**
 * Plisio White Label invoice with additional data
 * @see <a href="https://plisio.net/white-label">Plisio - White Label</a>
 * @see <a href="https://plisio.net/documentation/endpoints/create-an-invoice">Plisio API - Create an Invoice - White Label additional data</a>
 */
@Serializable
data class PlisioInvoice(
    /** Invoice ID */
    @SerialName("id") val id: PlisioInvoiceID,

    /** Invoice view key */
    @SerialName("view_key") val viewKey: String? = null,

    /** Invoice payment page URL */
    @SerialName("invoice_url") val url: String,

    /** Invoice status */
    @SerialName("status") val status: Status = Status.Undefined,

    /** Invoice status code */
    @SerialName("status_code") val statusCode: Int = 0,

    /** Invoice amount in the selected cryptocurrency */
    @SerialName("amount") val amount: PlisioAmount,

    /** The amount that was paid but not yet confirmed */
    @SerialName("pending_amount") val pendingAmount: PlisioAmount,

    /** The amount that was paid and confirmed */
    @SerialName("received_amount") val receivedAmount: PlisioAmount,

    /** The remaining amount to be paid */
    @SerialName("remaining_amount") val unpaidAmount: PlisioAmount,

    /** Invoice hash */
    @SerialName("wallet_hash") val walletHash: String,

    /** Cryptocurrency ID */
    @SerialName("psys_cid") val currency: PlisioCryptoCurrencyID,

    /** Cryptocurrency code */
    @SerialName("currency") val currencyCode: PlisioCryptoCurrencyCode,

    /** How many confirmations are expected to mark the invoice as completed */
    @SerialName("expected_confirmations") val expectedConfirmations: Int,

    /** Plisio commission */
    @SerialName("invoice_commission") val commission: PlisioAmount,

    /**
     * If shop pays the commission: [amount] - [commission]
     *
     * If client pays the commission: [amount]
     */
    @SerialName("invoice_sum") val sum: PlisioAmount,

    /**
     * If shop pays the commission: [amount]
     *
     * If client pays the commission: [commission] + [sum]
     */
    @SerialName("invoice_total_sum") val totalSum: PlisioAmount,

    /** Invoice creation date */
    @SerialName("created_utc") val creationDate: PlisioDate? = null,

    /** Invoice expiration date */
    @SerialName("expire_utc") val expirationDate: PlisioDate? = null,

    /** Is user email set */
    @SerialName("email_already_set") val isUserEmailSet: Boolean = false,

    /** Invoice QR code text */
    @SerialName("qr_url") val qrURL: String? = null
) {
    /** Invoice completion status */
    val completion: Completion
        get() = when (status) {
            Status.Expired -> when (receivedAmount.value > UnpaidThreshold && receivedAmount.value < amount.value) {
                true -> Completion.PartiallyCompleted
                false -> Completion.Expired
            }

            Status.Mismatch -> Completion.Overpaid
            Status.Error, Status.Cancelled -> Completion.Error
            else -> Completion.Completed
        }

    val isConfirming: Boolean
        get() = ((status == Status.Pending && statusCode == Status.PartialPayment) || status == Status.PendingInternal) && unpaidAmount.value < UnpaidThreshold

    val isReplacedWithNewInvoice: Boolean
        get() = status == Status.CancelledDuplicate || statusCode == Status.ReplacedWithNewInvoice

    @Serializable
    enum class Status(@StringRes val description: Int) {
        @SerialName("undefined")
        Undefined(R.string.plisio_invoice_status_undefined_description),
        @SerialName("new")
        New(R.string.plisio_invoice_status_new_description),
        @SerialName("pending")
        Pending(R.string.plisio_invoice_status_pending_description),
        @SerialName("pending internal")
        PendingInternal(R.string.plisio_invoice_status_pending_description),
        @SerialName("expired")
        Expired(R.string.plisio_invoice_status_expired_description),
        @SerialName("completed")
        Completed(R.string.plisio_invoice_status_completed_description),
        @SerialName("mismatch")
        Mismatch(R.string.plisio_invoice_status_mismatch_description),
        @SerialName("error")
        Error(R.string.plisio_invoice_status_error_description),
        @SerialName("cancelled")
        Cancelled(R.string.plisio_invoice_status_cancelled_description),
        @SerialName("cancelled duplicate")
        CancelledDuplicate(R.string.plisio_invoice_status_cancelled_duplicate_description);

        val isInProgress: Boolean
            get() = when (this) {
                Undefined, New, Pending, PendingInternal, CancelledDuplicate -> true
                else -> false
            }

        val isFinished: Boolean
            get() = !isInProgress

        val indicator: Indicator
            get() = when (this) {
                Undefined, New, Pending, PendingInternal, CancelledDuplicate -> Indicator.Progress
                Completed, Mismatch -> Indicator.Completed
                Error, Cancelled, Expired -> Indicator.Error
            }

        enum class Indicator {
            Progress, Error, Completed
        }

        companion object {
            const val PartialPayment = 2
            const val ReplacedWithNewInvoice = 111
        }
    }

    enum class Completion {
        Completed, PartiallyCompleted, Overpaid, Expired, Error
    }

    companion object {
        const val UnpaidThreshold = 0.000000001
    }
}