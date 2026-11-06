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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pageview
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OfficeRibbonBg
import com.example.ui.theme.OfficeRibbonBorder
import com.example.ui.theme.OfficeRibbonGroupBorder
import com.example.ui.theme.OfficeWordBlue
import com.example.ui.theme.OfficeWordDarkBlue
import com.example.ui.theme.StyleH1Color
import com.example.ui.theme.StyleH2Color
import com.example.ui.theme.WordFonts

@Composable
fun DesktopRibbon(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onFileMenuClick: () -> Unit,
    fontFamily: String,
    onFontFamilyChange: (String) -> Unit,
    fontSize: Int,
    onFontSizeChange: (Int) -> Unit,
    onFormatBold: () -> Unit,
    onFormatItalic: () -> Unit,
    onFormatUnderline: () -> Unit,
    onFormatStrikethrough: () -> Unit,
    onFormatHighlight: () -> Unit,
    onFormatHeading: (Int) -> Unit,
    onFormatList: (String) -> Unit,
    onInsertTable: () -> Unit,
    onInsertDivider: () -> Unit,
    onInsertDate: () -> Unit,
    onInsertLink: () -> Unit,
    onInsertCoverPage: () -> Unit = {},
    onInsertPageBreak: () -> Unit = {},
    onInsertPicture: () -> Unit = {},
    onInsertShapes: () -> Unit = {},
    onInsertHeader: () -> Unit = {},
    onInsertFooter: () -> Unit = {},
    onInsertWordArt: () -> Unit = {},
    onInsertSignatureLine: () -> Unit = {},
    onInsertEquation: () -> Unit = {},
    onInsertSymbol: () -> Unit = {},
    onFindReplaceClick: () -> Unit,
    onStatsClick: () -> Unit,
    onReadAloudClick: () -> Unit,
    isRulerVisible: Boolean,
    onToggleRuler: () -> Unit,
    zoomPercent: Int,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    marginType: String,
    onMarginChange: (String) -> Unit,
    pageOrientation: String,
    onOrientationChange: (String) -> Unit,
    isReadMode: Boolean,
    onToggleReadMode: () -> Unit,
    textDirection: String,
    onSetTextDirection: (String) -> Unit,
    isPcKeyboardVisible: Boolean,
    onTogglePcKeyboard: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("الشريط الرئيسي", "إدراج", "تخطيط", "مراجعة", "عرض")
    var showFontGalleryDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = OfficeRibbonBg,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Ribbon Tab Headers Row (شريط التبويبات الرئيسي)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OfficeRibbonBg)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.Bottom
            ) {
                // "ملف" (File) Rectangular Blue Tab
                Box(
                    modifier = Modifier
                        .background(OfficeWordDarkBlue)
                        .clickable(onClick = onFileMenuClick)
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                        .testTag("ribbon_file_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ملف",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Ribbon Tabs
                tabs.forEachIndexed { index, tabName ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { onTabSelected(index) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("ribbon_tab_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabName,
                            color = if (isSelected) OfficeWordBlue else Color(0xFF333333),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            HorizontalDivider(color = OfficeRibbonBorder, thickness = 1.dp)

            // Ribbon Tools Content Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .background(Color.White)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (selectedTab) {
                    0 -> HomeTabTools(
                        fontFamily = fontFamily,
                        onOpenFontGallery = { showFontGalleryDialog = true },
                        fontSize = fontSize,
                        onFontSizeChange = onFontSizeChange,
                        onFormatBold = onFormatBold,
                        onFormatItalic = onFormatItalic,
                        onFormatUnderline = onFormatUnderline,
                        onFormatStrikethrough = onFormatStrikethrough,
                        onFormatHighlight = onFormatHighlight,
                        onFormatHeading = onFormatHeading,
                        onFormatList = onFormatList,
                        textDirection = textDirection,
                        onSetTextDirection = onSetTextDirection,
                        onTogglePcKeyboard = onTogglePcKeyboard,
                        onAboutClick = onAboutClick,
                        onFindReplaceClick = onFindReplaceClick,
                        onOpenCoverGallery = onInsertCoverPage
                    )
                    1 -> InsertTabTools(
                        onInsertTable = onInsertTable,
                        onInsertDivider = onInsertDivider,
                        onInsertDate = onInsertDate,
                        onInsertLink = onInsertLink,
                        onInsertCoverPage = onInsertCoverPage,
                        onInsertPageBreak = onInsertPageBreak,
                        onInsertPicture = onInsertPicture,
                        onInsertShapes = onInsertShapes,
                        onInsertHeader = onInsertHeader,
                        onInsertFooter = onInsertFooter,
                        onInsertWordArt = onInsertWordArt,
                        onInsertSignatureLine = onInsertSignatureLine,
                        onInsertEquation = onInsertEquation,
                        onInsertSymbol = onInsertSymbol
                    )
                    2 -> LayoutTabTools(
                        marginType = marginType,
                        onMarginChange = onMarginChange,
                        pageOrientation = pageOrientation,
                        onOrientationChange = onOrientationChange
                    )
                    3 -> ReviewTabTools(
                        onStatsClick = onStatsClick,
                        onReadAloudClick = onReadAloudClick
                    )
                    4 -> ViewTabTools(
                        isRulerVisible = isRulerVisible,
                        onToggleRuler = onToggleRuler,
                        zoomPercent = zoomPercent,
                        onZoomIn = onZoomIn,
                        onZoomOut = onZoomOut,
                        isReadMode = isReadMode,
                        onToggleReadMode = onToggleReadMode
                    )
                }
            }

            HorizontalDivider(color = OfficeRibbonBorder, thickness = 1.dp)
        }
    }

    // Full Microsoft Word Desktop Font Gallery Dialog
    if (showFontGalleryDialog) {
        DesktopFontGalleryDialog(
            currentFont = fontFamily,
            onSelectFont = {
                onFontFamilyChange(it)
                showFontGalleryDialog = false
            },
            onDismiss = { showFontGalleryDialog = false }
        )
    }
}

// -------------------------------------------------------------
// TAB 0: HOME (الشريط الرئيسي)
// -------------------------------------------------------------
@Composable
private fun HomeTabTools(
    fontFamily: String,
    onOpenFontGallery: () -> Unit,
    fontSize: Int,
    onFontSizeChange: (Int) -> Unit,
    onFormatBold: () -> Unit,
    onFormatItalic: () -> Unit,
    onFormatUnderline: () -> Unit,
    onFormatStrikethrough: () -> Unit,
    onFormatHighlight: () -> Unit,
    onFormatHeading: (Int) -> Unit,
    onFormatList: (String) -> Unit,
    textDirection: String,
    onSetTextDirection: (String) -> Unit,
    onTogglePcKeyboard: () -> Unit,
    onAboutClick: () -> Unit,
    onFindReplaceClick: () -> Unit,
    onOpenCoverGallery: () -> Unit = {}
) {
    var sizeMenuOpen by remember { mutableStateOf(false) }
    val standardSizes = listOf(9, 10, 11, 12, 14, 16, 18, 20, 24, 28, 36, 48)

    // 1. Group: Font (خط)
    RibbonGroup(title = "خط") {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            // Row 1: Font selector & size stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Font Family Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFF3F2F1), RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFFD2D0CE), RoundedCornerShape(4.dp))
                        .clickable(onClick = onOpenFontGallery)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .testTag("font_family_button")
                ) {
                    Text(
                        text = WordFonts.getDisplayName(fontFamily),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF201F1E)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(14.dp))
                }

                // Font Size Dropdown Button
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFFF3F2F1), RoundedCornerShape(4.dp))
                            .border(1.dp, Color(0xFFD2D0CE), RoundedCornerShape(4.dp))
                            .clickable { sizeMenuOpen = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .testTag("font_size_dropdown")
                    ) {
                        Text(
                            text = "$fontSize",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF201F1E)
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(12.dp))
                    }

                    DropdownMenu(expanded = sizeMenuOpen, onDismissRequest = { sizeMenuOpen = false }) {
                        standardSizes.forEach { sz ->
                            DropdownMenuItem(
                                text = { Text("$sz pt", fontSize = 11.sp, fontWeight = if (sz == fontSize) FontWeight.Bold else FontWeight.Normal) },
                                onClick = {
                                    onFontSizeChange(sz)
                                    sizeMenuOpen = false
                                }
                            )
                        }
                    }
                }

                // Increment / Decrement Steppers
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFF3F2F1), RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFFD2D0CE), RoundedCornerShape(4.dp))
                        .padding(horizontal = 2.dp)
                ) {
                    DesktopMiniButton(icon = Icons.Default.Remove, desc = "تصغير", onClick = { onFontSizeChange(fontSize - 2) })
                    DesktopMiniButton(icon = Icons.Default.Add, desc = "تكبير", onClick = { onFontSizeChange(fontSize + 2) })
                }
            }

            // Row 2: B, I, U, abc, highlight
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RibbonToolButton(icon = Icons.Default.FormatBold, desc = "عريض (Ctrl+B)", onClick = onFormatBold)
                RibbonToolButton(icon = Icons.Default.FormatItalic, desc = "مائل (Ctrl+I)", onClick = onFormatItalic)
                RibbonToolButton(icon = Icons.Default.FormatUnderlined, desc = "تسطير (Ctrl+U)", onClick = onFormatUnderline)
                RibbonToolButton(icon = Icons.Default.FormatStrikethrough, desc = "يتوسطه خط", onClick = onFormatStrikethrough)
                RibbonToolButton(icon = Icons.Default.BorderColor, desc = "تمييز النص (Highlight)", onClick = onFormatHighlight)
            }
        }
    }

    // 2. Group: Paragraph (فقرة)
    RibbonGroup(title = "فقرة") {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RibbonToolButton(icon = Icons.Default.FormatListBulleted, desc = "تعداد نقطي", onClick = { onFormatList("bullet") })
                RibbonToolButton(icon = Icons.Default.FormatListNumbered, desc = "تعداد رقمي", onClick = { onFormatList("number") })
                RibbonToolButton(icon = Icons.Default.CheckBox, desc = "قائمة مهام", onClick = { onFormatList("check") })
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RibbonToolButton(icon = Icons.Default.FormatAlignRight, desc = "محاذاة لليمين", onClick = { onFormatHeading(0) })
                RibbonToolButton(icon = Icons.Default.FormatAlignCenter, desc = "توسيط", onClick = { onFormatHeading(0) })
                RibbonToolButton(icon = Icons.Default.FormatAlignLeft, desc = "محاذاة لليسار", onClick = { onFormatHeading(0) })
                RibbonToolButton(icon = Icons.Default.FormatQuote, desc = "اقتباس", onClick = { onFormatList("quote") })

                // RTL Button (من اليمين لليسار)
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    color = if (textDirection == "rtl") Color(0xFFDCEAF9) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (textDirection == "rtl") OfficeWordBlue else Color(0xFFD2D0CE)),
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable { onSetTextDirection("rtl") }
                        .testTag("btn_direction_rtl")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "¶<",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (textDirection == "rtl") OfficeWordBlue else Color(0xFF333333)
                        )
                    }
                }

                // LTR Button (من اليسار لليمين)
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    color = if (textDirection == "ltr") Color(0xFFDCEAF9) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (textDirection == "ltr") OfficeWordBlue else Color(0xFFD2D0CE)),
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable { onSetTextDirection("ltr") }
                        .testTag("btn_direction_ltr")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = ">¶",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (textDirection == "ltr") OfficeWordBlue else Color(0xFF333333)
                        )
                    }
                }
            }
        }
    }

    // 3. Group: Styles Gallery (أنماط)
    RibbonGroup(title = "أنماط") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StyleGalleryCard(
                title = "AaBbCc",
                subtitle = "عادي",
                color = Color.Black,
                onClick = { onFormatHeading(0) }
            )
            StyleGalleryCard(
                title = "AaBbCc",
                subtitle = "عنوان 1",
                color = StyleH1Color,
                isBold = true,
                onClick = { onFormatHeading(1) }
            )
            StyleGalleryCard(
                title = "AaBbCc",
                subtitle = "عنوان 2",
                color = StyleH2Color,
                isBold = true,
                onClick = { onFormatHeading(2) }
            )
        }
    }

    // 4. Group: Editing & PC Tools (تحرير وأدوات)
    RibbonGroup(title = "تحرير وأدوات") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            RibbonLargeButton(
                icon = Icons.Default.MenuBook,
                label = "قوالب الغلاف",
                onClick = onOpenCoverGallery
            )
            RibbonLargeButton(
                icon = Icons.Default.FindReplace,
                label = "بحث واستبدال",
                onClick = onFindReplaceClick
            )
            RibbonLargeButton(
                icon = Icons.Default.Keyboard,
                label = "كيبورد PC",
                onClick = onTogglePcKeyboard
            )
            RibbonLargeButton(
                icon = Icons.Default.Info,
                label = "حول والحقوق",
                onClick = onAboutClick
            )
        }
    }
}

// -------------------------------------------------------------
// TAB 1: INSERT (إدراج) - كافة أدوات Microsoft Word الرسمية
// -------------------------------------------------------------
@Composable
private fun InsertTabTools(
    onInsertTable: () -> Unit,
    onInsertDivider: () -> Unit,
    onInsertDate: () -> Unit,
    onInsertLink: () -> Unit,
    onInsertCoverPage: () -> Unit,
    onInsertPageBreak: () -> Unit,
    onInsertPicture: () -> Unit,
    onInsertShapes: () -> Unit,
    onInsertHeader: () -> Unit,
    onInsertFooter: () -> Unit,
    onInsertWordArt: () -> Unit,
    onInsertSignatureLine: () -> Unit,
    onInsertEquation: () -> Unit,
    onInsertSymbol: () -> Unit
) {
    // 1. Pages Group (صفحات)
    RibbonGroup(title = "صفحات") {
        RibbonLargeButton(
            icon = Icons.Default.MenuBook,
            label = "صفحة غلاف",
            onClick = onInsertCoverPage
        )
        RibbonLargeButton(
            icon = Icons.Default.HorizontalRule,
            label = "فاصل صفحات",
            onClick = onInsertPageBreak
        )
    }

    // 2. Tables Group (جداول)
    RibbonGroup(title = "جداول") {
        RibbonLargeButton(
            icon = Icons.Default.TableChart,
            label = "جدول بيانات",
            onClick = onInsertTable
        )
    }

    // 3. Illustrations & Media Group (رسومات توضيحية وصور)
    RibbonGroup(title = "رسومات وصور") {
        RibbonLargeButton(
            icon = Icons.Default.AddPhotoAlternate,
            label = "صور",
            onClick = onInsertPicture
        )
        RibbonLargeButton(
            icon = Icons.Default.Category,
            label = "أشكال",
            onClick = onInsertShapes
        )
    }

    // 4. Header & Footer Group (رأس وتذييل)
    RibbonGroup(title = "رأس وتذييل") {
        RibbonLargeButton(
            icon = Icons.Default.VerticalAlignTop,
            label = "رأس الصفحة",
            onClick = onInsertHeader
        )
        RibbonLargeButton(
            icon = Icons.Default.VerticalAlignBottom,
            label = "تذييل الصفحة",
            onClick = onInsertFooter
        )
    }

    // 5. Text & WordArt Group (نص و WordArt وتوقيع)
    RibbonGroup(title = "نص") {
        RibbonLargeButton(
            icon = Icons.Default.TextFields,
            label = "WordArt",
            onClick = onInsertWordArt
        )
        RibbonLargeButton(
            icon = Icons.Default.Draw,
            label = "سطر التوقيع",
            onClick = onInsertSignatureLine
        )
        RibbonLargeButton(
            icon = Icons.Default.Today,
            label = "التاريخ والوقت",
            onClick = onInsertDate
        )
    }

    // 6. Symbols & Equations Group (رموز ومعادلات)
    RibbonGroup(title = "رموز") {
        RibbonLargeButton(
            icon = Icons.Default.Functions,
            label = "معادلة",
            onClick = onInsertEquation
        )
        RibbonLargeButton(
            icon = Icons.Default.ShortText,
            label = "رمز",
            onClick = onInsertSymbol
        )
    }

    // 7. Links Group (ارتباطات وفواصل)
    RibbonGroup(title = "ارتباطات") {
        RibbonLargeButton(
            icon = Icons.Default.Link,
            label = "ارتباط تشعبي",
            onClick = onInsertLink
        )
        RibbonLargeButton(
            icon = Icons.Default.HorizontalRule,
            label = "خط فاصل",
            onClick = onInsertDivider
        )
    }
}

// -------------------------------------------------------------
// TAB 2: LAYOUT (تخطيط)
// -------------------------------------------------------------
@Composable
private fun LayoutTabTools(
    marginType: String,
    onMarginChange: (String) -> Unit,
    pageOrientation: String,
    onOrientationChange: (String) -> Unit
) {
    var marginMenuOpen by remember { mutableStateOf(false) }
    var orientMenuOpen by remember { mutableStateOf(false) }

    RibbonGroup(title = "إعداد الصفحة") {
        Box {
            RibbonLargeButton(
                icon = Icons.Default.Straighten,
                label = "الهوامش\n($marginType)",
                onClick = { marginMenuOpen = true }
            )
            DropdownMenu(expanded = marginMenuOpen, onDismissRequest = { marginMenuOpen = false }) {
                DropdownMenuItem(text = { Text("عادية (2.5 سم)") }, onClick = { onMarginChange("عادية"); marginMenuOpen = false })
                DropdownMenuItem(text = { Text("ضيقة (1.2 سم)") }, onClick = { onMarginChange("ضيقة"); marginMenuOpen = false })
                DropdownMenuItem(text = { Text("عريضة (3.8 سم)") }, onClick = { onMarginChange("عريضة"); marginMenuOpen = false })
            }
        }

        Box {
            RibbonLargeButton(
                icon = Icons.Default.Pageview,
                label = "الاتجاه\n($pageOrientation)",
                onClick = { orientMenuOpen = true }
            )
            DropdownMenu(expanded = orientMenuOpen, onDismissRequest = { orientMenuOpen = false }) {
                DropdownMenuItem(text = { Text("عمودي (Portrait)") }, onClick = { onOrientationChange("عمودي"); orientMenuOpen = false })
                DropdownMenuItem(text = { Text("أفقي (Landscape)") }, onClick = { onOrientationChange("أفقي"); orientMenuOpen = false })
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: REVIEW (مراجعة)
// -------------------------------------------------------------
@Composable
private fun ReviewTabTools(
    onStatsClick: () -> Unit,
    onReadAloudClick: () -> Unit
) {
    RibbonGroup(title = "تدقيق وإحصاء") {
        RibbonLargeButton(icon = Icons.Default.Spellcheck, label = "إحصائيات الكلمات", onClick = onStatsClick)
    }

    RibbonGroup(title = "صوت وقراءة") {
        RibbonLargeButton(icon = Icons.Default.VolumeUp, label = "قراءة بصوت عالٍ", onClick = onReadAloudClick)
    }
}

// -------------------------------------------------------------
// TAB 4: VIEW (عرض)
// -------------------------------------------------------------
@Composable
private fun ViewTabTools(
    isRulerVisible: Boolean,
    onToggleRuler: () -> Unit,
    zoomPercent: Int,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    isReadMode: Boolean,
    onToggleReadMode: () -> Unit
) {
    RibbonGroup(title = "طرق العرض") {
        RibbonLargeButton(
            icon = if (isReadMode) Icons.Default.MenuBook else Icons.Default.Print,
            label = if (isReadMode) "وضع القراءة" else "تخطيط الطباعة",
            onClick = onToggleReadMode
        )
    }

    RibbonGroup(title = "إظهار") {
        RibbonLargeButton(
            icon = Icons.Default.Rule,
            label = if (isRulerVisible) "إخفاء المسطرة" else "إظهار المسطرة",
            onClick = onToggleRuler
        )
    }

    RibbonGroup(title = "تكبير/تصغير") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RibbonToolButton(icon = Icons.Default.ZoomOut, desc = "تصغير", onClick = onZoomOut)
            Text(
                text = "$zoomPercent%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = OfficeWordBlue
            )
            RibbonToolButton(icon = Icons.Default.ZoomIn, desc = "تكبير", onClick = onZoomIn)
        }
    }
}

// -------------------------------------------------------------
// FONT GALLERY DIALOG (معرض الخطوط الرسمي)
// -------------------------------------------------------------
@Composable
private fun DesktopFontGalleryDialog(
    currentFont: String,
    onSelectFont: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = OfficeWordBlue,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("A", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("معرض خطوط Microsoft Word", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "اختر خط المستند المعتمد مع معاينة حية للنص العربي:",
                    fontSize = 11.sp,
                    color = Color(0xFF605E5C)
                )

                HorizontalDivider(color = Color(0xFFE5E7EB))

                WordFonts.availableFonts.forEach { item ->
                    val isSelected = currentFont.equals(item.id, ignoreCase = true) ||
                        currentFont.contains(item.displayName, ignoreCase = true)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onSelectFont(item.id) }
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) OfficeWordBlue else Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(6.dp)
                            ),
                        color = if (isSelected) Color(0xFFDCEAF9) else Color(0xFFFAFAFA)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.displayName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) OfficeWordDarkBlue else Color(0xFF201F1E)
                                )
                                if (isSelected) {
                                    Text("المحدد ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OfficeWordBlue)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.arabicSample,
                                fontSize = 14.sp,
                                fontFamily = item.fontFamily,
                                color = Color(0xFF333333)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق", fontWeight = FontWeight.Bold)
            }
        }
    )
}

// -------------------------------------------------------------
// HELPER COMPONENTS
// -------------------------------------------------------------
@Composable
private fun RibbonGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                content()
            }

            Text(
                text = title,
                fontSize = 9.sp,
                color = Color(0xFF777777),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .width(1.dp)
                .background(OfficeRibbonGroupBorder)
        )
        Spacer(modifier = Modifier.width(6.dp))
    }
}

@Composable
private fun RibbonToolButton(
    icon: ImageVector,
    desc: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(3.dp))
            .clickable(onClick = onClick)
            .border(0.5.dp, Color(0xFFD2D0CE), RoundedCornerShape(3.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = desc,
            tint = Color(0xFF2B2B2B),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun DesktopMiniButton(
    icon: ImageVector,
    desc: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = desc, modifier = Modifier.size(12.dp))
    }
}

@Composable
private fun RibbonLargeButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = OfficeWordBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFF333333),
            lineHeight = 11.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun StyleGalleryCard(
    title: String,
    subtitle: String,
    color: Color,
    isBold: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(3.dp),
        color = Color(0xFFFAFAFA),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCDCDC)),
        modifier = Modifier
            .width(62.dp)
            .height(52.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = color
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color(0xFF555555),
                maxLines = 1
            )
        }
    }
}
