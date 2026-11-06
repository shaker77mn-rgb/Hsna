package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pageview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OfficeRibbonBorder
import com.example.ui.theme.OfficeWordBlue
import com.example.util.DocumentStats

@Composable
fun DesktopStatusBar(
    stats: DocumentStats,
    isSaved: Boolean,
    isReadMode: Boolean,
    onToggleReadMode: () -> Unit,
    zoomPercent: Int,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onResetZoom: () -> Unit,
    onStatsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color(0xFFF3F2F1),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(color = OfficeRibbonBorder, thickness = 1.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Document Meta status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    StatusItem(text = "الصفحة 1 من 1", onClick = {})
                    StatusDivider()
                    StatusItem(
                        text = "${stats.words} كلمة",
                        onClick = onStatsClick,
                        tag = "status_word_count"
                    )
                    StatusDivider()
                    StatusItem(text = "العربية (السعودية)", onClick = {})
                    StatusDivider()
                    StatusItem(text = "تطوير: م. شاكر السفياني (770472829)", onClick = onStatsClick)
                    StatusDivider()

                    // AutoSave Status
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Check else Icons.Default.Sync,
                            contentDescription = null,
                            tint = if (isSaved) Color(0xFF2E7D32) else OfficeWordBlue,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isSaved) "تم الحفظ" else "جارٍ الحفظ",
                            fontSize = 10.sp,
                            color = if (isSaved) Color(0xFF2E7D32) else OfficeWordBlue
                        )
                    }
                }

                // Right: View mode buttons & Zoom Controller
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Read Mode button
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isReadMode) Color(0xFFDCEAF9) else Color.Transparent)
                            .clickable(onClick = onToggleReadMode)
                            .testTag("status_read_mode_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = "وضع القراءة",
                            tint = if (isReadMode) OfficeWordBlue else Color(0xFF555555),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    // Print Layout button
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (!isReadMode) Color(0xFFDCEAF9) else Color.Transparent)
                            .clickable(onClick = onToggleReadMode)
                            .testTag("status_print_layout_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Pageview,
                            contentDescription = "تخطيط الطباعة",
                            tint = if (!isReadMode) OfficeWordBlue else Color(0xFF555555),
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Zoom Stepper
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onZoomOut)
                            .testTag("status_zoom_out"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "تصغير", modifier = Modifier.size(11.dp))
                    }

                    Text(
                        text = "$zoomPercent%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier
                            .clickable(onClick = onResetZoom)
                            .padding(horizontal = 2.dp)
                            .testTag("status_zoom_text")
                    )

                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onZoomIn)
                            .testTag("status_zoom_in"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "تكبير", modifier = Modifier.size(11.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusItem(
    text: String,
    onClick: () -> Unit,
    tag: String? = null
) {
    Text(
        text = text,
        fontSize = 10.sp,
        color = Color(0xFF444444),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
    )
}

@Composable
private fun StatusDivider() {
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(1.dp)
            .background(Color(0xFFD2D0CE))
    )
}
