package net.plisio.sdk.ui.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.style.PlisioTextStyle

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)
internal fun PlisioTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    icon: Painter? = null,
    shape: Shape = MaterialTheme.shapes.small,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusRequester: FocusRequester? = null,
    end: (@Composable () -> Unit)? = null
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    val focusManager = LocalFocusManager.current
    val isFocused by interactionSource.collectIsFocusedAsState()

    val bgColor = when {
        isFocused -> LocalContentColor.current.copy(alpha = 0.1f)
        else -> LocalContentColor.current.copy(alpha = 0.05f)
    }

    val fgColor = when {
        value.text.isNotEmpty() -> LocalContentColor.current
        else -> LocalContentColor.current.copy(alpha = 0.5f)
    }

    Surface(
        shape = shape,
        color = bgColor,
        contentColor = fgColor,
        modifier = modifier
            .size(Dp.Unspecified, 40.dp)
            .fillMaxWidth()
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = PlisioTextStyle.textField.copy(color = fgColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(LocalContentColor.current),
            visualTransformation = visualTransformation,
            modifier = Modifier
                .onKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.Tab -> {
                            focusManager.moveFocus(if (keyEvent.isShiftPressed) FocusDirection.Previous else FocusDirection.Next)
                        }

                        Key.Enter, Key.NumPadEnter -> {
                            when (val action = keyboardActions.onDone ?: keyboardActions.onGo
                            ?: keyboardActions.onSend) {
                                null -> false
                                else -> {
                                    action(EmptyKeyboardActionScope)
                                    true
                                }
                            }
                        }

                        else -> false
                    }
                }
                .fillMaxWidth()
                .let {
                    when (focusRequester) {
                        null -> it
                        else -> it.focusRequester(focusRequester)
                    }
                }
        ) { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .size(Dp.Unspecified, 40.dp)
                    .padding(start = 16.dp, end = if (end == null) 16.dp else 0.dp)
                    .fillMaxWidth()
            ) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = fgColor
                    )
                }

                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(vertical = 0.dp)
                        .padding(end = if (end == null) 0.dp else 8.dp)
                ) {
                    if (value.text.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = PlisioTextStyle.textField,
                            color = LocalContentColor.current.copy(alpha = 0.5f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }

                if (end != null) {
                    end()
                }
            }
        }
    }

    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isFocused, isImeVisible) {
        if (isFocused && isImeVisible) {
            bringIntoViewRequester.bringIntoView()
        }
    }
}

internal val TextFieldValueSaver = listSaver<TextFieldValue, Any>(
    save = { listOf(it.text, it.selection.start, it.selection.end) },
    restore = {
        TextFieldValue(
            text = it[0] as String,
            selection = TextRange(it[1] as Int, it[2] as Int)
        )
    }
)

private object EmptyKeyboardActionScope : KeyboardActionScope {
    override fun defaultKeyboardAction(imeAction: ImeAction) {}
}