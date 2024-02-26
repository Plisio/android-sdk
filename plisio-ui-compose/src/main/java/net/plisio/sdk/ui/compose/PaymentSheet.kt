package net.plisio.sdk.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.components.AdaptiveSheetLayout
import net.plisio.sdk.ui.compose.components.LocalAdaptiveSheetLayoutState
import net.plisio.sdk.ui.compose.components.LocalImageLoader
import net.plisio.sdk.ui.compose.components.rememberImageLoader
import net.plisio.sdk.ui.compose.sheet.PlisioPaymentSheetContent
import net.plisio.sdk.ui.compose.sheet.PlisioPaymentSheetHeader
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle
import net.plisio.sdk.ui.compose.style.PlisioStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

/**
 * Plisio payment sheet
 * @param isVisible Is sheet visible
 * @param setVisibility Executes when user closes the sheet
 * @param paymentStep Plisio payment step managed by [net.plisio.sdk.viewmodels.PlisioPaymentViewModel]
 * @param modifier Optional [Modifier] for the entire component
 * @param style Plisio UI components style
 * @param shape The shape of the sheet
 * @param elevation The elevation of the sheet
 * @param headerContent Custom header content, [PlisioPaymentSheetHeader] by default
 * @param footerContent Custom footer content
 * @param content Custom content, [PlisioPaymentSheetContent] by default
 * @see [net.plisio.sdk.viewmodels.PlisioPaymentViewModel]
 * @see [PlisioStyle]
 * @see [PlisioPaymentSheetHeader]
 * @see [PlisioPaymentSheetContent]
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlisioPaymentSheet(
    isVisible: Boolean,
    setVisibility: (Boolean) -> Unit,
    paymentStep: PlisioPaymentStep,
    modifier: Modifier = Modifier,
    style: PlisioStyle = LocalPlisioStyle.current,
    shape: Shape = MaterialTheme.shapes.large,
    elevation: Dp = ModalBottomSheetDefaults.Elevation,
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    headerContent: @Composable (PlisioPaymentStep) -> Unit = { PlisioPaymentSheetHeader(paymentStep = it) },
    footerContent: @Composable (PlisioPaymentStep) -> Unit = {},
    content: @Composable (PlisioPaymentStep) -> Unit = { PlisioPaymentSheetContent(paymentStep = it) }
) {
    CompositionLocalProvider(
        LocalPlisioStyle provides style,
        LocalPlisioPaymentSheetDismissTrigger provides { setVisibility(false) },
        LocalImageLoader provides rememberImageLoader()
    ) {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(
                primary = style.primaryColor,
                onPrimary = style.onPrimaryColor,
                secondary = style.secondaryColor,
                onSecondary = style.onSecondaryColor,
                error = style.errorColor,
                onError = style.onErrorColor
            )
        ) {
            AdaptiveSheetLayout(
                isVisible = isVisible,
                setVisibility = setVisibility,
                shape = shape,
                elevation = elevation,
                backgroundColor = style.resolvedPaymentSheetBackground,
                contentColor = style.resolvedPaymentSheetContent,
                modifier = modifier
            ) {
                headerContent(paymentStep)
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .animateContentSize(tween(100, easing = LinearEasing))
                        .imePadding()
                        .navigationBarsPadding()
                        .height(IntrinsicSize.Min)
                ) {
                    Box(
                        content = { content(paymentStep) },
                        modifier = Modifier
                            .defaultMinSize(minHeight = 500.dp.coerceAtMost(LocalAdaptiveSheetLayoutState.current.maxHeight - 150.dp))
                            .weight(1f)
                    )
                    footerContent(paymentStep)
                }
            }
        }
    }

    val focusManager = LocalFocusManager.current
    SideEffect {
        if (isVisible && paymentStep !is PlisioPaymentStep.UserEmail) {
            softwareKeyboardController?.hide()
            focusManager.clearFocus(force = true)
        }
    }
}

internal val LocalPlisioPaymentSheetDismissTrigger = staticCompositionLocalOf { {} }