package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.DocumentEntity
import com.example.data.DocumentTemplate
import com.example.data.DocumentTemplates
import com.example.ui.theme.OfficeWordDarkBlue
import com.example.util.DocumentStats
import com.example.util.DocumentUtils

@Composable
fun DesktopBackstageView(
    document: DocumentEntity,
    stats: DocumentStats,
    onCloseBackstage: () -> Unit,
    onNewTemplate: (DocumentTemplate) -> Unit,
    onSaveClick: () -> Unit,
    onPrintClick: () -> Unit,
    onShareClick: () -> Unit,
    onCloseDocClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMenu by remember { mutableStateOf("info") }

    Row(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
    ) {
        // Left / Right Navigation Sidebar (Classic Word Backstage Blue)
        Surface(
            modifier = Modifier
                .width(180.dp)
                .fillMaxHeight(),
            color = OfficeWordDarkBlue
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp)
            ) {
                // Return to document button (circle arrow)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onCloseBackstage)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("backstage_return_btn")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0x33FFFFFF),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع إلى المستند",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("رجوع", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0x22FFFFFF), thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                BackstageMenuItem(
                    icon = Icons.Default.Info,
                    label = "معلومات",
                    isSelected = selectedMenu == "info",
                    onClick = { selectedMenu = "info" }
                )

                BackstageMenuItem(
                    icon = Icons.Default.NoteAdd,
                    label = "جديد (قوالب)",
                    isSelected = selectedMenu == "new",
                    onClick = { selectedMenu = "new" }
                )

                BackstageMenuItem(
                    icon = Icons.Default.Save,
                    label = "حفظ",
                    isSelected = false,
                    onClick = {
                        onSaveClick()
                        onCloseBackstage()
                    }
                )

                BackstageMenuItem(
                    icon = Icons.Default.Print,
                    label = "طباعة و PDF",
                    isSelected = false,
                    onClick = {
                        onCloseBackstage()
                        onPrintClick()
                    }
                )

                BackstageMenuItem(
                    icon = Icons.Default.Share,
                    label = "مشاركة",
                    isSelected = false,
                    onClick = {
                        onCloseBackstage()
                        onShareClick()
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(color = Color(0x22FFFFFF), thickness = 1.dp)

                BackstageMenuItem(
                    icon = Icons.Default.Close,
                    label = "إغلاق المستند",
                    isSelected = false,
                    onClick = onCloseDocClick
                )
            }
        }

        // Right Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFFF9FAFB))
                .padding(24.dp)
        ) {
            when (selectedMenu) {
                "info" -> BackstageInfoPanel(document = document, stats = stats)
                "new" -> BackstageNewPanel(onSelectTemplate = {
                    onCloseBackstage()
                    onNewTemplate(it)
                })
            }
        }
    }
}

@Composable
private fun BackstageMenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0x33FFFFFF) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun BackstageInfoPanel(
    document: DocumentEntity,
    stats: DocumentStats
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "معلومات المستند",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF201F1E)
        )
        Text(
            text = document.title,
            fontSize = 14.sp,
            color = Color(0xFF605E5C)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "الخصائص الأساسية (Properties)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = OfficeWordDarkBlue
                )
                HorizontalDivider(color = Color(0xFFE5E7EB))

                InfoRow(label = "الحجم والكلمات", value = "${stats.words} كلمة (${stats.charsWithSpaces} حرف)")
                InfoRow(label = "عدد الفقرات", value = "${stats.paragraphs} فقرة")
                InfoRow(label = "وقت القراءة الإجمالي", value = "${stats.readingTimeMinutes} دقيقة تقريباً")
                InfoRow(label = "تاريخ الإنشاء", value = DocumentUtils.formatDate(document.createdAt))
                InfoRow(label = "آخر تعديل", value = DocumentUtils.formatDate(document.updatedAt))
                InfoRow(label = "التصنيف", value = document.category)

                HorizontalDivider(color = Color(0xFFE5E7EB))

                Text(
                    text = "حقوق التطوير والملكية",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = OfficeWordDarkBlue
                )
                InfoRow(label = "تطوير وبرمجة", value = "المهندس شاكر السفياني")
                InfoRow(label = "هاتف التواصل والدعم", value = "770472829")
                InfoRow(label = "الحقوق", value = "جميع الحقوق محفوظة © 2026")
            }
        }
    }
}

@Composable
private fun BackstageNewPanel(
    onSelectTemplate: (DocumentTemplate) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "إنشاء مستند جديد من القوالب",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF201F1E)
        )
        Text(
            text = "اختر أحد قوالب Office المعتمدة للبدء السريع",
            fontSize = 13.sp,
            color = Color(0xFF605E5C)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(DocumentTemplates.templates) { template ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectTemplate(template) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFDCEAF9),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Description,
                                    contentDescription = null,
                                    tint = OfficeWordDarkBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = template.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = template.summary,
                            fontSize = 10.sp,
                            color = Color(0xFF605E5C),
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color(0xFF605E5C))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF201F1E))
    }
}
