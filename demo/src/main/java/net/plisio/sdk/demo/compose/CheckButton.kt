package net.plisio.sdk.demo.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.plisio.sdk.ui.compose.style.PlisioColor
import net.plisio.sdk.ui.compose.style.contrastingColorFor

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun CheckButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (checked) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    val contentColor = contrastingColorFor(backgroundColor)
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        OutlinedButton(
            onClick = { onCheckedChange(!checked) },
            contentPadding = PaddingValues(4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor,
            ),
            modifier = modifier
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = contentColor.copy(alpha = 0.2f),
                    uncheckedColor = PlisioColor.dark.copy(alpha = 0.1f),
                    checkmarkColor = contentColor
                ),
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = text,
                letterSpacing = 0.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}