package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocumentEntity
import com.example.data.DocumentTemplate
import com.example.data.DocumentTemplates
import com.example.ui.components.CoverPageDialog
import com.example.ui.components.TemplatesBottomSheet
import com.example.ui.theme.OfficeDeskGray
import com.example.ui.theme.OfficeRibbonBorder
import com.example.ui.theme.OfficeWordBlue
import com.example.ui.theme.OfficeWordDarkBlue
import com.example.util.DocumentUtils
import com.example.viewmodel.DocumentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    viewModel: DocumentViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val documents by viewModel.documentsList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isGridView by viewModel.isGridView.collectAsState()

    var showTemplatesSheet by remember { mutableStateOf(false) }
    var showCoverGalleryDialog by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var docToDelete by remember { mutableStateOf<DocumentEntity?>(null) }

    val categories = listOf("الكل", "المفضلة", "عام", "عمل", "دراسة", "شخصي")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = OfficeWordBlue,
                tonalElevation = 3.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Word PC Branding
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "W",
                                        color = OfficeWordBlue,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Microsoft Word",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "معالج النصوص الاحترافي",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Top Actions
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { isSearchActive = !isSearchActive },
                                modifier = Modifier.testTag("toggle_search_button")
                            ) {
                                Icon(
                                    imageVector = if (isSearchActive) Icons.Default.Clear else Icons.Default.Search,
                                    contentDescription = "بحث في المستندات",
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = { viewModel.toggleViewMode() },
                                modifier = Modifier.testTag("toggle_view_mode_button")
                            ) {
                                Icon(
                                    imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                                    contentDescription = "تغيير طريقة العرض",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.createNewDocument() },
                containerColor = OfficeWordBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("مستند فارغ جديد", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("fab_new_document")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OfficeDeskGray)
        ) {
            // Search Input Panel
            AnimatedVisibility(visible = isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("بحث في المستندات...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OfficeWordBlue) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "مسح")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("search_text_field")
                )
            }

            // Desktop "جديد" (New Templates Section)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(0.5.dp, OfficeRibbonBorder)
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "جديد",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF201F1E)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        // Prominent Cover Pages button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8F1FC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, OfficeWordBlue.copy(alpha = 0.5f)),
                            modifier = Modifier.clickable { showCoverGalleryDialog = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("📖", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "معرض قوالب الغلافات",
                                    fontSize = 11.sp,
                                    color = OfficeWordBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = { showTemplatesSheet = true },
                        modifier = Modifier.testTag("all_templates_button")
                    ) {
                        Text("مزيد من القوالب...", fontSize = 12.sp, color = OfficeWordBlue, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(DocumentTemplates.templates.take(4)) { template ->
                        DesktopTemplateStartCard(
                            template = template,
                            onClick = { viewModel.createNewDocument(template) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recent Documents Section Header & Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المستندات الأخيرة",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF201F1E)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedCategory(category) },
                            label = {
                                Text(
                                    text = category,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFDCEAF9),
                                selectedLabelColor = OfficeWordDarkBlue
                            ),
                            modifier = Modifier.testTag("filter_chip_$category")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Document List or Grid
            if (documents.isEmpty()) {
                EmptyStateView(
                    isSearching = searchQuery.isNotBlank(),
                    onNewDocClick = { viewModel.createNewDocument() }
                )
            } else {
                if (isGridView) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 88.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(documents, key = { it.id }) { doc ->
                            DesktopGridCard(
                                doc = doc,
                                onClick = { viewModel.openDocument(doc) },
                                onToggleFavorite = { viewModel.toggleFavorite(doc) },
                                onTogglePin = { viewModel.togglePin(doc) },
                                onDuplicate = { viewModel.duplicateDocument(doc) },
                                onShare = { DocumentUtils.shareDocument(context, doc.title, doc.content) },
                                onPrint = { DocumentUtils.printOrExportPdf(context, doc.title, doc.content) },
                                onDelete = { docToDelete = doc }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(documents, key = { it.id }) { doc ->
                            DesktopListCard(
                                doc = doc,
                                onClick = { viewModel.openDocument(doc) },
                                onToggleFavorite = { viewModel.toggleFavorite(doc) },
                                onTogglePin = { viewModel.togglePin(doc) },
                                onDuplicate = { viewModel.duplicateDocument(doc) },
                                onShare = { DocumentUtils.shareDocument(context, doc.title, doc.content) },
                                onPrint = { DocumentUtils.printOrExportPdf(context, doc.title, doc.content) },
                                onDelete = { docToDelete = doc }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "تطوير وبرمجة: المهندس شاكر السفياني",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = OfficeWordDarkBlue
                                    )
                                    Text(
                                        text = "هاتف الدعم الفني: 770472829",
                                        fontSize = 11.sp,
                                        color = OfficeWordBlue
                                    )
                                    Text(
                                        text = "جميع الحقوق محفوظة © 2026 - Word معالج النصوص",
                                        fontSize = 10.sp,
                                        color = Color(0xFF777777)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Templates BottomSheet
    if (showTemplatesSheet) {
        TemplatesBottomSheet(
            onDismiss = { showTemplatesSheet = false },
            onSelectTemplate = { template ->
                viewModel.createNewDocument(template)
            }
        )
    }

    // Cover Page Gallery Dialog (معرض قوالب الغلافات)
    if (showCoverGalleryDialog) {
        CoverPageDialog(
            currentTitle = "مستند بغلاف رسمي",
            onInsertCover = { coverTitle, subtitle, author, style ->
                viewModel.createNewDocument()
                viewModel.onTitleChange(coverTitle)
                viewModel.insertCoverPage(coverTitle, subtitle, author, style)
                showCoverGalleryDialog = false
            },
            onDismiss = { showCoverGalleryDialog = false }
        )
    }

    // Delete Confirmation Dialog
    docToDelete?.let { doc ->
        AlertDialog(
            onDismissRequest = { docToDelete = null },
            title = { Text("حذف المستند") },
            text = { Text("هل تريد بالتأكيد حذف \"${doc.title}\"؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteDocument(doc)
                        docToDelete = null
                    },
                    modifier = Modifier.testTag("confirm_delete_button")
                ) {
                    Text("حذف", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { docToDelete = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
private fun DesktopTemplateStartCard(
    template: DocumentTemplate,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(136.dp)
            .height(100.dp)
            .clickable(onClick = onClick)
            .testTag("quick_template_${template.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = Color(0xFFDCEAF9),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (template.id) {
                            "blank" -> Icons.Default.Add
                            else -> Icons.Default.Description
                        },
                        contentDescription = null,
                        tint = OfficeWordBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column {
                Text(
                    text = template.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF201F1E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = template.summary,
                    fontSize = 9.sp,
                    color = Color(0xFF605E5C),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DesktopListCard(
    doc: DocumentEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onTogglePin: () -> Unit,
    onDuplicate: () -> Unit,
    onShare: () -> Unit,
    onPrint: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .testTag("doc_card_${doc.id}"),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Word Document Blue Icon
            Surface(
                color = Color(0xFFDCEAF9),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = OfficeWordBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (doc.isPinned) {
                        Icon(
                            Icons.Default.PushPin,
                            contentDescription = "مثبت",
                            tint = OfficeWordBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = doc.title,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF201F1E)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = DocumentUtils.formatDate(doc.updatedAt),
                        fontSize = 10.sp,
                        color = Color(0xFF605E5C)
                    )
                    Text("•", fontSize = 10.sp, color = Color(0xFF605E5C))
                    Text(
                        text = "${doc.wordCount} كلمة",
                        fontSize = 10.sp,
                        color = Color(0xFF605E5C)
                    )
                    Text("•", fontSize = 10.sp, color = Color(0xFF605E5C))
                    Text(
                        text = doc.category,
                        fontSize = 10.sp,
                        color = OfficeWordBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = if (doc.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "المفضلة",
                    tint = if (doc.isFavorite) Color(0xFFFFA000) else Color(0xFFA19F9D),
                    modifier = Modifier.size(18.dp)
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.MoreVert, contentDescription = "خيارات", modifier = Modifier.size(18.dp))
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(if (doc.isPinned) "إلغاء التثبيت" else "تثبيت في الأعلى") },
                        leadingIcon = { Icon(Icons.Default.PushPin, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onTogglePin()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("تكرار المستند") },
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onDuplicate()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("مشاركة") },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onShare()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("طباعة أو PDF") },
                        leadingIcon = { Icon(Icons.Default.Print, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onPrint()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("حذف", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DesktopGridCard(
    doc: DocumentEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onTogglePin: () -> Unit,
    onDuplicate: () -> Unit,
    onShare: () -> Unit,
    onPrint: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .testTag("doc_grid_card_${doc.id}"),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFDCEAF9),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = OfficeWordBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Row {
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = if (doc.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "المفضلة",
                            tint = if (doc.isFavorite) Color(0xFFFFA000) else Color(0xFFA19F9D),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "خيارات", modifier = Modifier.size(16.dp))
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text(if (doc.isPinned) "إلغاء التثبيت" else "تثبيت في الأعلى") },
                                leadingIcon = { Icon(Icons.Default.PushPin, contentDescription = null) },
                                onClick = { showMenu = false; onTogglePin() }
                            )
                            DropdownMenuItem(
                                text = { Text("تكرار المستند") },
                                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null) },
                                onClick = { showMenu = false; onDuplicate() }
                            )
                            DropdownMenuItem(
                                text = { Text("مشاركة") },
                                leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                                onClick = { showMenu = false; onShare() }
                            )
                            DropdownMenuItem(
                                text = { Text("طباعة أو PDF") },
                                leadingIcon = { Icon(Icons.Default.Print, contentDescription = null) },
                                onClick = { showMenu = false; onPrint() }
                            )
                            DropdownMenuItem(
                                text = { Text("حذف", color = MaterialTheme.colorScheme.error) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                                onClick = { showMenu = false; onDelete() }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = doc.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF201F1E),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Simulated Paper preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFF9FAFB), RoundedCornerShape(3.dp))
                    .border(0.5.dp, Color(0xFFE5E7EB), RoundedCornerShape(3.dp))
                    .padding(6.dp)
            ) {
                Text(
                    text = doc.content.replace(Regex("[#*~_`>\\[\\]|]"), " ").trim(),
                    fontSize = 9.sp,
                    color = Color(0xFF605E5C),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${doc.wordCount} كلمة",
                    fontSize = 9.sp,
                    color = Color(0xFF605E5C)
                )
                if (doc.isPinned) {
                    Icon(
                        Icons.Default.PushPin,
                        contentDescription = "مثبت",
                        tint = OfficeWordBlue,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStateView(
    isSearching: Boolean,
    onNewDocClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = Color(0xFFDCEAF9),
            shape = CircleShape,
            modifier = Modifier.size(70.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    tint = OfficeWordBlue,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (isSearching) "لم يتم العثور على أي نتائج" else "لا توجد مستندات بعد",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF201F1E)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (isSearching) "جرّب البحث بكلمات أخرى" else "انقر أدناه لبدء كتابة مستندك الأول في Word",
            fontSize = 12.sp,
            color = Color(0xFF605E5C)
        )

        if (!isSearching) {
            Spacer(modifier = Modifier.height(16.dp))
            ExtendedFloatingActionButton(
                onClick = onNewDocClick,
                containerColor = OfficeWordBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("إنشاء مستند جديد") }
            )
        }
    }
}
