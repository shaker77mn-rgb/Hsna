package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OfficeWordBlue
import com.example.ui.theme.OfficeWordDarkBlue

@Composable
fun DesktopPcKeyboard(
    isArabic: Boolean,
    isShiftActive: Boolean,
    isCtrlActive: Boolean,
    onKeyPress: (String) -> Unit,
    onToggleLang: () -> Unit,
    onToggleShift: () -> Unit,
    onToggleCtrl: () -> Unit,
    onCloseKeyboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color(0xFF2B2D30), // Classic PC Keyboard Dark Chassis
        tonalElevation = 8.dp,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        ) {
            // Keyboard Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1F22))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Keyboard,
                        contentDescription = null,
                        tint = OfficeWordBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "لوحة مفاتيح كمبيوتر كاملة (PC Keyboard)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Shortcuts hint
                    Text(
                        text = "Ctrl+B عريض • Ctrl+Z تراجع • Ctrl+S حفظ",
                        color = Color(0xFFAAAAAA),
                        fontSize = 9.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Language Toggle
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = OfficeWordBlue,
                        modifier = Modifier
                            .clickable(onClick = onToggleLang)
                            .testTag("pc_keyboard_lang_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isArabic) "عربي (AR)" else "English (EN)",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onCloseKeyboard,
                        modifier = Modifier.size(24.dp).testTag("pc_keyboard_close_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق الكيبورد", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            HorizontalDivider(color = Color(0xFF383A40), thickness = 1.dp)

            // Keys Area (Horizontal Scroll to support full PC layout comfortably)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Row 1: Function Keys (Esc, F1 - F12, Del)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PcSpecialKey(label = "Esc", width = 38.dp, onClick = { /* Esc */ })
                    Spacer(modifier = Modifier.width(6.dp))
                    (1..12).forEach { f ->
                        PcFunctionKey(label = "F$f", onClick = { /* Fn */ })
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    PcSpecialKey(label = "Del", width = 38.dp, onClick = { onKeyPress("Del") })
                }

                // Row 2: Numbers & Symbols
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val numRow = if (isArabic) {
                        listOf("ذ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=")
                    } else {
                        listOf("~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=")
                    }
                    numRow.forEach { ch ->
                        PcStandardKey(char = ch, onClick = { onKeyPress(ch) })
                    }
                    PcSpecialKey(label = "Backspace ⌫", width = 80.dp, onClick = { onKeyPress("Backspace") })
                }

                // Row 3: Tab + Top Row
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PcSpecialKey(label = "Tab ⇥", width = 50.dp, onClick = { onKeyPress("Tab") })
                    val topRow = if (isArabic) {
                        listOf("ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج", "د", "\\")
                    } else {
                        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\")
                    }
                    topRow.forEach { ch ->
                        PcStandardKey(char = ch, isShiftActive = isShiftActive, onClick = { onKeyPress(ch) })
                    }
                }

                // Row 4: Caps + Home Row + Enter
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PcSpecialKey(
                        label = "Caps ⇪",
                        width = 56.dp,
                        isActive = isShiftActive,
                        onClick = onToggleShift
                    )
                    val homeRow = if (isArabic) {
                        listOf("ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ك", "ط")
                    } else {
                        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'")
                    }
                    homeRow.forEach { ch ->
                        PcStandardKey(char = ch, isShiftActive = isShiftActive, onClick = { onKeyPress(ch) })
                    }
                    PcSpecialKey(label = "Enter ↵", width = 76.dp, isPrimary = true, onClick = { onKeyPress("Enter") })
                }

                // Row 5: Shift + Bottom Row + Shift
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PcSpecialKey(
                        label = "Shift ⇧",
                        width = 68.dp,
                        isActive = isShiftActive,
                        onClick = onToggleShift
                    )
                    val bottomRow = if (isArabic) {
                        listOf("ئ", "ء", "ؤ", "ر", "ى", "ة", "و", "ز", "ظ", "،", "؟")
                    } else {
                        listOf("z", "x", "c", "v", "b", "n", "m", ",", ".", "/", "-")
                    }
                    bottomRow.forEach { ch ->
                        PcStandardKey(char = ch, isShiftActive = isShiftActive, onClick = { onKeyPress(ch) })
                    }
                    PcSpecialKey(
                        label = "Shift ⇧",
                        width = 68.dp,
                        isActive = isShiftActive,
                        onClick = onToggleShift
                    )
                }

                // Row 6: Bottom Navigation & Spacebar Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PcSpecialKey(label = "Ctrl", width = 46.dp, isActive = isCtrlActive, onClick = onToggleCtrl)
                    PcSpecialKey(label = "❖ Win", width = 46.dp, onClick = {})
                    PcSpecialKey(label = "Alt", width = 42.dp, onClick = {})

                    // Spacebar (مسافة)
                    Box(
                        modifier = Modifier
                            .width(240.dp)
                            .height(34.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF3C3F41))
                            .border(1.dp, Color(0xFF55575C), RoundedCornerShape(4.dp))
                            .clickable { onKeyPress("Space") }
                            .testTag("pc_key_space"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "مسافة (Space)", color = Color(0xFFB0B0B0), fontSize = 11.sp)
                    }

                    PcSpecialKey(label = "Alt Gr", width = 46.dp, onClick = {})
                    PcSpecialKey(
                        label = if (isArabic) "EN" else "عربي",
                        width = 46.dp,
                        isPrimary = true,
                        onClick = onToggleLang
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Arrow Keys: Left, Up, Down, Right
                    PcArrowKey(label = "←", onClick = { onKeyPress("ArrowLeft") })
                    PcArrowKey(label = "↑", onClick = { onKeyPress("ArrowUp") })
                    PcArrowKey(label = "↓", onClick = { onKeyPress("ArrowDown") })
                    PcArrowKey(label = "→", onClick = { onKeyPress("ArrowRight") })
                }
            }
        }
    }
}

@Composable
private fun PcStandardKey(
    char: String,
    isShiftActive: Boolean = false,
    onClick: () -> Unit
) {
    val displayChar = if (isShiftActive) char.uppercase() else char
    Box(
        modifier = Modifier
            .size(34.dp, 34.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF3C3F41))
            .border(1.dp, Color(0xFF55575C), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .testTag("pc_key_$char"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayChar,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun PcFunctionKey(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp, 26.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color(0xFF333538))
            .border(0.5.dp, Color(0xFF4E5157), RoundedCornerShape(3.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color(0xFFAAAAAA),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PcSpecialKey(
    label: String,
    width: androidx.compose.ui.unit.Dp,
    isActive: Boolean = false,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    val bgColor = when {
        isActive -> OfficeWordBlue
        isPrimary -> OfficeWordDarkBlue
        else -> Color(0xFF333538)
    }
    Box(
        modifier = Modifier
            .size(width, 34.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .border(1.dp, if (isActive) Color.White else Color(0xFF55575C), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .testTag("pc_key_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = if (isActive || isPrimary) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun PcArrowKey(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp, 34.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF333538))
            .border(1.dp, Color(0xFF55575C), RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .testTag("pc_key_arrow_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
