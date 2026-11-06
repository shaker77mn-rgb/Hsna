package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.ui.theme.OfficeRulerBg
import com.example.ui.theme.OfficeRulerIndent
import com.example.ui.theme.OfficeRulerTicks
import com.example.ui.theme.OfficeWordBlue

@Composable
fun DesktopRuler(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(18.dp),
        color = OfficeRulerBg,
        shadowElevation = 1.dp
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .height(18.dp)
                    .padding(horizontal = 16.dp)
            ) {
                val width = size.width
                val height = size.height

                // Draw background white center area and gray margins
                val marginWidth = 24.dp.toPx()
                
                // Margin shaded areas (gray)
                drawRect(
                    color = Color(0xFFE5E7EB),
                    topLeft = Offset(0f, 0f),
                    size = androidx.compose.ui.geometry.Size(marginWidth, height)
                )
                drawRect(
                    color = Color(0xFFE5E7EB),
                    topLeft = Offset(width - marginWidth, 0f),
                    size = androidx.compose.ui.geometry.Size(marginWidth, height)
                )

                // White printable ruler body
                drawRect(
                    color = Color.White,
                    topLeft = Offset(marginWidth, 0f),
                    size = androidx.compose.ui.geometry.Size(width - 2 * marginWidth, height)
                )

                // Bottom ruler line
                drawLine(
                    color = Color(0xFFD1D5DB),
                    start = Offset(0f, height - 1f),
                    end = Offset(width, height - 1f),
                    strokeWidth = 1f
                )

                // Draw ruler ticks and numbers (1 to 16 cm)
                val printableWidth = width - 2 * marginWidth
                val totalUnits = 16
                val step = printableWidth / totalUnits

                for (i in 0..totalUnits) {
                    val x = marginWidth + i * step

                    // Major tick
                    drawLine(
                        color = OfficeRulerTicks,
                        start = Offset(x, height - 8.dp.toPx()),
                        end = Offset(x, height - 1f),
                        strokeWidth = 1.2f
                    )

                    // Draw unit number
                    if (i > 0 && i < totalUnits && i % 2 == 0) {
                        val textPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 22f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                        }
                        drawContext.canvas.nativeCanvas.drawText(
                            "$i",
                            x,
                            height - 10.dp.toPx(),
                            textPaint
                        )
                    }

                    // Minor tick at half unit
                    if (i < totalUnits) {
                        val halfX = x + step / 2f
                        drawLine(
                            color = OfficeRulerTicks.copy(alpha = 0.6f),
                            start = Offset(halfX, height - 5.dp.toPx()),
                            end = Offset(halfX, height - 1f),
                            strokeWidth = 1f
                        )
                    }
                }

                // Left Indent Markers (Triangles)
                val leftMarkerX = marginWidth
                val topTriangle = Path().apply {
                    moveTo(leftMarkerX - 4.dp.toPx(), 0f)
                    lineTo(leftMarkerX + 4.dp.toPx(), 0f)
                    lineTo(leftMarkerX, 6.dp.toPx())
                    close()
                }
                drawPath(topTriangle, color = OfficeWordBlue, style = Fill)

                val bottomTriangle = Path().apply {
                    moveTo(leftMarkerX, height - 6.dp.toPx())
                    lineTo(leftMarkerX - 4.dp.toPx(), height - 1f)
                    lineTo(leftMarkerX + 4.dp.toPx(), height - 1f)
                    close()
                }
                drawPath(bottomTriangle, color = OfficeWordBlue, style = Fill)

                // Right Margin Marker (Triangle)
                val rightMarkerX = width - marginWidth
                val rightTriangle = Path().apply {
                    moveTo(rightMarkerX, height - 6.dp.toPx())
                    lineTo(rightMarkerX - 4.dp.toPx(), height - 1f)
                    lineTo(rightMarkerX + 4.dp.toPx(), height - 1f)
                    close()
                }
                drawPath(rightTriangle, color = OfficeWordBlue, style = Fill)
            }
        }
    }
}
