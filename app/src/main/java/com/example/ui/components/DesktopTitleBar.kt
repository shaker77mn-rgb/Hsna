package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OfficeWordBlue
import com.example.ui.theme.OfficeWordDarkBlue

@Composable
fun DesktopTitleBar(
    title: String,
    isSaved: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    onSaveClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onPrintClick: () -> Unit,
    onTitleClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = OfficeWordBlue,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Quick Access Toolbar (شريط أدوات الوصول السريع)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Windows Word Icon
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "W",
                                color = OfficeWordBlue,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // AutoSave Toggle Switch (مثل كمبيوتر وورد تماماً)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSaved) Color(0x33FFFFFF) else Color(0x66FFC107),
                        modifier = Modifier
                            .clickable(onClick = onSaveClick)
                            .testTag("desktop_autosave_toggle")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = "حفظ تلقائي",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (isSaved) "حفظ تلقائي: تشغيل" else "جارٍ الحفظ...",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    TitleBarIconButton(
                        icon = Icons.AutoMirrored.Filled.Undo,
                        desc = "تراجع (Ctrl+Z)",
                        enabled = canUndo,
                        tag = "desktop_undo_btn",
                        onClick = onUndoClick
                    )

                    TitleBarIconButton(
                        icon = Icons.AutoMirrored.Filled.Redo,
                        desc = "إعادة (Ctrl+Y)",
                        enabled = canRedo,
                        tag = "desktop_redo_btn",
                        onClick = onRedoClick
                    )

                    TitleBarIconButton(
                        icon = Icons.Default.Print,
                        desc = "طباعة (Ctrl+P)",
                        enabled = true,
                        tag = "desktop_print_btn",
                        onClick = onPrintClick
                    )
                }

                // Middle: Document Title + Tell-me Search Box
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(onClick = onTitleClick)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                            .testTag("desktop_title_badge")
                    ) {
                        Text(
                            text = "$title - Word",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "إعادة تسمية",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(11.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Office Tell-Me / Search
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x33FFFFFF),
                        modifier = Modifier
                            .clickable(onClick = onSearchClick)
                            .testTag("desktop_tell_me_search")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "بحث",
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "بحث (Alt+Q)",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Right: Windows Desktop Window Controls (Minimize, Maximize, Close)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Windows Minimize button (_)
                    Box(
                        modifier = Modifier
                            .size(34.dp, 30.dp)
                            .clickable { /* Minimize */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "—",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    // Windows Maximize / Restore button (❐)
                    Box(
                        modifier = Modifier
                            .size(34.dp, 30.dp)
                            .clickable { /* Maximize */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "❐",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    // Windows Close button (✕ with classic red background on hover/click)
                    Box(
                        modifier = Modifier
                            .size(38.dp, 30.dp)
                            .clickable(onClick = onCloseClick)
                            .testTag("desktop_window_close_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "إغلاق المستند",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleBarIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    desc: String,
    enabled: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(3.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = desc,
            tint = if (enabled) Color.White else Color.White.copy(alpha = 0.4f),
            modifier = Modifier.size(14.dp)
        )
    }
}
