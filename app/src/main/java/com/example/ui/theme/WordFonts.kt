package com.example.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

data class WordFontItem(
    val id: String,
    val displayName: String,
    val arabicSample: String,
    val fontFamily: FontFamily
)

object WordFonts {
    val CairoFont = FontFamily(Font(R.font.cairo, FontWeight.Normal))
    val AmiriFont = FontFamily(Font(R.font.amiri, FontWeight.Normal))
    val TajawalFont = FontFamily(Font(R.font.tajawal, FontWeight.Normal))
    val AlmaraiFont = FontFamily(Font(R.font.almarai, FontWeight.Normal))
    val RobotoFont = FontFamily(Font(R.font.roboto, FontWeight.Normal))

    val availableFonts = listOf(
        WordFontItem(
            id = "cairo",
            displayName = "Cairo (خط القاهرة)",
            arabicSample = "بسم الله الرحمن الرحيم - خط رسمي عصري",
            fontFamily = CairoFont
        ),
        WordFontItem(
            id = "amiri",
            displayName = "Amiri (خط النسخ الأميري)",
            arabicSample = "بسم الله الرحمن الرحيم - خط كلاسيكي أصيل",
            fontFamily = AmiriFont
        ),
        WordFontItem(
            id = "tajawal",
            displayName = "Tajawal (خط تجوال)",
            arabicSample = "بسم الله الرحمن الرحيم - خط تقارير أنيق",
            fontFamily = TajawalFont
        ),
        WordFontItem(
            id = "almarai",
            displayName = "Almarai (خط المراعي)",
            arabicSample = "بسم الله الرحمن الرحيم - خط متناسق ومريح",
            fontFamily = AlmaraiFont
        ),
        WordFontItem(
            id = "roboto",
            displayName = "Roboto (Calibri / Arial)",
            arabicSample = "The quick brown fox jumps over the lazy dog",
            fontFamily = RobotoFont
        )
    )

    fun getFontFamily(fontIdOrName: String): FontFamily {
        val match = availableFonts.firstOrNull {
            it.id.equals(fontIdOrName, ignoreCase = true) ||
            it.displayName.contains(fontIdOrName, ignoreCase = true)
        }
        return match?.fontFamily ?: CairoFont
    }

    fun getDisplayName(fontIdOrName: String): String {
        val match = availableFonts.firstOrNull {
            it.id.equals(fontIdOrName, ignoreCase = true) ||
            it.displayName.contains(fontIdOrName, ignoreCase = true)
        }
        return match?.displayName ?: "Cairo (خط القاهرة)"
    }
}
