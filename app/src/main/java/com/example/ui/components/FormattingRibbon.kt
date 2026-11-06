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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.OutlineItem

@Composable
fun FormattingRibbon(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
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
    outlineItems: List<OutlineItem>,
    onJumpToOutline: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("خط وتنسيق", "فقرات وعناوين", "إدراج", "فهرس المستند")

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Ribbon Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 12.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier.testTag("ribbon_tab_$index")
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Ribbon Action Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                when (selectedTab) {
                    0 -> FontStyleRibbon(
                        fontSize = fontSize,
                        onFontSizeChange = onFontSizeChange,
                        onFormatBold = onFormatBold,
                        onFormatItalic = onFormatItalic,
                        onFormatUnderline = onFormatUnderline,
                        onFormatStrikethrough = onFormatStrikethrough,
                        onFormatHighlight = onFormatHighlight
                    )
                    1 -> ParagraphRibbon(
                        onFormatHeading = onFormatHeading,
                        onFormatList = onFormatList
                    )
                    2 -> InsertRibbon(
                        onInsertTable = onInsertTable,
                        onInsertDivider = onInsertDivider,
                        onInsertDate = onInsertDate,
                        onInsertLink = onInsertLink
                    )
                    3 -> OutlineRibbon(
                        outlineItems = outlineItems,
                        onJumpToOutline = onJumpToOutline
                    )
                }
            }
        }
    }
}

@Composable
private fun FontStyleRibbon(
    fontSize: Int,
    onFontSizeChange: (Int) -> Unit,
    onFormatBold: () -> Unit,
    onFormatItalic: () -> Unit,
    onFormatUnderline: () -> Unit,
    onFormatStrikethrough: () -> Unit,
    onFormatHighlight: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        RibbonActionButton(icon = Icons.Default.FormatBold, label = "عريض", tag = "btn_bold", onClick = onFormatBold)
        RibbonActionButton(icon = Icons.Default.FormatItalic, label = "مائل", tag = "btn_italic", onClick = onFormatItalic)
        RibbonActionButton(icon = Icons.Default.FormatUnderlined, label = "تحته خط", tag = "btn_underline", onClick = onFormatUnderline)
        RibbonActionButton(icon = Icons.Default.FormatStrikethrough, label = "يتوسطه خط", tag = "btn_strike", onClick = onFormatStrikethrough)
        RibbonActionButton(icon = Icons.Default.BorderColor, label = "تمييز", tag = "btn_highlight", onClick = onFormatHighlight)

        Box(
            modifier = Modifier
                .height(24.dp)
                .width(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        // Font Size Stepper
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            IconButton(
                onClick = { onFontSizeChange(fontSize - 2) },
                modifier = Modifier.size(32.dp).testTag("btn_font_minus")
            ) {
                Icon(Icons.Default.Remove, contentDescription = "تصغير الخط", modifier = Modifier.size(16.dp))
            }

            Text(
                text = "$fontSize pt",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            IconButton(
                onClick = { onFontSizeChange(fontSize + 2) },
                modifier = Modifier.size(32.dp).testTag("btn_font_plus")
            ) {
                Icon(Icons.Default.Add, contentDescription = "تكبير الخط", modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun ParagraphRibbon(
    onFormatHeading: (Int) -> Unit,
    onFormatList: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TextRibbonPill(label = "عنوان 1", tag = "btn_h1", onClick = { onFormatHeading(1) })
        TextRibbonPill(label = "عنوان 2", tag = "btn_h2", onClick = { onFormatHeading(2) })
        TextRibbonPill(label = "عنوان 3", tag = "btn_h3", onClick = { onFormatHeading(3) })
        TextRibbonPill(label = "نص عادي", tag = "btn_p", onClick = { onFormatHeading(0) })

        Box(
            modifier = Modifier
                .height(24.dp)
                .width(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        RibbonActionButton(icon = Icons.Default.FormatListBulleted, label = "نقطي", tag = "btn_bullet", onClick = { onFormatList("bullet") })
        RibbonActionButton(icon = Icons.Default.FormatListNumbered, label = "رقمي", tag = "btn_numbered", onClick = { onFormatList("number") })
        RibbonActionButton(icon = Icons.Default.CheckBox, label = "مهمة", tag = "btn_checklist", onClick = { onFormatList("check") })
        RibbonActionButton(icon = Icons.Default.FormatQuote, label = "اقتباس", tag = "btn_quote", onClick = { onFormatList("quote") })
    }
}

@Composable
private fun InsertRibbon(
    onInsertTable: () -> Unit,
    onInsertDivider: () -> Unit,
    onInsertDate: () -> Unit,
    onInsertLink: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RibbonActionButton(icon = Icons.Default.TableChart, label = "جدول", tag = "btn_insert_table", onClick = onInsertTable)
        RibbonActionButton(icon = Icons.Default.HorizontalRule, label = "خط فاصل", tag = "btn_insert_divider", onClick = onInsertDivider)
        RibbonActionButton(icon = Icons.Default.Today, label = "التاريخ", tag = "btn_insert_date", onClick = onInsertDate)
        RibbonActionButton(icon = Icons.Default.Link, label = "رابط", tag = "btn_insert_link", onClick = onInsertLink)
    }
}

@Composable
private fun OutlineRibbon(
    outlineItems: List<OutlineItem>,
    onJumpToOutline: (Int) -> Unit
) {
    if (outlineItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "لم يتم العثور على عناوين رئيسية. استخدم عنوان 1 أو عنوان 2 لإنشاء فهرس.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            outlineItems.forEach { item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clickable { onJumpToOutline(item.position) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "H${item.level}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RibbonActionButton(
    icon: ImageVector,
    label: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TextRibbonPill(
    label: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
