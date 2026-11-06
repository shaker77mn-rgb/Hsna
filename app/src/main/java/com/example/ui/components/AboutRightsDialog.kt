package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OfficeWordBlue
import com.example.ui.theme.OfficeWordDarkBlue

@Composable
fun AboutRightsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = OfficeWordBlue,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "W",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "حول Word والحقوق",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "إصدار سطح المكتب المكتبي",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF605E5C)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Developer Card (بطاقة المطور)
                Surface(
                    color = Color(0xFFDCEAF9),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OfficeWordBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = OfficeWordBlue,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "المهندس شاكر السفياني",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = OfficeWordDarkBlue
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.Default.Verified,
                                        contentDescription = "معتمد",
                                        tint = OfficeWordBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = "مطور ومصمم التطبيق",
                                    fontSize = 11.sp,
                                    color = Color(0xFF444444)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = OfficeWordBlue.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Phone Contact Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Call,
                                    contentDescription = null,
                                    tint = OfficeWordBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "رقم الهاتف / التواصل:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF333333)
                                )
                            }
                            Text(
                                text = "770472829",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OfficeWordDarkBlue
                            )
                        }
                    }
                }

                // Features & Rights Summary
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "مميزات الإصدار المكتبي:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF201F1E)
                    )
                    Text("• شريط ريبون Office PC الكامل مع أدوات التنسيق", fontSize = 11.sp, color = Color(0xFF555555))
                    Text("• مسطرة Word الأفقية بالسنتيمتر مع الهوامش", fontSize = 11.sp, color = Color(0xFF555555))
                    Text("• خطوط عربية أصلية (Cairo، Amiri، Tajawal، Almarai)", fontSize = 11.sp, color = Color(0xFF555555))
                    Text("• تبديل اتجاه الكتابة: من اليمين لليسار (RTL) / من اليسار لليمين (LTR)", fontSize = 11.sp, color = Color(0xFF555555))
                    Text("• لوحة مفاتيح كمبيوتر كاملة مع جميع أزرار الـ PC", fontSize = 11.sp, color = Color(0xFF555555))
                }

                // Copyright Statement
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Copyright,
                        contentDescription = null,
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "جميع الحقوق محفوظة © 2026 - م. شاكر السفياني",
                        fontSize = 11.sp,
                        color = Color(0xFF777777),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.testTag("dismiss_about_dialog")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("حسناً")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
