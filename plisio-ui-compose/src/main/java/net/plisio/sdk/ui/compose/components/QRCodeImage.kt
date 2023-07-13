package net.plisio.sdk.ui.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import io.github.g0dkar.qrcode.ErrorCorrectionLevel
import io.github.g0dkar.qrcode.QRCode
import io.github.g0dkar.qrcode.QRCodeDataType
import io.github.g0dkar.qrcode.internals.QRCodeSquareType
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle
import kotlin.math.round

@Composable
@NonRestartableComposable
internal fun QRCodeImage(
    data: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    accentColor: Color = LocalPlisioStyle.current.qrCodeAccent
) {
    val image = remember(data) {
        QRCode(
            data = data,
            errorCorrectionLevel = ErrorCorrectionLevel.M,
            dataType = QRCodeDataType.DEFAULT
        ).encode()
    }

    Canvas(modifier = modifier.aspectRatio(1f)) {
        val cellSize = Size(round(size.width / image.size), round(size.height / image.size))

        image.forEach { rowCells ->
            rowCells.forEach { cell ->
                if (cell?.dark == true) {
                    drawRect(
                        color = when (cell.squareInfo.type) {
                            QRCodeSquareType.POSITION_PROBE, QRCodeSquareType.POSITION_ADJUST -> accentColor
                            else -> color
                        },
                        topLeft = Offset(cellSize.width * cell.row, cellSize.height * cell.col),
                        size = cellSize,
                        style = Fill
                    )
                }
            }
        }
    }
}