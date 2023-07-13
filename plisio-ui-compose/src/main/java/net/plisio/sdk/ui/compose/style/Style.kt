package net.plisio.sdk.ui.compose.style

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse

data class PlisioStyle(
    val primaryColor: Color = PlisioColor.primary,
    val onPrimaryColor: Color = contrastingColorFor(primaryColor),
    val secondaryColor: Color = PlisioColor.secondary,
    val onSecondaryColor: Color = contrastingColorFor(secondaryColor),
    val errorColor: Color = PlisioColor.error,
    val onErrorColor: Color = contrastingColorFor(errorColor),
    val paymentButton: PaymentButton = PaymentButton.Default,
    val paymentButtonBackground: Color = Color.Unspecified,
    val paymentButtonContent: Color = contrastingColorFor(paymentButtonBackground),
    val paymentButtonAccent: Color = primaryColor,
    val paymentSheetHeader: PaymentSheetHeader = PaymentSheetHeader.Gradient(secondaryColor, primaryColor),
    val showPaymentSheetHeader: Boolean = true,
    val paymentSheetBackground: Color = Color.Unspecified,
    val paymentSheetContent: Color = contrastingColorFor(paymentSheetBackground),
    val qrCodeAccent: Color = primaryColor
) {
    enum class PaymentButton {
        Default, Icons, FlatIcons
    }

    sealed interface PaymentSheetHeader {
        data class Colored(
            val color: Color,
            val contentColor: Color = contrastingColorFor(color)
        ) : PaymentSheetHeader

        data class Gradient(
            val start: Color = PlisioColor.secondary,
            val end: Color = PlisioColor.primary,
            val contentColor: Color = contrastingColorFor(start),
            val startOffset: Offset = Offset.Zero,
            val endOffset: Offset = Offset.Infinite
        ) : PaymentSheetHeader

        companion object {
            val Transparent = Colored(Color.Unspecified)
        }
    }

    internal val resolvedPaymentButtonBackground: Color
        @Composable get() = paymentButtonBackground.takeOrElse { if (MaterialTheme.colors.isLight) PlisioColor.light else PlisioColor.dark }

    internal val resolvedPaymentButtonContent: Color
        @Composable get() = paymentButtonContent.takeOrElse { contrastingColorFor(resolvedPaymentButtonBackground) }

    internal val resolvedPaymentSheetBackground: Color
        @Composable get() = paymentSheetBackground.takeOrElse { if (MaterialTheme.colors.isLight) PlisioColor.light else PlisioColor.darkBackground }

    internal val resolvedPaymentSheetContent: Color
        @Composable get() = paymentSheetContent.takeOrElse { contrastingColorFor(resolvedPaymentSheetBackground) }

    companion object {
        @Composable
        fun fromTheme(colors: Colors = MaterialTheme.colors) = PlisioStyle(
            primaryColor = colors.primary,
            onPrimaryColor = colors.onPrimary,
            secondaryColor = colors.secondary,
            onSecondaryColor = colors.onSecondary,
            errorColor = colors.error,
            onErrorColor = colors.onError
        )
    }
}

val LocalPlisioStyle = compositionLocalOf { PlisioStyle() }