package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.OfficeWordDarkBlue

/**
 * 1. Shapes Gallery Dialog (معرض الأشكال التوضيحية والرسومات)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShapesGalleryDialog(
    onSelectShape: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val basicShapes = listOf(
        Triple("rect", "مربع نص", "┌──┐\n└──┘"),
        Triple("box", "إطار تنبيهي", "╔══╗\n╚══╝"),
        Triple("note", "بطاقة ملاحظة", "📌 ملاحظة"),
        Triple("quote_card", "بطاقة اقتباس", "❝ اقتباس ❞"),
        Triple("check", "شكل إنجاز", "✔ تم الإنجاز")
    )

    val arrowShapes = listOf(
        Triple("arrow_right", "سهم يمين", "➔"),
        Triple("arrow_left", "سهم يسار", "⬅"),
        Triple("arrow_up", "سهم لأعلى", "⬆"),
        Triple("arrow_down", "سهم لأسفل", "⬇")
    )

    val starAndBannerShapes = listOf(
        Triple("star", "نجوم تقييم", "★★★★★"),
        Triple("bullet_point", "نقطة دائرية", "● نقطة"),
        Triple("diamond", "شكل ماسي", "◆ ماسة"),
        Triple("circle", "دائرة توضيحية", "○ دائرة")
    )

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
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("معرض الأشكال (Shapes)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("اختر شكلاً لإدراجه في موضع المؤشر", fontSize = 11.sp, color = Color.Gray)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text("أشكال أساسية ومربعات نصية", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OfficeWordBlue)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    basicShapes.forEach { (type, label, symbol) ->
                        ShapePickerCard(
                            label = label,
                            symbol = symbol,
                            onClick = {
                                onSelectShape(type)
                                onDismiss()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("أسهم وتوجيه", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OfficeWordBlue)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    arrowShapes.forEach { (type, label, symbol) ->
                        ShapePickerCard(
                            label = label,
                            symbol = symbol,
                            onClick = {
                                onSelectShape(type)
                                onDismiss()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("نجوم وشارات", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OfficeWordBlue)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    starAndBannerShapes.forEach { (type, label, symbol) ->
                        ShapePickerCard(
                            label = label,
                            symbol = symbol,
                            onClick = {
                                onSelectShape(type)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = OfficeWordBlue)
            }
        }
    )
}

@Composable
private fun ShapePickerCard(
    label: String,
    symbol: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OfficeRibbonBorder),
        color = Color(0xFFF9FAFB),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(symbol, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OfficeWordBlue)
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, fontSize = 11.sp, color = Color.DarkGray)
        }
    }
}

/**
 * 2. Custom Table Dimensions Dialog (إدراج جدول مخصص مع منتقي الأبعاد)
 */
@Composable
fun TableDimensionsDialog(
    onInsertTable: (rows: Int, cols: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var rows by remember { mutableIntStateOf(3) }
    var cols by remember { mutableIntStateOf(3) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GridOn,
                    contentDescription = null,
                    tint = OfficeWordBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("إدراج جدول (Insert Table)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "حدد عدد الأعمدة والصفوف المطلوبة:",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Columns Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("عدد الأعمدة:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (cols > 1) cols-- },
                            modifier = Modifier.size(36.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "$cols",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OfficeWordBlue
                        )
                        OutlinedButton(
                            onClick = { if (cols < 8) cols++ },
                            modifier = Modifier.size(36.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rows Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("عدد الصفوف:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (rows > 1) rows-- },
                            modifier = Modifier.size(36.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "$rows",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OfficeWordBlue
                        )
                        OutlinedButton(
                            onClick = { if (rows < 12) rows++ },
                            modifier = Modifier.size(36.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mini Preview Table Grid
                Text("معاينة حجم الجدول (${cols} × ${rows}):", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, OfficeWordBlue, RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    repeat(rows.coerceAtMost(5)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            repeat(cols) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(14.dp)
                                        .background(Color(0xFFE8F1FC), RoundedCornerShape(2.dp))
                                )
                            }
                        }
                    }
                    if (rows > 5) {
                        Text("... + ${rows - 5} صفوف إضافية", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onInsertTable(rows, cols)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = OfficeWordBlue)
            ) {
                Text("إدراج الجدول")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = Color.Gray)
            }
        }
    )
}

/**
 * 3. Symbols Gallery Dialog (معرض الرموز الخاصة والرياضية)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SymbolsGalleryDialog(
    onSelectSymbol: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("عام") }

    val commonSymbols = listOf("©", "®", "™", "§", "¶", "†", "‡", "•", "‰", "№", "✓", "✗", "★", "☎", "✉", "➔", "➜", "▲", "▼", "◄", "►")
    val currencySymbols = listOf("$", "€", "£", "¥", "﷼", "د.ك", "د.إ", "ج.م", "₺", "₽", "₩", "₹", "₿")
    val mathSymbols = listOf("±", "×", "÷", "≠", "≈", "≤", "≥", "∑", "√", "∞", "π", "∫", "∂", "∆", "∈", "∉", "⊂", "⊃", "∩", "∪")

    val activeList = when (selectedCategory) {
        "عملات" -> currencySymbols
        "رياضيات" -> mathSymbols
        else -> commonSymbols
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShortText,
                    contentDescription = null,
                    tint = OfficeWordBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("معرض الرموز (Symbols)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Category Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("عام", "عملات", "رياضيات").forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    activeList.forEach { symbol ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, OfficeRibbonBorder),
                            color = Color(0xFFF9FAFB),
                            modifier = Modifier
                                .size(42.dp)
                                .clickable {
                                    onSelectSymbol(symbol)
                                    onDismiss()
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = symbol,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OfficeWordBlue
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق", color = OfficeWordBlue)
            }
        }
    )
}

/**
 * 4. Equations Gallery Dialog (معادلات رياضية قياسية)
 */
@Composable
fun EquationsGalleryDialog(
    onSelectEquation: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val equations = listOf(
        Pair("نظرية فيثاغورس", "a² + b² = c²"),
        Pair("صيغة الدستور (المعادلة التربيعية)", "x = (-b ± √(b² - 4ac)) / (2a)"),
        Pair("مساحة الدائرة", "A = πr²"),
        Pair("تكامل محدد", "∫[a to b] f(x) dx"),
        Pair("متطابقة أويلر", "e^(iπ) + 1 = 0"),
        Pair("مجموع متتالية (سيجما)", "∑[i=1 to n] (x_i)"),
        Pair("كسر عام", "(a + b) / (c + d)"),
        Pair("نهاية دالة", "lim[x → 0] (sin(x)/x) = 1")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Functions,
                    contentDescription = null,
                    tint = OfficeWordBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("معادلة رياضية (Equation)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("اختر معادلة رياضية شائعة لإدراجها وتعديلها:", fontSize = 12.sp, color = Color.Gray)

                equations.forEach { (name, formula) ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OfficeRibbonBorder),
                        color = Color(0xFFFAFAFA),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectEquation(formula)
                                onDismiss()
                            }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OfficeWordBlue)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(formula, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = OfficeWordBlue)
            }
        }
    )
}

/**
 * 5. WordArt Dialog (نصوص مزخرفة بأسلوب WordArt الكلاسيكي والحديث)
 */
@Composable
fun WordArtDialog(
    onInsertWordArt: (text: String, style: String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("عنوان بارز") }
    var selectedStyle by remember { mutableStateOf("gold") }

    val styles = listOf(
        Pair("gold", "ذهبي ملكي ✨"),
        Pair("blue", "أزرق Microsoft Word 🔹"),
        Pair("box", "إطار فخم ═══"),
        Pair("star", "نجوم وبراق ✦")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = OfficeWordBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("إدراج WordArt (نص مزخرف)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("اكتب النص المراد زخرفته") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text("اختر نمط WordArt:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                styles.forEach { (key, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedStyle = key }
                            .padding(vertical = 4.dp)
                    ) {
                        androidx.compose.material3.RadioButton(
                            selected = selectedStyle == key,
                            onClick = { selectedStyle = key }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(label, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onInsertWordArt(text, selectedStyle)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = OfficeWordBlue)
            ) {
                Text("إدراج")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = Color.Gray)
            }
        }
    )
}

/**
 * 6. Cover Page Gallery Dialog (معرض قوالب صفحات الغلاف الجاهزة - Cover Pages Gallery)
 */
data class CoverPageTemplateItem(
    val id: String,
    val name: String,
    val description: String,
    val badge: String,
    val iconColor: Color,
    val previewBorder: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoverPageDialog(
    currentTitle: String,
    onInsertCover: (title: String, subtitle: String, author: String, style: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(currentTitle.ifBlank { "تقرير العمل الرسمي" }) }
    var subtitle by remember { mutableStateOf("إدارة وتخطيط المهام التنفيذية") }
    var author by remember { mutableStateOf("المهندس شاكر السفياني (770472829)") }
    var selectedStyle by remember { mutableStateOf("executive") }

    val templates = listOf(
        CoverPageTemplateItem(
            id = "executive",
            name = "تنفيذي رسمي (Executive)",
            description = "تصميم رسمي راقٍ للتقارير الإدارية ومستندات الشركات والمنظمات",
            badge = "الأكثر استخداماً",
            iconColor = Color(0xFF185ABD),
            previewBorder = "══════════"
        ),
        CoverPageTemplateItem(
            id = "modern",
            name = "عصري حديث (Modern)",
            description = "خطوط عصرية وتقسيم بيانات واضح للمؤسسات والمشاريع الرقمية",
            badge = "تقني",
            iconColor = Color(0xFF0F766E),
            previewBorder = "■■■■■■■■■■"
        ),
        CoverPageTemplateItem(
            id = "academic",
            name = "بحث جامعي / أكاديمي (Academic)",
            description = "ترويسة وزارة التعليم العالي والجامعات مع بيانات الباحث والإشراف",
            badge = "جامعي",
            iconColor = Color(0xFF4338CA),
            previewBorder = "━━━━━━━━━━"
        ),
        CoverPageTemplateItem(
            id = "project",
            name = "ميثاق وخطة مشروع (Project Plan)",
            description = "جدول بيانات حوكمة المشروع مع أسماء المسؤولين وتواريخ الاعتماد",
            badge = "إدارة مشاريع",
            iconColor = Color(0xFFB45309),
            previewBorder = "┌──────────┐"
        ),
        CoverPageTemplateItem(
            id = "legal",
            name = "عقد واتفاقية قانونية (Legal Contract)",
            description = "صياغة قانونية ورسمية لتوقيع العقود والاتفاقيات والمذكرات",
            badge = "قانوني",
            iconColor = Color(0xFF334155),
            previewBorder = "⚖️ ═══════ ⚖️"
        ),
        CoverPageTemplateItem(
            id = "creative",
            name = "إبداعي فني (Creative & Art)",
            description = "تنسيق مبهج ونجوم براقة للكتيبات والفعاليات والأفكار الإبداعية",
            badge = "إبداعي",
            iconColor = Color(0xFFC026D3),
            previewBorder = "✨ ✦ 🌟 ✦ ✨"
        )
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = OfficeWordBlue,
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("معرض قوالب صفحات الغلاف (Cover Pages)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("اختر قالباً جاهزاً بأسلوب Microsoft Word المعتمد", fontSize = 11.sp, color = Color.Gray)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "1. اختر نمط وتصميم الغلاف الجاهز:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OfficeWordBlue
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    templates.forEach { tmpl ->
                        val isSelected = selectedStyle == tmpl.id
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) OfficeWordBlue else OfficeRibbonBorder
                            ),
                            color = if (isSelected) Color(0xFFF0F6FF) else Color(0xFFFAFAFA),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStyle = tmpl.id }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.material3.RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedStyle = tmpl.id }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = tmpl.name,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) OfficeWordBlue else Color.Black
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = tmpl.iconColor.copy(alpha = 0.12f),
                                            modifier = Modifier.padding(start = 4.dp)
                                        ) {
                                            Text(
                                                text = tmpl.badge,
                                                fontSize = 9.sp,
                                                color = tmpl.iconColor,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = tmpl.description,
                                        fontSize = 10.sp,
                                        color = Color.DarkGray,
                                        lineHeight = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "معاينة الإطار: ${tmpl.previewBorder}",
                                        fontSize = 9.sp,
                                        color = tmpl.iconColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = OfficeRibbonBorder)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "2. بيانات ونصوص الغلاف المخصصة:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OfficeWordBlue
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان المستند الرئيسي") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("العنوان الفرعي أو الوصف") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("المؤلف / جهة الإصدار / المطور") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onInsertCover(title, subtitle, author, selectedStyle)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = OfficeWordBlue)
            ) {
                Text("إدراج الغلاف في المستند")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = Color.Gray)
            }
        }
    )
}

/**
 * 7. Header and Footer Dialog (رأس وتذييل الصفحة)
 */
@Composable
fun HeaderFooterDialog(
    isHeader: Boolean,
    onInsert: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var content by remember {
        mutableStateOf(if (isHeader) "وثيقة رسمية - معالج نصوص Word" else "صفحة رقم 1 - حقوق النشر محفوظة © 2026")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (isHeader) "إدراج رأس الصفحة (Header)" else "إدراج تذييل الصفحة (Footer)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isHeader) "اكتب النص الذي سيظهر في ترويسة المستند:" else "اكتب النص الذي سيظهر أسفل الصفحة:",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(if (isHeader) "نص الرأس" else "نص التذييل") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (content.isNotBlank()) {
                        onInsert(content)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = OfficeWordBlue)
            ) {
                Text("إدراج")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = Color.Gray)
            }
        }
    )
}

/**
 * 8. Picture Options Dialog (إدراج صورة من الاستوديو أو عينة توضيحية)
 */
@Composable
fun PictureOptionsDialog(
    onPickFromGallery: () -> Unit,
    onInsertStockPhoto: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    tint = OfficeWordBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("إدراج صورة (Insert Picture)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("اختر مصدر الصورة المراد إدراجها في المستند:", fontSize = 12.sp, color = Color.DarkGray)

                Button(
                    onClick = {
                        onPickFromGallery()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = OfficeWordBlue)
                ) {
                    Text("📷 اختيار صورة من جهازك (Photo Picker)")
                }

                HorizontalDivider(color = OfficeRibbonBorder)

                Text("أو اختر قالباً بيانياً سريعاً:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)

                OutlinedButton(
                    onClick = {
                        onInsertStockPhoto("مخطط بياني تقني")
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📊 إدراج قالب مخطط إحصائي")
                }

                OutlinedButton(
                    onClick = {
                        onInsertStockPhoto("شعار رسمي")
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🏛️ إدراج شعار رسمي")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = Color.Gray)
            }
        }
    )
}
