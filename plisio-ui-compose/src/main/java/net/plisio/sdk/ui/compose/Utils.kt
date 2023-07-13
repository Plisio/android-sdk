package net.plisio.sdk.ui.compose

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun copyToClipboard(
    @StringRes toast: Int,
    clipboardManager: ClipboardManager = LocalClipboardManager.current,
    context: Context = LocalContext.current,
    data: () -> String
): () -> Unit = {
    clipboardManager.setText(AnnotatedString(data()))
    Toast.makeText(context, context.getString(toast), Toast.LENGTH_SHORT).show()
}

@Composable
internal fun highlightedString(
    @StringRes id: Int,
    style: SpanStyle,
    vararg formatArgs: String
) = buildAnnotatedString {
    val string = stringResource(id, *formatArgs)
    append(string)
    formatArgs.forEach { arg ->
        var startIndex = 0
        while (true) {
            val index = string.indexOf(arg, startIndex)
            if (index == -1) break
            startIndex = index + arg.length
            addStyle(style, index, index + arg.length)
        }
    }
}

@Composable
internal fun highlightedString(@StringRes id: Int, vararg formatArgs: String) = highlightedString(id, SpanStyle(fontWeight = FontWeight.Bold), *formatArgs)