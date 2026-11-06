package com.example.util

import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.speech.tts.TextToSpeech
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DocumentStats(
    val words: Int,
    val charsWithSpaces: Int,
    val charsWithoutSpaces: Int,
    val paragraphs: Int,
    val readingTimeMinutes: Int
)

object DocumentUtils {

    fun calculateStats(text: String): DocumentStats {
        if (text.isBlank()) {
            return DocumentStats(0, 0, 0, 0, 0)
        }
        val words = text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        val wordCount = words.size
        val charsWithSpaces = text.length
        val charsWithoutSpaces = text.replace(Regex("\\s"), "").length
        val paragraphs = text.split("\n").count { it.isNotBlank() }
        val readingTime = (wordCount / 180).coerceAtLeast(1)

        return DocumentStats(
            words = wordCount,
            charsWithSpaces = charsWithSpaces,
            charsWithoutSpaces = charsWithoutSpaces,
            paragraphs = paragraphs,
            readingTimeMinutes = if (wordCount == 0) 0 else readingTime
        )
    }

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getCurrentDateFormatted(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Wrap selection with prefix and suffix (e.g. ** for bold)
    fun wrapSelection(value: TextFieldValue, prefix: String, suffix: String = prefix): TextFieldValue {
        val text = value.text
        val selection = value.selection
        return if (selection.collapsed) {
            // No selection: insert placeholder or just tags
            val newText = text.substring(0, selection.start) + prefix + suffix + text.substring(selection.end)
            val newCursorPos = selection.start + prefix.length
            TextFieldValue(newText, TextRange(newCursorPos))
        } else {
            val selectedText = text.substring(selection.start, selection.end)
            val newText = text.substring(0, selection.start) + prefix + selectedText + suffix + text.substring(selection.end)
            val newSelection = TextRange(selection.start + prefix.length, selection.end + prefix.length)
            TextFieldValue(newText, newSelection)
        }
    }

    // Toggle prefix at line start (e.g. # for Heading 1, * for bullets)
    fun toggleLinePrefix(value: TextFieldValue, prefix: String): TextFieldValue {
        val text = value.text
        val selection = value.selection
        val lineStart = text.lastIndexOf('\n', (selection.start - 1).coerceAtLeast(0)).let {
            if (it == -1) 0 else it + 1
        }
        val lineEnd = text.indexOf('\n', selection.start).let {
            if (it == -1) text.length else it
        }
        val currentLine = text.substring(lineStart, lineEnd)

        val updatedLine = if (currentLine.startsWith(prefix)) {
            currentLine.removePrefix(prefix)
        } else {
            // Remove existing heading/list markers if any
            val cleaned = currentLine.replace(Regex("^(#{1,6}\\s|\\*\\s|\\d+\\.\\s|-\\s\\[[\\sxX]\\]\\s|>\\s)"), "")
            prefix + cleaned
        }

        val newText = text.substring(0, lineStart) + updatedLine + text.substring(lineEnd)
        val diff = updatedLine.length - currentLine.length
        val newPos = (selection.start + diff).coerceIn(0, newText.length)
        return TextFieldValue(newText, TextRange(newPos))
    }

    fun insertTextAtCursor(value: TextFieldValue, insertText: String): TextFieldValue {
        val text = value.text
        val selection = value.selection
        val newText = text.substring(0, selection.start) + insertText + text.substring(selection.end)
        val newCursor = selection.start + insertText.length
        return TextFieldValue(newText, TextRange(newCursor))
    }

    fun shareDocument(context: Context, title: String, content: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$content")
        }
        context.startActivity(Intent.createChooser(shareIntent, "مشاركة المستند"))
    }

    fun printOrExportPdf(context: Context, title: String, content: String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
        val webView = WebView(context)
        
        // Convert basic markdown to neat printable HTML
        val htmlBody = convertToPrintHtml(title, content)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printAdapter = webView.createPrintDocumentAdapter(title)
                val jobName = "طباعة $title"
                printManager.print(
                    jobName,
                    printAdapter,
                    PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build()
                )
            }
        }
        webView.loadDataWithBaseURL(null, htmlBody, "text/html; charset=utf-8", "UTF-8", null)
    }

    private fun convertToPrintHtml(title: String, content: String): String {
        val safeContent = content
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")

        // Convert basic markdown to styled HTML
        var formatted = safeContent
            .replace(Regex("^# (.*?)$", RegexOption.MULTILINE), "<h1>$1</h1>")
            .replace(Regex("^## (.*?)$", RegexOption.MULTILINE), "<h2>$1</h2>")
            .replace(Regex("^### (.*?)$", RegexOption.MULTILINE), "<h3>$1</h3>")
            .replace(Regex("^#### (.*?)$", RegexOption.MULTILINE), "<h4>$1</h4>")
            .replace(Regex("\\*\\*(.*?)\\*\\*"), "<strong>$1</strong>")
            .replace(Regex("\\*(.*?)\\*"), "<em>$1</em>")
            .replace(Regex("<u>(.*?)<\\/u>"), "<u>$1</u>")
            .replace(Regex("~~(.*?)~~"), "<del>$1</del>")
            .replace(Regex("^> (.*?)$", RegexOption.MULTILINE), "<blockquote>$1</blockquote>")
            .replace(Regex("^---$", RegexOption.MULTILINE), "<hr/>")
            .replace(Regex("^\\* (.*?)$", RegexOption.MULTILINE), "<li>$1</li>")
            .replace(Regex("^\\d+\\.\\s(.*?)$", RegexOption.MULTILINE), "<li>$1</li>")
            .replace("\n", "<br/>")

        return """
            <!DOCTYPE html>
            <html dir="rtl" lang="ar">
            <head>
                <meta charset="utf-8">
                <title>$title</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif, 'Amiri';
                        line-height: 1.8;
                        padding: 30px;
                        color: #1a1a1a;
                        direction: rtl;
                    }
                    h1 { color: #104C91; border-bottom: 2px solid #104C91; padding-bottom: 8px; font-size: 26px; }
                    h2 { color: #185ABD; margin-top: 20px; font-size: 22px; }
                    h3 { color: #2B88D8; margin-top: 16px; font-size: 18px; }
                    blockquote {
                        border-right: 4px solid #104C91;
                        padding-right: 15px;
                        margin: 15px 0;
                        color: #555;
                        background: #f8f9fa;
                        padding: 10px;
                    }
                    hr { border: 0; height: 1px; background: #ddd; margin: 25px 0; }
                    table { width: 100%; border-collapse: collapse; margin: 20px 0; }
                    th, td { border: 1px solid #ddd; padding: 10px; text-align: right; }
                    th { background-color: #E3EDF8; color: #08274B; }
                    del { color: #888; }
                </style>
            </head>
            <body>
                $formatted
            </body>
            </html>
        """.trimIndent()
    }
}
