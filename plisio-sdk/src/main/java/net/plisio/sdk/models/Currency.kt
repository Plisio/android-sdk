@file:Suppress("SpellCheckingInspection", "unused")

package net.plisio.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Serializable
data class PlisioCryptoCurrency(
    @SerialName("cid") val id: PlisioCryptoCurrencyID,
    @SerialName("name") val name: String,
    @SerialName("currency") val code: PlisioCryptoCurrencyCode,
    @SerialName("icon") val iconURL: String? = null,
    @SerialName("precision") val precision: Int = 8,
    @SerialName("output_precision") val outputPrecision: Int = precision,
    @SerialName("maintenance") val isOnMaintenance: Boolean = false,
    @SerialName("amount") val amount: PlisioAmount? = null,
    @SerialName("min_sum_in") val minAmount: PlisioAmount? = null,
    @SerialName("contractStandard") val contractStandard: String? = null
) {
    val formattedAmount: String = formatAmount(amount ?: PlisioAmount.Zero)

    val isAmountValid: Boolean = minAmount == null || amount == null || amount.value >= minAmount.value
    val isEnabled: Boolean = isAmountValid && !isOnMaintenance

    fun formatAmount(amount: PlisioAmount, withCurrency: Boolean = true) = buildString {
        append(getFormat().format(amount.value))
        if (withCurrency) {
            append(' ')
            append(code.code)
        }
    }

    private fun getFormat() = DecimalFormat().apply {
        minimumFractionDigits = outputPrecision.coerceAtMost(8)
        maximumFractionDigits = minimumFractionDigits
        groupingSize = 0
        decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.US)
    }
}

/**
 * Cryptocurrency ID
 * @see <a href="https://plisio.net/documentation/appendices/supported-cryptocurrencies">Plisio API - Supported cryptocurrencies</a>
 */
@JvmInline
@Serializable
value class PlisioCryptoCurrencyID(val id: String) {
    override fun toString(): String = id

    companion object {
        /** Ethereum */
        val ETH = PlisioCryptoCurrencyID("ETH")

        /** Bitcoin */
        val BTC = PlisioCryptoCurrencyID("BTC")

        /** Litecoin  */
        val LTC = PlisioCryptoCurrencyID("LTC")

        /** Dash */
        val DASH = PlisioCryptoCurrencyID("DASH")

        /** Zcash */
        val TZEC = PlisioCryptoCurrencyID("TZEC")

        /** Dogecoin */
        val DOGE = PlisioCryptoCurrencyID("DOGE")

        /** Bitcoin Cash */
        val BCH = PlisioCryptoCurrencyID("BCH")

        /** Monero */
        val XMR = PlisioCryptoCurrencyID("XMR")

        /** Tether */
        val USDT = PlisioCryptoCurrencyID("USDT")

        /** USD Coin */
        val USDC = PlisioCryptoCurrencyID("USDC")

        /** Shiba Inu */
        val SHIB = PlisioCryptoCurrencyID("SHIB")

        /** BitTorrent-Chain */
        val BTTC = PlisioCryptoCurrencyID("BTTC")
    }
}

/**
 * Cryptocurrency code
 * @see <a href="https://plisio.net/documentation/appendices/supported-cryptocurrencies">Plisio API - Supported cryptocurrencies</a>
 */
@JvmInline
@Serializable
value class PlisioCryptoCurrencyCode(val code: String) {
    override fun toString(): String = code
}

/**
 * Fiat currency ID
 * @see <a href="https://plisio.net/documentation/appendices/supported-fiat-currencies">Plisio API - Supported fiat currencies</a>
 */
@JvmInline
@Serializable
value class PlisioFiatCurrencyID(val id: String) {
    override fun toString(): String = id

    companion object {
        /** United Arab Emirates Dirham */
        val AED = PlisioFiatCurrencyID("AED")

        /** Afghan Afghani */
        val AFN = PlisioFiatCurrencyID("AFN")

        /** Albanian Lek */
        val ALL = PlisioFiatCurrencyID("ALL")

        /** Armenian Dram */
        val AMD = PlisioFiatCurrencyID("AMD")

        /** Netherlands Antillean Guilder */
        val ANG = PlisioFiatCurrencyID("ANG")

        /** Angolan Kwanza */
        val AOA = PlisioFiatCurrencyID("AOA")

        /** Argentine Peso */
        val ARS = PlisioFiatCurrencyID("ARS")

        /** Australian Dollar */
        val AUD = PlisioFiatCurrencyID("AUD")

        /** Aruban Florin */
        val AWG = PlisioFiatCurrencyID("AWG")

        /** Azerbaijani Manat */
        val AZN = PlisioFiatCurrencyID("AZN")

        /** Bosnia-Herzegovina Convertible Mark */
        val BAM = PlisioFiatCurrencyID("BAM")

        /** Barbadian Dollar */
        val BBD = PlisioFiatCurrencyID("BBD")

        /** Bangladeshi Taka */
        val BDT = PlisioFiatCurrencyID("BDT")

        /** Bulgarian Lev */
        val BGN = PlisioFiatCurrencyID("BGN")

        /** Bahraini Dinar */
        val BHD = PlisioFiatCurrencyID("BHD")

        /** Burundian Franc */
        val BIF = PlisioFiatCurrencyID("BIF")

        /** Bermuda Dollar */
        val BMD = PlisioFiatCurrencyID("BMD")

        /** Brunei Dollar */
        val BND = PlisioFiatCurrencyID("BND")

        /** Bolivian Boliviano */
        val BOB = PlisioFiatCurrencyID("BOB")

        /** Brazilian Real */
        val BRL = PlisioFiatCurrencyID("BRL")

        /** Bahamian Dollar */
        val BSD = PlisioFiatCurrencyID("BSD")

        /** Bhutanese Ngultrum */
        val BTN = PlisioFiatCurrencyID("BTN")

        /** Botswana Pula */
        val BWP = PlisioFiatCurrencyID("BWP")

        /** New Belarusian Ruble */
        val BYN = PlisioFiatCurrencyID("BYN")

        /** Belarusian Ruble */
        val BYR = PlisioFiatCurrencyID("BYR")

        /** Belize Dollar */
        val BZD = PlisioFiatCurrencyID("BZD")

        /** Canadian Dollar */
        val CAD = PlisioFiatCurrencyID("CAD")

        /** Congolese Franc */
        val CDF = PlisioFiatCurrencyID("CDF")

        /** Swiss Franc */
        val CHF = PlisioFiatCurrencyID("CHF")

        /** Chilean Unit of Account (UF) */
        val CLF = PlisioFiatCurrencyID("CLF")

        /** Chilean Peso */
        val CLP = PlisioFiatCurrencyID("CLP")

        /** Chinese Yuan */
        val CNY = PlisioFiatCurrencyID("CNY")

        /** Colombian Peso */
        val COP = PlisioFiatCurrencyID("COP")

        /** Costa Rican Colón */
        val CRC = PlisioFiatCurrencyID("CRC")

        /** Cuban Convertible Peso */
        val CUC = PlisioFiatCurrencyID("CUC")

        /** Cuban Peso */
        val CUP = PlisioFiatCurrencyID("CUP")

        /** Cape Verdean Escudo */
        val CVE = PlisioFiatCurrencyID("CVE")

        /** Czech Republic Koruna */
        val CZK = PlisioFiatCurrencyID("CZK")

        /** Djiboutian Franc */
        val DJF = PlisioFiatCurrencyID("DJF")

        /** Danish Krone */
        val DKK = PlisioFiatCurrencyID("DKK")

        /** Dominican Peso */
        val DOP = PlisioFiatCurrencyID("DOP")

        /** Algerian Dinar */
        val DZD = PlisioFiatCurrencyID("DZD")

        /** Egyptian Pound */
        val EGP = PlisioFiatCurrencyID("EGP")

        /** Eritrean Nakfa */
        val ERN = PlisioFiatCurrencyID("ERN")

        /** Ethiopian Birr */
        val ETB = PlisioFiatCurrencyID("ETB")

        /** Euro */
        val EUR = PlisioFiatCurrencyID("EUR")

        /** Fijian Dollar */
        val FJD = PlisioFiatCurrencyID("FJD")

        /** Falkland Islands Pound */
        val FKP = PlisioFiatCurrencyID("FKP")

        /** British Pound Sterling */
        val GBP = PlisioFiatCurrencyID("GBP")

        /** Georgian Lari */
        val GEL = PlisioFiatCurrencyID("GEL")

        /** Guernsey Pound */
        val GGP = PlisioFiatCurrencyID("GGP")

        /** Ghanaian Cedi */
        val GHS = PlisioFiatCurrencyID("GHS")

        /** Gibraltar Pound */
        val GIP = PlisioFiatCurrencyID("GIP")

        /** Gambian Dalasi */
        val GMD = PlisioFiatCurrencyID("GMD")

        /** Guinean Franc */
        val GNF = PlisioFiatCurrencyID("GNF")

        /** Guatemalan Quetzal */
        val GTQ = PlisioFiatCurrencyID("GTQ")

        /** Guyanese Dollar */
        val GYD = PlisioFiatCurrencyID("GYD")

        /** Hong Kong Dollar */
        val HKD = PlisioFiatCurrencyID("HKD")

        /** Honduran Lempira */
        val HNL = PlisioFiatCurrencyID("HNL")

        /** Croatian Kuna */
        val HRK = PlisioFiatCurrencyID("HRK")

        /** Haitian Gourde */
        val HTG = PlisioFiatCurrencyID("HTG")

        /** Hungarian Forint */
        val HUF = PlisioFiatCurrencyID("HUF")

        /** Indonesian Rupiah */
        val IDR = PlisioFiatCurrencyID("IDR")

        /** Israeli New Sheqel */
        val ILS = PlisioFiatCurrencyID("ILS")

        /** Manx pound */
        val IMP = PlisioFiatCurrencyID("IMP")

        /** Indian Rupee */
        val INR = PlisioFiatCurrencyID("INR")

        /** Iraqi Dinar */
        val IQD = PlisioFiatCurrencyID("IQD")

        /** Iranian Rial */
        val IRR = PlisioFiatCurrencyID("IRR")

        /** Icelandic Króna */
        val ISK = PlisioFiatCurrencyID("ISK")

        /** Jersey Pound */
        val JEP = PlisioFiatCurrencyID("JEP")

        /** Jamaican Dollar */
        val JMD = PlisioFiatCurrencyID("JMD")

        /** Jordanian Dinar */
        val JOD = PlisioFiatCurrencyID("JOD")

        /** Japanese Yen */
        val JPY = PlisioFiatCurrencyID("JPY")

        /** Kenyan Shilling */
        val KES = PlisioFiatCurrencyID("KES")

        /** Kyrgyzstan Som */
        val KGS = PlisioFiatCurrencyID("KGS")

        /** Cambodian Riel */
        val KHR = PlisioFiatCurrencyID("KHR")

        /** Comorian Franc */
        val KMF = PlisioFiatCurrencyID("KMF")

        /** North Korean Won */
        val KPW = PlisioFiatCurrencyID("KPW")

        /** South Korean Won */
        val KRW = PlisioFiatCurrencyID("KRW")

        /** Kuwaiti Dinar */
        val KWD = PlisioFiatCurrencyID("KWD")

        /** Cayman Islands Dollar */
        val KYD = PlisioFiatCurrencyID("KYD")

        /** Kazakhstani Tenge */
        val KZT = PlisioFiatCurrencyID("KZT")

        /** Laotian Kip */
        val LAK = PlisioFiatCurrencyID("LAK")

        /** Lebanese Pound */
        val LBP = PlisioFiatCurrencyID("LBP")

        /** Sri Lankan Rupee */
        val LKR = PlisioFiatCurrencyID("LKR")

        /** Liberian Dollar */
        val LRD = PlisioFiatCurrencyID("LRD")

        /** Lesotho Loti */
        val LSL = PlisioFiatCurrencyID("LSL")

        /** Lithuanian Litas */
        val LTL = PlisioFiatCurrencyID("LTL")

        /** Latvian Lats */
        val LVL = PlisioFiatCurrencyID("LVL")

        /** Libyan Dinar */
        val LYD = PlisioFiatCurrencyID("LYD")

        /** Moroccan Dirham */
        val MAD = PlisioFiatCurrencyID("MAD")

        /** Moldovan Leu */
        val MDL = PlisioFiatCurrencyID("MDL")

        /** Malagasy Ariary */
        val MGA = PlisioFiatCurrencyID("MGA")

        /** Macedonian Denar */
        val MKD = PlisioFiatCurrencyID("MKD")

        /** Myanmar Kyat */
        val MMK = PlisioFiatCurrencyID("MMK")

        /** Mongolian Tugrik */
        val MNT = PlisioFiatCurrencyID("MNT")

        /** Macanese Pataca */
        val MOP = PlisioFiatCurrencyID("MOP")

        /** Mauritania Ouguiya */
        val MRO = PlisioFiatCurrencyID("MRO")

        /** Mauritian Rupee */
        val MUR = PlisioFiatCurrencyID("MUR")

        /** Maldivian Rufiyaa */
        val MVR = PlisioFiatCurrencyID("MVR")

        /** Malawi Kwacha */
        val MWK = PlisioFiatCurrencyID("MWK")

        /** Mexican Peso */
        val MXN = PlisioFiatCurrencyID("MXN")

        /** Malaysian Ringgit */
        val MYR = PlisioFiatCurrencyID("MYR")

        /** Mozambican Metical */
        val MZN = PlisioFiatCurrencyID("MZN")

        /** Namibian Dollar */
        val NAD = PlisioFiatCurrencyID("NAD")

        /** Nigerian Naira */
        val NGN = PlisioFiatCurrencyID("NGN")

        /** Nicaraguan Córdoba */
        val NIO = PlisioFiatCurrencyID("NIO")

        /** Norwegian Krone */
        val NOK = PlisioFiatCurrencyID("NOK")

        /** Nepalese Rupee */
        val NPR = PlisioFiatCurrencyID("NPR")

        /** New Zealand Dollar */
        val NZD = PlisioFiatCurrencyID("NZD")

        /** Omani Rial */
        val OMR = PlisioFiatCurrencyID("OMR")

        /** Panamanian Balboa */
        val PAB = PlisioFiatCurrencyID("PAB")

        /** Peruvian Nuevo Sol */
        val PEN = PlisioFiatCurrencyID("PEN")

        /** Papua New Guinean Kina */
        val PGK = PlisioFiatCurrencyID("PGK")

        /** Philippine Peso */
        val PHP = PlisioFiatCurrencyID("PHP")

        /** Pakistani Rupee */
        val PKR = PlisioFiatCurrencyID("PKR")

        /** Polish Zloty */
        val PLN = PlisioFiatCurrencyID("PLN")

        /** Paraguayan Guarani */
        val PYG = PlisioFiatCurrencyID("PYG")

        /** Qatari Rial */
        val QAR = PlisioFiatCurrencyID("QAR")

        /** Romanian Leu */
        val RON = PlisioFiatCurrencyID("RON")

        /** Serbian Dinar */
        val RSD = PlisioFiatCurrencyID("RSD")

        /** Russian Ruble */
        val RUB = PlisioFiatCurrencyID("RUB")

        /** Rwandan Franc */
        val RWF = PlisioFiatCurrencyID("RWF")

        /** Saudi Riyal */
        val SAR = PlisioFiatCurrencyID("SAR")

        /** Solomon Islands Dollar */
        val SBD = PlisioFiatCurrencyID("SBD")

        /** Seychellois Rupee */
        val SCR = PlisioFiatCurrencyID("SCR")

        /** Sudanese Pound */
        val SDG = PlisioFiatCurrencyID("SDG")

        /** Swedish Krona */
        val SEK = PlisioFiatCurrencyID("SEK")

        /** Singapore Dollar */
        val SGD = PlisioFiatCurrencyID("SGD")

        /** Saint Helena Pound */
        val SHP = PlisioFiatCurrencyID("SHP")

        /** Sierra Leonean Leone */
        val SLL = PlisioFiatCurrencyID("SLL")

        /** Somali Shilling */
        val SOS = PlisioFiatCurrencyID("SOS")

        /** Surinamese Dollar */
        val SRD = PlisioFiatCurrencyID("SRD")

        /** São Tomé and Príncipe Dobra */
        val STD = PlisioFiatCurrencyID("STD")

        /** Salvadoran Colón */
        val SVC = PlisioFiatCurrencyID("SVC")

        /** Syrian Pound */
        val SYP = PlisioFiatCurrencyID("SYP")

        /** Swazi Lilangeni */
        val SZL = PlisioFiatCurrencyID("SZL")

        /** Thai Baht */
        val THB = PlisioFiatCurrencyID("THB")

        /** Tajikistan Somoni */
        val TJS = PlisioFiatCurrencyID("TJS")

        /** Turkmenistan Manat */
        val TMT = PlisioFiatCurrencyID("TMT")

        /** Tunisian Dinar */
        val TND = PlisioFiatCurrencyID("TND")

        /** Tongan Paʻanga */
        val TOP = PlisioFiatCurrencyID("TOP")

        /** Turkish Lira */
        val TRY = PlisioFiatCurrencyID("TRY")

        /** Trinidad and Tobago Dollar */
        val TTD = PlisioFiatCurrencyID("TTD")

        /** New Taiwan Dollar */
        val TWD = PlisioFiatCurrencyID("TWD")

        /** Tanzanian Shilling */
        val TZS = PlisioFiatCurrencyID("TZS")

        /** Ukrainian Hryvnia */
        val UAH = PlisioFiatCurrencyID("UAH")

        /** Ugandan Shilling */
        val UGX = PlisioFiatCurrencyID("UGX")

        /** United States Dollar */
        val USD = PlisioFiatCurrencyID("USD")

        /** Uruguayan Peso */
        val UYU = PlisioFiatCurrencyID("UYU")

        /** Uzbekistan Som */
        val UZS = PlisioFiatCurrencyID("UZS")

        /** Venezuelan Bolívar Fuerte */
        val VEF = PlisioFiatCurrencyID("VEF")

        /** Vietnamese Dong */
        val VND = PlisioFiatCurrencyID("VND")

        /** Vanuatu Vatu */
        val VUV = PlisioFiatCurrencyID("VUV")

        /** Samoan Tala */
        val WST = PlisioFiatCurrencyID("WST")

        /** CFA Franc BEAC */
        val XAF = PlisioFiatCurrencyID("XAF")

        /** Silver (troy ounce) */
        val XAG = PlisioFiatCurrencyID("XAG")

        /** Gold (troy ounce) */
        val XAU = PlisioFiatCurrencyID("XAU")

        /** East Caribbean Dollar */
        val XCD = PlisioFiatCurrencyID("XCD")

        /** Special Drawing Rights */
        val XDR = PlisioFiatCurrencyID("XDR")

        /** CFA Franc BCEAO */
        val XOF = PlisioFiatCurrencyID("XOF")

        /** CFP Franc */
        val XPF = PlisioFiatCurrencyID("XPF")

        /** Yemeni Rial */
        val YER = PlisioFiatCurrencyID("YER")

        /** South African Rand */
        val ZAR = PlisioFiatCurrencyID("ZAR")

        /** Zambian Kwacha (pre-2013) */
        val ZMK = PlisioFiatCurrencyID("ZMK")

        /** Zambian Kwacha */
        val ZMW = PlisioFiatCurrencyID("ZMW")

        /** Zimbabwean Dollar */
        val ZWL = PlisioFiatCurrencyID("ZWL")
    }
}