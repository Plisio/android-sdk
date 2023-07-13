package net.plisio.sdk.ui.compose.style

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import net.plisio.sdk.ui.compose.R

object PlisioTextStyle {
    val font = FontFamily(
        Font(R.font.plisio_proxima_nova_regular, FontWeight.Normal),
        Font(R.font.plisio_proxima_nova_bold, FontWeight.Bold),
        Font(R.font.plisio_proxima_nova_extrabold, FontWeight.ExtraBold)
    )

    val default = TextStyle(
        fontFamily = font,
        letterSpacing = 0.sp
    )

    val paymentButton = default.copy(
        fontWeight = FontWeight.ExtraBold
    )

    val textField = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    val button = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )

    val textButton = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    val paymentSheetCountdown = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        fontFeatureSettings = "tnum"
    )

    val paymentSheetShopName = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )

    val paymentSheetAmount = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFeatureSettings = "tnum"
    )

    val paymentSheetTitle = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        textAlign = TextAlign.Center
    )

    val paymentSheetText = default.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Center,
        fontFeatureSettings = "tnum"
    )

    val paymentSheetError = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 16.sp
    )

    val paymentSheetWalletAddress = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Center,
        fontFeatureSettings = "tnum"
    )

    val currencyCardName = default.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    val currencyCardAmount = default.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        fontFeatureSettings = "tnum"
    )
}