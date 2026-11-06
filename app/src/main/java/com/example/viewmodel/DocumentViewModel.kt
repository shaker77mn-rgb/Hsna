package com.example.viewmodel

import android.app.Application
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DocumentEntity
import com.example.data.DocumentRepository
import com.example.data.DocumentTemplate
import com.example.data.DocumentTemplates
import com.example.util.DocumentStats
import com.example.util.DocumentUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OutlineItem(
    val title: String,
    val level: Int,
    val position: Int
)

class DocumentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DocumentRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DocumentRepository(database.documentDao())
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Navigation state: "list" or "editor"
    private val _currentScreen = MutableStateFlow("list")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Filter & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("الكل")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isGridView = MutableStateFlow(false)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    val documentsList: StateFlow<List<DocumentEntity>> = combine(
        repository.allDocuments,
        _searchQuery,
        _selectedCategory
    ) { docs, query, category ->
        docs.filter { doc ->
            val matchesQuery = query.isBlank() ||
                doc.title.contains(query, ignoreCase = true) ||
                doc.content.contains(query, ignoreCase = true)
            val matchesCategory = when (category) {
                "الكل" -> true
                "المفضلة" -> doc.isFavorite
                else -> doc.category == category
            }
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Document in Editor
    private val _activeDocument = MutableStateFlow<DocumentEntity?>(null)
    val activeDocument: StateFlow<DocumentEntity?> = _activeDocument.asStateFlow()

    private val _editorTitle = MutableStateFlow("")
    val editorTitle: StateFlow<String> = _editorTitle.asStateFlow()

    private val _editorTextValue = MutableStateFlow(TextFieldValue(""))
    val editorTextValue: StateFlow<TextFieldValue> = _editorTextValue.asStateFlow()

    private val _documentStats = MutableStateFlow(DocumentStats(0, 0, 0, 0, 0))
    val documentStats: StateFlow<DocumentStats> = _documentStats.asStateFlow()

    // Undo / Redo
    private val undoStack = mutableListOf<TextFieldValue>()
    private val redoStack = mutableListOf<TextFieldValue>()
    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()
    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    // View Modes
    private val _isPageMode = MutableStateFlow(true)
    val isPageMode: StateFlow<Boolean> = _isPageMode.asStateFlow()

    private val _fontSize = MutableStateFlow(16)
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()

    // Desktop Word ribbon tab: 0=Home, 1=Insert, 2=Layout, 3=Review, 4=View
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Desktop Word features
    private val _fontFamily = MutableStateFlow("Calibri")
    val fontFamily: StateFlow<String> = _fontFamily.asStateFlow()

    private val _zoomPercent = MutableStateFlow(100)
    val zoomPercent: StateFlow<Int> = _zoomPercent.asStateFlow()

    private val _isRulerVisible = MutableStateFlow(true)
    val isRulerVisible: StateFlow<Boolean> = _isRulerVisible.asStateFlow()

    private val _isBackstageOpen = MutableStateFlow(false)
    val isBackstageOpen: StateFlow<Boolean> = _isBackstageOpen.asStateFlow()

    private val _isReadMode = MutableStateFlow(false)
    val isReadMode: StateFlow<Boolean> = _isReadMode.asStateFlow()

    private val _marginType = MutableStateFlow("عادية") // عادية, ضيقة, عريضة
    val marginType: StateFlow<String> = _marginType.asStateFlow()

    private val _pageOrientation = MutableStateFlow("عمودي") // عمودي, أفقي
    val pageOrientation: StateFlow<String> = _pageOrientation.asStateFlow()

    // RTL / LTR Document Direction
    private val _textDirection = MutableStateFlow("rtl") // "rtl" (من اليمين لليسار) or "ltr" (من اليسار لليمين)
    val textDirection: StateFlow<String> = _textDirection.asStateFlow()

    // Optional PC Keyboard state
    private val _isPcKeyboardVisible = MutableStateFlow(false)
    val isPcKeyboardVisible: StateFlow<Boolean> = _isPcKeyboardVisible.asStateFlow()

    private val _pcKeyboardLang = MutableStateFlow("ar") // "ar" or "en"
    val pcKeyboardLang: StateFlow<String> = _pcKeyboardLang.asStateFlow()

    private val _isPcShiftActive = MutableStateFlow(false)
    val isPcShiftActive: StateFlow<Boolean> = _isPcShiftActive.asStateFlow()

    private val _isPcCtrlActive = MutableStateFlow(false)
    val isPcCtrlActive: StateFlow<Boolean> = _isPcCtrlActive.asStateFlow()

    // Find & Replace
    private val _isFindReplaceOpen = MutableStateFlow(false)
    val isFindReplaceOpen: StateFlow<Boolean> = _isFindReplaceOpen.asStateFlow()
    private val _findQuery = MutableStateFlow("")
    val findQuery: StateFlow<String> = _findQuery.asStateFlow()
    private val _replaceQuery = MutableStateFlow("")
    val replaceQuery: StateFlow<String> = _replaceQuery.asStateFlow()
    private val _matchesCount = MutableStateFlow(0)
    val matchesCount: StateFlow<Int> = _matchesCount.asStateFlow()

    // Document Outline
    private val _outlineItems = MutableStateFlow<List<OutlineItem>>(emptyList())
    val outlineItems: StateFlow<List<OutlineItem>> = _outlineItems.asStateFlow()

    // Save indicator
    private val _isSaved = MutableStateFlow(true)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    // Auto-save debounce job
    private var autoSaveJob: Job? = null

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun togglePageMode() {
        _isPageMode.value = !_isPageMode.value
        _activeDocument.value?.let { doc ->
            viewModelScope.launch {
                repository.update(doc.copy(isPageMode = _isPageMode.value))
            }
        }
    }

    fun setFontFamily(font: String) {
        _fontFamily.value = font
    }

    fun setZoomPercent(percent: Int) {
        _zoomPercent.value = percent.coerceIn(50, 200)
    }

    fun zoomIn() {
        setZoomPercent(_zoomPercent.value + 10)
    }

    fun zoomOut() {
        setZoomPercent(_zoomPercent.value - 10)
    }

    fun toggleRuler() {
        _isRulerVisible.value = !_isRulerVisible.value
    }

    fun toggleBackstage(open: Boolean? = null) {
        _isBackstageOpen.value = open ?: !_isBackstageOpen.value
    }

    fun toggleReadMode() {
        _isReadMode.value = !_isReadMode.value
    }

    fun setMarginType(type: String) {
        _marginType.value = type
    }

    fun setPageOrientation(orientation: String) {
        _pageOrientation.value = orientation
    }

    fun setTextDirection(dir: String) {
        _textDirection.value = dir
    }

    fun togglePcKeyboard(show: Boolean? = null) {
        _isPcKeyboardVisible.value = show ?: !_isPcKeyboardVisible.value
    }

    fun togglePcKeyboardLang() {
        _pcKeyboardLang.value = if (_pcKeyboardLang.value == "ar") "en" else "ar"
    }

    fun togglePcShift() {
        _isPcShiftActive.value = !_isPcShiftActive.value
    }

    fun togglePcCtrl() {
        _isPcCtrlActive.value = !_isPcCtrlActive.value
    }

    fun onPcKeyPress(key: String) {
        val current = _editorTextValue.value
        val text = current.text
        val selection = current.selection

        // Check if Ctrl is active for shortcuts
        if (_isPcCtrlActive.value) {
            _isPcCtrlActive.value = false
            when (key.uppercase()) {
                "Z" -> { undo(); return }
                "Y" -> { redo(); return }
                "B" -> { formatWrap("**"); return }
                "I" -> { formatWrap("*"); return }
                "U" -> { formatWrap("<u>", "</u>"); return }
                "S" -> { saveActiveDocument(); return }
                "A" -> {
                    _editorTextValue.value = current.copy(selection = TextRange(0, text.length))
                    return
                }
            }
        }

        when (key) {
            "Backspace" -> {
                if (!selection.collapsed) {
                    val newText = text.substring(0, selection.start) + text.substring(selection.end)
                    onContentChange(TextFieldValue(newText, TextRange(selection.start)))
                } else if (selection.start > 0) {
                    val newText = text.substring(0, selection.start - 1) + text.substring(selection.start)
                    onContentChange(TextFieldValue(newText, TextRange(selection.start - 1)))
                }
            }
            "Enter" -> {
                val newText = text.substring(0, selection.start) + "\n" + text.substring(selection.end)
                onContentChange(TextFieldValue(newText, TextRange(selection.start + 1)))
            }
            "Space" -> {
                val newText = text.substring(0, selection.start) + " " + text.substring(selection.end)
                onContentChange(TextFieldValue(newText, TextRange(selection.start + 1)))
            }
            "Tab" -> {
                val newText = text.substring(0, selection.start) + "    " + text.substring(selection.end)
                onContentChange(TextFieldValue(newText, TextRange(selection.start + 4)))
            }
            "ArrowLeft" -> {
                val newPos = (selection.start - 1).coerceAtLeast(0)
                _editorTextValue.value = current.copy(selection = TextRange(newPos))
            }
            "ArrowRight" -> {
                val newPos = (selection.start + 1).coerceAtMost(text.length)
                _editorTextValue.value = current.copy(selection = TextRange(newPos))
            }
            "ArrowUp" -> {
                val lastNewline = text.lastIndexOf('\n', (selection.start - 1).coerceAtLeast(0))
                if (lastNewline != -1) {
                    val prevLineStart = text.lastIndexOf('\n', (lastNewline - 1).coerceAtLeast(0)).let {
                        if (it == -1) 0 else it + 1
                    }
                    val col = selection.start - lastNewline - 1
                    val newPos = (prevLineStart + col).coerceAtMost(lastNewline)
                    _editorTextValue.value = current.copy(selection = TextRange(newPos))
                }
            }
            "ArrowDown" -> {
                val nextNewline = text.indexOf('\n', selection.start)
                if (nextNewline != -1) {
                    val lineStart = text.lastIndexOf('\n', (selection.start - 1).coerceAtLeast(0)).let {
                        if (it == -1) 0 else it + 1
                    }
                    val col = selection.start - lineStart
                    val afterNextNewline = nextNewline + 1
                    val endOfNextLine = text.indexOf('\n', afterNextNewline).let {
                        if (it == -1) text.length else it
                    }
                    val newPos = (afterNextNewline + col).coerceAtMost(endOfNextLine)
                    _editorTextValue.value = current.copy(selection = TextRange(newPos))
                }
            }
            "Del" -> {
                if (!selection.collapsed) {
                    val newText = text.substring(0, selection.start) + text.substring(selection.end)
                    onContentChange(TextFieldValue(newText, TextRange(selection.start)))
                } else if (selection.start < text.length) {
                    val newText = text.substring(0, selection.start) + text.substring(selection.start + 1)
                    onContentChange(TextFieldValue(newText, TextRange(selection.start)))
                }
            }
            else -> {
                val charToInsert = if (_isPcShiftActive.value) key.uppercase() else key
                val newText = text.substring(0, selection.start) + charToInsert + text.substring(selection.end)
                onContentChange(TextFieldValue(newText, TextRange(selection.start + charToInsert.length)))
                if (_isPcShiftActive.value) {
                    _isPcShiftActive.value = false
                }
            }
        }
    }

    fun setFontSize(size: Int) {
        _fontSize.value = size.coerceIn(12, 36)
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun toggleFindReplace(show: Boolean? = null) {
        _isFindReplaceOpen.value = show ?: !_isFindReplaceOpen.value
        if (!_isFindReplaceOpen.value) {
            _findQuery.value = ""
            _replaceQuery.value = ""
            _matchesCount.value = 0
        }
    }

    fun openDocument(doc: DocumentEntity) {
        _activeDocument.value = doc
        _editorTitle.value = doc.title
        val initialValue = TextFieldValue(doc.content, TextRange(doc.content.length))
        _editorTextValue.value = initialValue
        _isPageMode.value = doc.isPageMode
        _fontSize.value = doc.fontSize
        _documentStats.value = DocumentUtils.calculateStats(doc.content)
        extractOutline(doc.content)

        undoStack.clear()
        redoStack.clear()
        undoStack.add(initialValue)
        _canUndo.value = false
        _canRedo.value = false
        _isSaved.value = true

        _currentScreen.value = "editor"
    }

    fun createNewDocument(template: DocumentTemplate? = null) {
        val title = template?.title ?: "مستند جديد بدون عنوان"
        val content = template?.content ?: "# مستند جديد\n\nاكتب هنا..."
        val category = template?.category ?: "عام"
        val stats = DocumentUtils.calculateStats(content)

        val newDoc = DocumentEntity(
            title = title,
            content = content,
            category = category,
            wordCount = stats.words,
            charCount = stats.charsWithSpaces,
            isPageMode = true,
            fontSize = 16
        )

        viewModelScope.launch {
            val id = repository.insert(newDoc)
            val created = repository.getDocumentOnce(id) ?: newDoc.copy(id = id)
            openDocument(created)
        }
    }

    fun onTitleChange(newTitle: String) {
        _editorTitle.value = newTitle
        _isSaved.value = false
        triggerAutoSave()
    }

    fun onContentChange(newValue: TextFieldValue) {
        if (_editorTextValue.value.text != newValue.text) {
            // Push to undo stack
            if (undoStack.size > 50) undoStack.removeAt(0)
            undoStack.add(_editorTextValue.value)
            redoStack.clear()
            _canUndo.value = true
            _canRedo.value = false

            _isSaved.value = false
            _documentStats.value = DocumentUtils.calculateStats(newValue.text)
            extractOutline(newValue.text)
            triggerAutoSave()
        }
        _editorTextValue.value = newValue
    }

    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(800) // 800ms debounce
            saveActiveDocument()
        }
    }

    fun saveActiveDocument() {
        val doc = _activeDocument.value ?: return
        val currentTitle = _editorTitle.value.ifBlank { "مستند بدون عنوان" }
        val currentContent = _editorTextValue.value.text
        val stats = DocumentUtils.calculateStats(currentContent)

        val updated = doc.copy(
            title = currentTitle,
            content = currentContent,
            wordCount = stats.words,
            charCount = stats.charsWithSpaces,
            fontSize = _fontSize.value,
            isPageMode = _isPageMode.value,
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.update(updated)
            _activeDocument.value = updated
            _isSaved.value = true
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val previous = undoStack.removeAt(undoStack.lastIndex)
            redoStack.add(_editorTextValue.value)
            _editorTextValue.value = previous
            _canUndo.value = undoStack.isNotEmpty()
            _canRedo.value = true
            _documentStats.value = DocumentUtils.calculateStats(previous.text)
            extractOutline(previous.text)
            triggerAutoSave()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val next = redoStack.removeAt(redoStack.lastIndex)
            undoStack.add(_editorTextValue.value)
            _editorTextValue.value = next
            _canUndo.value = true
            _canRedo.value = redoStack.isNotEmpty()
            _documentStats.value = DocumentUtils.calculateStats(next.text)
            extractOutline(next.text)
            triggerAutoSave()
        }
    }

    // Formatting Toolbar Actions
    fun formatWrap(prefix: String, suffix: String = prefix) {
        val current = _editorTextValue.value
        val formatted = DocumentUtils.wrapSelection(current, prefix, suffix)
        onContentChange(formatted)
    }

    fun formatHeading(level: Int) {
        val prefix = when (level) {
            1 -> "# "
            2 -> "## "
            3 -> "### "
            4 -> "#### "
            else -> ""
        }
        val current = _editorTextValue.value
        val formatted = DocumentUtils.toggleLinePrefix(current, prefix)
        onContentChange(formatted)
    }

    fun formatList(type: String) {
        val prefix = when (type) {
            "bullet" -> "* "
            "number" -> "1. "
            "check" -> "- [ ] "
            "quote" -> "> "
            else -> ""
        }
        val current = _editorTextValue.value
        val formatted = DocumentUtils.toggleLinePrefix(current, prefix)
        onContentChange(formatted)
    }

    fun insertTable() {
        insertCustomTable(3, 3)
    }

    fun insertDivider() {
        val divider = "\n\n---\n\n"
        val current = _editorTextValue.value
        val formatted = DocumentUtils.insertTextAtCursor(current, divider)
        onContentChange(formatted)
    }

    fun insertDate() {
        val dateText = " ${DocumentUtils.getCurrentDateFormatted()} "
        val current = _editorTextValue.value
        val formatted = DocumentUtils.insertTextAtCursor(current, dateText)
        onContentChange(formatted)
    }

    fun insertCustomText(customText: String) {
        val current = _editorTextValue.value
        val formatted = DocumentUtils.insertTextAtCursor(current, customText)
        onContentChange(formatted)
    }

    fun insertCustomTable(rows: Int, cols: Int) {
        val sb = StringBuilder("\n\n")
        sb.append("|")
        for (c in 1..cols) {
            sb.append(" العمود $c |")
        }
        sb.append("\n|")
        for (c in 1..cols) {
            sb.append("---|")
        }
        sb.append("\n")
        for (r in 1..rows) {
            sb.append("|")
            for (c in 1..cols) {
                sb.append(" خلية $r-$c |")
            }
            sb.append("\n")
        }
        sb.append("\n")
        insertCustomText(sb.toString())
    }

    fun insertCoverPage(docTitle: String, docSubtitle: String, author: String, templateStyle: String = "executive") {
        val date = DocumentUtils.getCurrentDateFormatted()
        val cover = when (templateStyle) {
            "modern" -> """
                
                ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
                # $docTitle
                ### ❖ $docSubtitle
                ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
                
                > **إعداد المستند بواسطة:** $author
                > **تاريخ التقرير:** $date
                > **هاتف التواصل:** 770472829
                > **المؤسسة / الإدارة:** الإدارة العامة والمشاريع
                
                ---
                *مستند رسمي صادر عبر نظام Microsoft Word - تطوير المهندس شاكر السفياني*
                
                ══════════════════ [ فاصل صفحات ] ══════════════════
                
                
            """.trimIndent()
            "academic" -> """
                
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                ### الجمهورية اليمنية
                ### وزارة التعليم العالي والبحث العلمي
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                
                
                # $docTitle
                ### $docSubtitle
                
                
                **بحث / دراسة علمية مقدمة من الباحث:**
                **الاسم:** $author
                **التخصص:** هندسة برمجيات ونظم المعلومات
                **التاريخ الأكاديمي:** $date
                **إشراف وتطوير:** م. شاكر السفياني (770472829)
                
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                *العام الجامعي 1447 هـ / 2026 م*
                
                ══════════════════ [ فاصل صفحات ] ══════════════════
                
                
            """.trimIndent()
            "project" -> """
                
                ┌───────────────────────────────────────────────────┐
                │             وثيقة خطة وإدارة المشروع              │
                └───────────────────────────────────────────────────┘
                
                # 🚀 $docTitle
                ### 📋 $docSubtitle
                
                ---
                
                | بند المشروع | التفاصيل المعتمدة |
                |---|---|
                | **اسم المشروع** | $docTitle |
                | **مدير المشروع والمطور** | $author |
                | **رقم الاتصال** | 770472829 |
                | **تاريخ البدء والاعتماد** | $date |
                | **حالة المشروع** | قيد التنفيذ والمتابعة |
                
                ---
                
                > 📌 **ملحوظة:** هذه الوثيقة معتمدة رسمياً ولا يجوز تعديلها دون إذن مسبق.
                
                ══════════════════ [ فاصل صفحات ] ══════════════════
                
                
            """.trimIndent()
            "legal" -> """
                
                ═════════════════════════════════════════════════════
                ⚖️ **عقد واتفاقية عمل رسمية** ⚖️
                ═════════════════════════════════════════════════════
                
                # $docTitle
                ### $docSubtitle
                
                **إنه في يوم:** $date
                **تم الاتفاق والتعاقد بين كلاً من:**
                * **الطرف الأول (المطور والمنفذ):** $author - هاتف: 770472829
                * **الطرف الثاني:** الجهة المعتمدة للمستند
                
                ─────────────────────────────────────────────────────
                *حقوق الصياغة والتنفيذ محفوظة للمهندس شاكر السفياني © 2026*
                
                ══════════════════ [ فاصل صفحات ] ══════════════════
                
                
            """.trimIndent()
            "creative" -> """
                
                ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨
                
                # 🌟 $docTitle 🌟
                ### ✦ $docSubtitle ✦
                
                ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨ ✨
                
                **صُمم وأُعد بعناية بواسطة:** $author
                **التاريخ الفني:** $date
                **للتواصل والاستفسار:** 770472829
                
                [ إبداع تقني وتصميم معتمد - معالج نصوص Microsoft Word ]
                
                ══════════════════ [ فاصل صفحات ] ══════════════════
                
                
            """.trimIndent()
            else -> """
                
                ═══════════════════════════════════════════════════
                # $docTitle
                ### $docSubtitle
                ───────────────────────────────────────────────────
                *إعداد وتطوير:* $author
                *التاريخ:* $date
                *رقم التواصل:* 770472829
                ═══════════════════════════════════════════════════
                
                [ صفحة الغلاف التنفيذية الرسمية - Microsoft Word ]
                
                ══════════════════ [ فاصل صفحات ] ══════════════════
                
                
            """.trimIndent()
        }
        val current = _editorTextValue.value
        val newText = cover + current.text
        onContentChange(TextFieldValue(newText, TextRange(cover.length)))
    }

    fun insertPageBreak() {
        insertCustomText("\n\n══════════════════ [ فاصل صفحات ] ══════════════════\n\n")
    }

    fun insertSignatureLine(name: String = "المهندس شاكر السفياني", phone: String = "770472829") {
        val sig = """
            
            _____________________________________________
            **توقيع المسؤول:** $name
            **الهاتف:** $phone
            **التاريخ:** ${DocumentUtils.getCurrentDateFormatted()}
            **التوقيع الرسمي:** _________________________
            
        """.trimIndent()
        insertCustomText(sig)
    }

    fun insertShape(shapeType: String) {
        val shapeSnippet = when (shapeType) {
            "rect" -> "\n\n┌──────────────────────────────┐\n│         مربع نص شكل         │\n└──────────────────────────────┘\n\n"
            "box" -> "\n\n> ╔════════════════════════════════╗\n> ║  ⚠️ إطار تنبيه وشكل توضيحي     ║\n> ╚════════════════════════════════╝\n\n"
            "arrow_right" -> " ➔ "
            "arrow_left" -> " ⬅ "
            "arrow_up" -> " ⬆ "
            "arrow_down" -> " ⬇ "
            "star" -> " ★★★★★ "
            "check" -> " ✔ [تم الإنجاز بنجاح] "
            "note" -> "\n\n> 📌 **ملاحظة توضيحية هامة:**\n> اكتب تفاصيل الملاحظة هنا...\n\n"
            "quote_card" -> "\n\n> ❝\n> **اقتباس مميز أو حكمة:**\n> اكتب نص الاقتباس هنا...\n> ❞\n\n"
            else -> " ■ "
        }
        insertCustomText(shapeSnippet)
    }

    fun insertImagePlaceholder(imageName: String = "صورة توضيحية") {
        val imgTag = "\n\n![🖼️ $imageName](https://via.placeholder.com/600x300.png?text=$imageName)\n*شكل توضيحي: $imageName*\n\n"
        insertCustomText(imgTag)
    }

    fun insertImageUri(uriString: String, caption: String = "صورة مدرجة") {
        val imgTag = "\n\n![🖼️ $caption]($uriString)\n*شكل: $caption*\n\n"
        insertCustomText(imgTag)
    }

    fun insertEquation(eq: String) {
        insertCustomText(" $eq ")
    }

    fun insertSymbol(symbol: String) {
        insertCustomText(" $symbol ")
    }

    fun insertHeader(headerText: String) {
        insertCustomText("\n\n### 🏷️ ترويسة علوية: $headerText\n---\n")
    }

    fun insertFooter(footerText: String) {
        insertCustomText("\n---\n*📄 تذييل الصفحة: $footerText*\n\n")
    }

    fun insertWordArt(text: String, artStyle: String) {
        val formattedArt = when (artStyle) {
            "gold" -> "\n\n## ✨ ==**$text**== ✨\n\n"
            "blue" -> "\n\n# 🔹 <span style=\"color:#185ABD\">**$text**</span> 🔹\n\n"
            "box" -> "\n\n# ═══【 $text 】═══\n\n"
            else -> "\n\n# ✦ $text ✦\n\n"
        }
        insertCustomText(formattedArt)
    }

    // Find and Replace
    fun onFindQueryChange(query: String) {
        _findQuery.value = query
        if (query.isBlank()) {
            _matchesCount.value = 0
            return
        }
        val text = _editorTextValue.value.text
        val count = Regex(Regex.escape(query), RegexOption.IGNORE_CASE).findAll(text).count()
        _matchesCount.value = count
    }

    fun onReplaceQueryChange(query: String) {
        _replaceQuery.value = query
    }

    fun replaceAllMatches() {
        val find = _findQuery.value
        val replace = _replaceQuery.value
        if (find.isBlank()) return
        val current = _editorTextValue.value
        val newText = current.text.replace(find, replace, ignoreCase = true)
        onContentChange(TextFieldValue(newText, TextRange(current.selection.start.coerceIn(0, newText.length))))
        _matchesCount.value = 0
    }

    fun replaceNextMatch() {
        val find = _findQuery.value
        val replace = _replaceQuery.value
        if (find.isBlank()) return
        val current = _editorTextValue.value
        val text = current.text
        val match = Regex(Regex.escape(find), RegexOption.IGNORE_CASE).find(text, current.selection.start)
            ?: Regex(Regex.escape(find), RegexOption.IGNORE_CASE).find(text, 0)

        match?.let {
            val range = it.range
            val newText = text.substring(0, range.first) + replace + text.substring(range.last + 1)
            val newCursor = range.first + replace.length
            onContentChange(TextFieldValue(newText, TextRange(newCursor)))
            onFindQueryChange(find)
        }
    }

    private fun extractOutline(text: String) {
        val lines = text.split("\n")
        val outline = mutableListOf<OutlineItem>()
        var charPos = 0
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("#")) {
                val level = trimmed.takeWhile { it == '#' }.length
                val title = trimmed.dropWhile { it == '#' }.trim()
                if (title.isNotBlank()) {
                    outline.add(OutlineItem(title, level, charPos))
                }
            }
            charPos += line.length + 1
        }
        _outlineItems.value = outline
    }

    fun jumpToPosition(pos: Int) {
        val current = _editorTextValue.value
        val safePos = pos.coerceIn(0, current.text.length)
        _editorTextValue.value = current.copy(selection = TextRange(safePos))
    }

    fun navigateBackToList() {
        saveActiveDocument()
        _activeDocument.value = null
        _currentScreen.value = "list"
    }

    fun duplicateDocument(doc: DocumentEntity) {
        viewModelScope.launch {
            repository.duplicate(doc)
        }
    }

    fun toggleFavorite(doc: DocumentEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(doc)
            if (_activeDocument.value?.id == doc.id) {
                _activeDocument.value = doc.copy(isFavorite = !doc.isFavorite)
            }
        }
    }

    fun togglePin(doc: DocumentEntity) {
        viewModelScope.launch {
            repository.togglePin(doc)
            if (_activeDocument.value?.id == doc.id) {
                _activeDocument.value = doc.copy(isPinned = !doc.isPinned)
            }
        }
    }

    fun deleteDocument(doc: DocumentEntity) {
        viewModelScope.launch {
            repository.delete(doc)
            if (_activeDocument.value?.id == doc.id) {
                navigateBackToList()
            }
        }
    }
}
