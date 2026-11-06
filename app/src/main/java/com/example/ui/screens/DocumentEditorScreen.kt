package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AboutRightsDialog
import com.example.ui.components.CoverPageDialog
import com.example.ui.components.DesktopBackstageView
import com.example.ui.components.DesktopPcKeyboard
import com.example.ui.components.DesktopRibbon
import com.example.ui.components.DesktopRuler
import com.example.ui.components.DesktopStatusBar
import com.example.ui.components.DesktopTitleBar
import com.example.ui.components.DocumentStatsDialog
import com.example.ui.components.EquationsGalleryDialog
import com.example.ui.components.HeaderFooterDialog
import com.example.ui.components.PictureOptionsDialog
import com.example.ui.components.ShapesGalleryDialog
import com.example.ui.components.SymbolsGalleryDialog
import com.example.ui.components.TableDimensionsDialog
import com.example.ui.components.WordArtDialog
import com.example.ui.theme.OfficeDeskGray
import com.example.ui.theme.OfficePaperWhite
import com.example.ui.theme.OfficeWordBlue
import com.example.ui.theme.OfficeWordDarkBlue
import com.example.ui.theme.WordFonts
import com.example.util.DocumentUtils
import com.example.util.TextToSpeechHelper
import com.example.viewmodel.DocumentViewModel

@Composable
fun DocumentEditorScreen(
    viewModel: DocumentViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeDoc by viewModel.activeDocument.collectAsState()
    val title by viewModel.editorTitle.collectAsState()
    val textValue by viewModel.editorTextValue.collectAsState()
    val stats by viewModel.documentStats.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()
    val canUndo by viewModel.canUndo.collectAsState()
    val canRedo by viewModel.canRedo.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    // Desktop States
    val fontFamily by viewModel.fontFamily.collectAsState()
    val zoomPercent by viewModel.zoomPercent.collectAsState()
    val isRulerVisible by viewModel.isRulerVisible.collectAsState()
    val isBackstageOpen by viewModel.isBackstageOpen.collectAsState()
    val isReadMode by viewModel.isReadMode.collectAsState()
    val marginType by viewModel.marginType.collectAsState()
    val pageOrientation by viewModel.pageOrientation.collectAsState()

    // RTL / LTR Direction and PC Keyboard States
    val textDirection by viewModel.textDirection.collectAsState()
    val isPcKeyboardVisible by viewModel.isPcKeyboardVisible.collectAsState()
    val pcKeyboardLang by viewModel.pcKeyboardLang.collectAsState()
    val isPcShiftActive by viewModel.isPcShiftActive.collectAsState()
    val isPcCtrlActive by viewModel.isPcCtrlActive.collectAsState()

    // Find and Replace
    val isFindReplaceOpen by viewModel.isFindReplaceOpen.collectAsState()
    val findQuery by viewModel.findQuery.collectAsState()
    val replaceQuery by viewModel.replaceQuery.collectAsState()
    val matchesCount by viewModel.matchesCount.collectAsState()

    var showTitleRenameDialog by remember { mutableStateOf(false) }
    var showStatsDialog by remember { mutableStateOf(false) }
    var showAboutRightsDialog by remember { mutableStateOf(false) }

    // Insert Suite Dialog States
    var showShapesDialog by remember { mutableStateOf(false) }
    var showTableDialog by remember { mutableStateOf(false) }
    var showSymbolsDialog by remember { mutableStateOf(false) }
    var showEquationsDialog by remember { mutableStateOf(false) }
    var showWordArtDialog by remember { mutableStateOf(false) }
    var showCoverPageDialog by remember { mutableStateOf(false) }
    var showHeaderDialog by remember { mutableStateOf(false) }
    var showFooterDialog by remember { mutableStateOf(false) }
    var showPictureDialog by remember { mutableStateOf(false) }

    // Android Zero-Permission Photo Picker for inserting images
    val photoPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.insertImageUri(uri.toString(), "صورة تم اختيارها")
        }
    }

    // TTS Helper
    val ttsHelper = remember { TextToSpeechHelper(context) }
    val isSpeaking by ttsHelper.isSpeaking

    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    BackHandler {
        if (isBackstageOpen) {
            viewModel.toggleBackstage(false)
        } else if (isPcKeyboardVisible) {
            viewModel.togglePcKeyboard(false)
        } else {
            viewModel.navigateBackToList()
        }
    }

    val pagePadding = when (marginType) {
        "ضيقة" -> 14.dp
        "عريضة" -> 36.dp
        else -> 24.dp
    }

    val selectedFont = WordFonts.getFontFamily(fontFamily)

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            topBar = {
                Column {
                    // 1. Official Desktop Word Title Bar
                    DesktopTitleBar(
                        title = title,
                        isSaved = isSaved,
                        canUndo = canUndo,
                        canRedo = canRedo,
                        onSaveClick = { viewModel.saveActiveDocument() },
                        onUndoClick = { viewModel.undo() },
                        onRedoClick = { viewModel.redo() },
                        onPrintClick = { DocumentUtils.printOrExportPdf(context, title, textValue.text) },
                        onTitleClick = { showTitleRenameDialog = true },
                        onSearchClick = { viewModel.toggleFindReplace() },
                        onCloseClick = { viewModel.navigateBackToList() }
                    )

                    // 2. Official Desktop Word Ribbon Toolbar
                    DesktopRibbon(
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.setSelectedTab(it) },
                        onFileMenuClick = { viewModel.toggleBackstage(true) },
                        fontFamily = fontFamily,
                        onFontFamilyChange = { viewModel.setFontFamily(it) },
                        fontSize = fontSize,
                        onFontSizeChange = { viewModel.setFontSize(it) },
                        onFormatBold = { viewModel.formatWrap("**") },
                        onFormatItalic = { viewModel.formatWrap("*") },
                        onFormatUnderline = { viewModel.formatWrap("<u>", "</u>") },
                        onFormatStrikethrough = { viewModel.formatWrap("~~") },
                        onFormatHighlight = { viewModel.formatWrap("==") },
                        onFormatHeading = { viewModel.formatHeading(it) },
                        onFormatList = { viewModel.formatList(it) },
                        onInsertTable = { showTableDialog = true },
                        onInsertDivider = { viewModel.insertDivider() },
                        onInsertDate = { viewModel.insertDate() },
                        onInsertLink = { viewModel.formatWrap("[", "](https://)") },
                        onInsertCoverPage = { showCoverPageDialog = true },
                        onInsertPageBreak = { viewModel.insertPageBreak() },
                        onInsertPicture = { showPictureDialog = true },
                        onInsertShapes = { showShapesDialog = true },
                        onInsertHeader = { showHeaderDialog = true },
                        onInsertFooter = { showFooterDialog = true },
                        onInsertWordArt = { showWordArtDialog = true },
                        onInsertSignatureLine = { viewModel.insertSignatureLine() },
                        onInsertEquation = { showEquationsDialog = true },
                        onInsertSymbol = { showSymbolsDialog = true },
                        onFindReplaceClick = { viewModel.toggleFindReplace() },
                        onStatsClick = { showStatsDialog = true },
                        onReadAloudClick = {
                            if (isSpeaking) ttsHelper.stop() else ttsHelper.speak(textValue.text)
                        },
                        isRulerVisible = isRulerVisible,
                        onToggleRuler = { viewModel.toggleRuler() },
                        zoomPercent = zoomPercent,
                        onZoomIn = { viewModel.zoomIn() },
                        onZoomOut = { viewModel.zoomOut() },
                        marginType = marginType,
                        onMarginChange = { viewModel.setMarginType(it) },
                        pageOrientation = pageOrientation,
                        onOrientationChange = { viewModel.setPageOrientation(it) },
                        isReadMode = isReadMode,
                        onToggleReadMode = { viewModel.toggleReadMode() },
                        textDirection = textDirection,
                        onSetTextDirection = { viewModel.setTextDirection(it) },
                        isPcKeyboardVisible = isPcKeyboardVisible,
                        onTogglePcKeyboard = { viewModel.togglePcKeyboard() },
                        onAboutClick = { showAboutRightsDialog = true }
                    )

                    // 3. Desktop Horizontal Centimeter Ruler
                    if (isRulerVisible && !isReadMode) {
                        DesktopRuler()
                    }
                }
            },
            bottomBar = {
                Column {
                    // Optional Full PC Keyboard
                    if (isPcKeyboardVisible) {
                        DesktopPcKeyboard(
                            isArabic = pcKeyboardLang == "ar",
                            isShiftActive = isPcShiftActive,
                            isCtrlActive = isPcCtrlActive,
                            onKeyPress = { viewModel.onPcKeyPress(it) },
                            onToggleLang = { viewModel.togglePcKeyboardLang() },
                            onToggleShift = { viewModel.togglePcShift() },
                            onToggleCtrl = { viewModel.togglePcCtrl() },
                            onCloseKeyboard = { viewModel.togglePcKeyboard(false) }
                        )
                    }

                    // 4. Official Desktop Word Bottom Status Bar
                    DesktopStatusBar(
                        stats = stats,
                        isSaved = isSaved,
                        isReadMode = isReadMode,
                        onToggleReadMode = { viewModel.toggleReadMode() },
                        zoomPercent = zoomPercent,
                        onZoomIn = { viewModel.zoomIn() },
                        onZoomOut = { viewModel.zoomOut() },
                        onResetZoom = { viewModel.setZoomPercent(100) },
                        onStatsClick = { showStatsDialog = true }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(OfficeDeskGray)
            ) {
                // Find and Replace Dropdown Panel
                AnimatedVisibility(
                    visible = isFindReplaceOpen,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Surface(
                        color = Color(0xFFF3F2F1),
                        modifier = Modifier.fillMaxWidth(),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD2D0CE))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = findQuery,
                                    onValueChange = { viewModel.onFindQueryChange(it) },
                                    placeholder = { Text("بحث عن...", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_find_query")
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                if (findQuery.isNotBlank()) {
                                    Surface(
                                        color = Color(0xFFDCEAF9),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "$matchesCount مطابق",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OfficeWordDarkBlue,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                                IconButton(onClick = { viewModel.toggleFindReplace(false) }) {
                                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = replaceQuery,
                                    onValueChange = { viewModel.onReplaceQueryChange(it) },
                                    placeholder = { Text("استبدال بـ...", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_replace_query")
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedButton(
                                    onClick = { viewModel.replaceNextMatch() },
                                    modifier = Modifier.testTag("btn_replace_next")
                                ) {
                                    Text("استبدال التالي", fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Button(
                                    onClick = { viewModel.replaceAllMatches() },
                                    modifier = Modifier.testTag("btn_replace_all")
                                ) {
                                    Text("استبدال الكل", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Desktop Paper Canvas Area
                val scrollState = rememberScrollState()
                val effectiveFontSize = (fontSize * (zoomPercent / 100f)).coerceAtLeast(10f)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 14.dp, vertical = 18.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Authentic Microsoft Word White A4 Sheet
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = if (pageOrientation == "أفقي") 840.dp else 680.dp)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(2.dp)),
                        shape = RoundedCornerShape(2.dp),
                        color = OfficePaperWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1D5DB))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(pagePadding)
                        ) {
                            // Page Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 10.sp,
                                    color = Color(0xFFA19F9D)
                                )
                                Text(
                                    text = "Microsoft Word Document",
                                    fontSize = 10.sp,
                                    color = Color(0xFFA19F9D)
                                )
                            }

                            HorizontalDivider(
                                color = Color(0xFFE5E7EB),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Document Text Body (with selected Arabic/English font and RTL/LTR direction)
                            BasicTextField(
                                value = textValue,
                                onValueChange = { viewModel.onContentChange(it) },
                                readOnly = isReadMode,
                                textStyle = TextStyle(
                                    fontSize = effectiveFontSize.sp,
                                    lineHeight = (effectiveFontSize * 1.6).sp,
                                    color = Color(0xFF201F1E),
                                    fontFamily = selectedFont,
                                    textDirection = if (textDirection == "rtl") TextDirection.Rtl else TextDirection.Ltr
                                ),
                                cursorBrush = SolidColor(OfficeWordBlue),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(780.dp)
                                    .testTag("document_text_editor")
                            )

                            HorizontalDivider(
                                color = Color(0xFFE5E7EB),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Page Footer Guide
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "تطوير: م. شاكر السفياني 770472829",
                                    fontSize = 9.sp,
                                    color = Color(0xFFA19F9D)
                                )
                                Text(
                                    text = "— 1 —",
                                    fontSize = 10.sp,
                                    color = Color(0xFFA19F9D)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Fullscreen Desktop Word "ملف" (Backstage View)
        AnimatedVisibility(
            visible = isBackstageOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            activeDoc?.let { doc ->
                DesktopBackstageView(
                    document = doc,
                    stats = stats,
                    onCloseBackstage = { viewModel.toggleBackstage(false) },
                    onNewTemplate = { template ->
                        viewModel.createNewDocument(template)
                    },
                    onSaveClick = { viewModel.saveActiveDocument() },
                    onPrintClick = { DocumentUtils.printOrExportPdf(context, title, textValue.text) },
                    onShareClick = { DocumentUtils.shareDocument(context, title, textValue.text) },
                    onCloseDocClick = { viewModel.navigateBackToList() }
                )
            }
        }
    }

    // Title Rename Dialog
    if (showTitleRenameDialog) {
        var tempTitle by remember { mutableStateOf(title) }
        AlertDialog(
            onDismissRequest = { showTitleRenameDialog = false },
            title = { Text("إعادة تسمية المستند - Word") },
            text = {
                OutlinedTextField(
                    value = tempTitle,
                    onValueChange = { tempTitle = it },
                    label = { Text("اسم المستند") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempTitle.isNotBlank()) {
                            viewModel.onTitleChange(tempTitle.trim())
                        }
                        showTitleRenameDialog = false
                    }
                ) {
                    Text("حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTitleRenameDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Document Statistics Dialog
    if (showStatsDialog && activeDoc != null) {
        DocumentStatsDialog(
            document = activeDoc!!,
            stats = stats,
            onDismiss = { showStatsDialog = false }
        )
    }

    // About & Rights Dialog (تطوير المهندس شاكر السفياني 770472829)
    if (showAboutRightsDialog) {
        AboutRightsDialog(
            onDismiss = { showAboutRightsDialog = false }
        )
    }

    // 1. Shapes Gallery Dialog (معرض الأشكال والرسومات)
    if (showShapesDialog) {
        ShapesGalleryDialog(
            onSelectShape = { shapeType ->
                viewModel.insertShape(shapeType)
            },
            onDismiss = { showShapesDialog = false }
        )
    }

    // 2. Table Dimensions Dialog (إدراج جدول بأبعاد مخصصة)
    if (showTableDialog) {
        TableDimensionsDialog(
            onInsertTable = { rows, cols ->
                viewModel.insertCustomTable(rows, cols)
            },
            onDismiss = { showTableDialog = false }
        )
    }

    // 3. Symbols Gallery Dialog (معرض الرموز الخاصة)
    if (showSymbolsDialog) {
        SymbolsGalleryDialog(
            onSelectSymbol = { symbol ->
                viewModel.insertSymbol(symbol)
            },
            onDismiss = { showSymbolsDialog = false }
        )
    }

    // 4. Equations Gallery Dialog (معادلات رياضية)
    if (showEquationsDialog) {
        EquationsGalleryDialog(
            onSelectEquation = { eq ->
                viewModel.insertEquation(eq)
            },
            onDismiss = { showEquationsDialog = false }
        )
    }

    // 5. WordArt Dialog (نصوص مزخرفة)
    if (showWordArtDialog) {
        WordArtDialog(
            onInsertWordArt = { artText, artStyle ->
                viewModel.insertWordArt(artText, artStyle)
            },
            onDismiss = { showWordArtDialog = false }
        )
    }

    // 6. Cover Page Gallery Dialog (معرض قوالب أغلفة المستندات الجاهزة)
    if (showCoverPageDialog) {
        CoverPageDialog(
            currentTitle = title,
            onInsertCover = { coverTitle, subtitle, author, style ->
                viewModel.insertCoverPage(coverTitle, subtitle, author, style)
            },
            onDismiss = { showCoverPageDialog = false }
        )
    }

    // 7. Header Dialog (رأس الصفحة)
    if (showHeaderDialog) {
        HeaderFooterDialog(
            isHeader = true,
            onInsert = { headerText ->
                viewModel.insertHeader(headerText)
            },
            onDismiss = { showHeaderDialog = false }
        )
    }

    // 8. Footer Dialog (تذييل الصفحة)
    if (showFooterDialog) {
        HeaderFooterDialog(
            isHeader = false,
            onInsert = { footerText ->
                viewModel.insertFooter(footerText)
            },
            onDismiss = { showFooterDialog = false }
        )
    }

    // 9. Picture Options Dialog (إدراج صور)
    if (showPictureDialog) {
        PictureOptionsDialog(
            onPickFromGallery = {
                photoPickerLauncher.launch(
                    androidx.activity.result.PickVisualMediaRequest(
                        androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onInsertStockPhoto = { caption ->
                viewModel.insertImagePlaceholder(caption)
            },
            onDismiss = { showPictureDialog = false }
        )
    }
}
