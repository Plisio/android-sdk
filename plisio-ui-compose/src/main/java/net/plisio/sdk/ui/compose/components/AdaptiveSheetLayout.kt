package net.plisio.sdk.ui.compose.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.ElevationOverlay
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun AdaptiveSheetLayout(
    isVisible: Boolean,
    setVisibility: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    elevation: Dp = ModalBottomSheetDefaults.Elevation,
    elevationOverlay: ElevationOverlay? = null,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    width: Dp = 500.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    CompositionLocalProvider(LocalElevationOverlay provides elevationOverlay) {
        BoxWithConstraints(modifier.fillMaxSize()) {
            val maxAvailableHeight = maxHeight -
                WindowInsets.statusBars.asPaddingValues().calculateTopPadding() -
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() -
                WindowInsets.ime.asPaddingValues().calculateBottomPadding()
            val hasVerticalSpace = maxAvailableHeight > width

            if (maxWidth > width) {
                if (isVisible) {
                    Dialog(
                        onDismissRequest = { setVisibility(false) },
                        properties = DialogProperties(
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        Surface(
                            shape = shape,
                            elevation = elevation,
                            color = backgroundColor,
                            contentColor = contentColor,
                            modifier = Modifier
                                .width(width)
                                .padding(16.dp)
                        ) {
                            var visibility by remember { mutableStateOf(false) }
                            CompositionLocalProvider(
                                LocalAdaptiveSheetLayoutState provides AdaptiveSheetLayoutState(
                                    isVisible = visibility,
                                    hasVerticalSpace = hasVerticalSpace,
                                    maxHeight = maxAvailableHeight,
                                    mode = AdaptiveSheetLayoutState.Mode.Dialog
                                )
                            ) {
                                Column(content = content)
                            }
                            LaunchedEffect(Unit) {
                                delay(100.milliseconds)
                                visibility = true
                            }
                        }
                    }
                }
            } else {
                val sheetState = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    skipHalfExpanded = true,
                    confirmValueChange = {
                        setVisibility(it != ModalBottomSheetValue.Hidden)
                        true
                    }
                )

                ModalBottomSheetLayout(
                    sheetContent = {
                        CompositionLocalProvider(
                            LocalAdaptiveSheetLayoutState provides AdaptiveSheetLayoutState(
                                isVisible = sheetState.targetValue == ModalBottomSheetValue.Expanded,
                                sheetState = sheetState,
                                hasVerticalSpace = hasVerticalSpace,
                                maxHeight = maxAvailableHeight,
                                mode = AdaptiveSheetLayoutState.Mode.Sheet
                            )
                        ) {
                            Column(content = content)
                        }
                    },
                    sheetState = sheetState,
                    sheetShape = RoundedCornerShape(16.dp),
                    sheetElevation = elevation,
                    sheetBackgroundColor = backgroundColor,
                    sheetContentColor = contentColor,
                    scrimColor = Color.Black.copy(alpha = 0.6f),
                    content = {}
                )
                LaunchedEffect(isVisible) {
                    if (isVisible) {
                        sheetState.show()
                    } else {
                        sheetState.hide()
                    }
                }
                BackHandler(isVisible) {
                    setVisibility(false)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
internal val LocalAdaptiveSheetLayoutState = compositionLocalOf { AdaptiveSheetLayoutState() }

@OptIn(ExperimentalMaterialApi::class)
internal data class AdaptiveSheetLayoutState(
    val isVisible: Boolean = false,
    val sheetState: ModalBottomSheetState? = null,
    val sheetOffset: Float? = null,
    val hasVerticalSpace: Boolean = true,
    val maxHeight: Dp = Dp.Unspecified,
    val mode: Mode = Mode.Sheet
) {
    enum class Mode { Sheet, Dialog }
}