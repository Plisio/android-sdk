package net.plisio.sdk.demo.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.plisio.sdk.ui.compose.style.contrastingColorFor

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ButtonGroup(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Row(
            horizontalArrangement = Arrangement.spacedBy((-2).dp),
            modifier = modifier
        ) {
            items.forEachIndexed { index, item ->
                val backgroundColor =
                    if (selectedIndex == index) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                val contentColor = contrastingColorFor(backgroundColor)
                OutlinedButton(
                    onClick = { onItemSelected(index) },
                    shape = when (index) {
                        0 -> MaterialTheme.shapes.small.copy(
                            topEnd = ZeroCornerSize,
                            bottomEnd = ZeroCornerSize
                        )

                        items.lastIndex -> MaterialTheme.shapes.small.copy(
                            topStart = ZeroCornerSize,
                            bottomStart = ZeroCornerSize
                        )

                        else -> RectangleShape
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = backgroundColor,
                        contentColor = contentColor,
                    ),
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item,
                        letterSpacing = 0.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}