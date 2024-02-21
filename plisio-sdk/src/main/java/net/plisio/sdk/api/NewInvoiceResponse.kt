package net.plisio.sdk.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewInvoiceResponse(
    val data: InvoiceData,
    val status: String
)
@Serializable
data class InvoiceData(
    @SerialName("actual_fee") val actualFee: String,
    val amount: String,
    @SerialName("created_at_utc") val createdAtUtc: Int,
    @SerialName("created_utc") val createdUtc: Int,
    val currency: String,
    @SerialName("email_already_set") val emailAlreadySet: Boolean,
    @SerialName("expected_confirmations") val expectedConfirmations: String,
    @SerialName("expire_at_utc") val expireAtUtc: String,
    @SerialName("expire_utc") val expireUtc: String,
    val id: String,
    @SerialName("invoice_commission") val invoiceCommission: String,
    @SerialName("invoice_sum") val invoiceSum: String,
    @SerialName("invoice_total_sum") val invoiceTotalSum: String,
    @SerialName("invoice_url") val invoiceUrl: String,
    @SerialName("paid_id") val paidId: String,
    val params: InvoiceParams,
    @SerialName("pending_amount") val pendingAmount: String,
    @SerialName("psys_cid") val psysCid: String,
    @SerialName("qr_code") val qrCode: String,
    @SerialName("qr_url") val qrUrl: String,
    @SerialName("received_amount") val receivedAmount: String,
    @SerialName("remaining_amount") val remainingAmount: String,
    @SerialName("source_currency") val sourceCurrency: String,
    @SerialName("source_rate") val sourceRate: String,
    val status: String,
    @SerialName("status_code") val statusCode: Int,
    @SerialName("switch_id") val switchId: String,
    val tx: List<String>,
    @SerialName("tx_id") val txId: String,
    @SerialName("tx_url") val txUrl: String,
    @SerialName("txn_id") val txnId: String,
    val type: String,
    @SerialName("verify_hash") val verifyHash: String,
    @SerialName("view_key") val viewKey: String,
    @SerialName("wallet_hash") val walletHash: String
)

@Serializable
data class InvoiceParams(
    val amount: String,
    @SerialName("order_name") val orderName: String,
    @SerialName("order_number") val orderNumber: String,
    @SerialName("source_amount") val sourceAmount: String,
    @SerialName("source_currency") val sourceCurrency: String,
    @SerialName("source_rate") val sourceRate: String
)