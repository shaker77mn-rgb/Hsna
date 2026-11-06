package com.example.data

import kotlinx.coroutines.flow.Flow

class DocumentRepository(private val dao: DocumentDao) {
    val allDocuments: Flow<List<DocumentEntity>> = dao.getAllDocuments()
    val favoriteDocuments: Flow<List<DocumentEntity>> = dao.getFavoriteDocuments()

    fun getDocument(id: Long): Flow<DocumentEntity?> = dao.getDocumentById(id)

    suspend fun getDocumentOnce(id: Long): DocumentEntity? = dao.getDocumentByIdOnce(id)

    fun searchDocuments(query: String): Flow<List<DocumentEntity>> = dao.searchDocuments(query)

    suspend fun insert(document: DocumentEntity): Long = dao.insertDocument(document)

    suspend fun update(document: DocumentEntity) = dao.updateDocument(document)

    suspend fun delete(document: DocumentEntity) = dao.deleteDocument(document)

    suspend fun deleteById(id: Long) = dao.deleteDocumentById(id)

    suspend fun duplicate(document: DocumentEntity): Long {
        val copy = document.copy(
            id = 0,
            title = "${document.title} (نسخة)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isPinned = false
        )
        return dao.insertDocument(copy)
    }

    suspend fun toggleFavorite(document: DocumentEntity) {
        dao.updateDocument(document.copy(isFavorite = !document.isFavorite, updatedAt = System.currentTimeMillis()))
    }

    suspend fun togglePin(document: DocumentEntity) {
        dao.updateDocument(document.copy(isPinned = !document.isPinned, updatedAt = System.currentTimeMillis()))
    }

    suspend fun checkAndSeedInitialData() {
        val count = dao.getCount()
        if (count == 0) {
            dao.insertDocument(DocumentTemplates.sampleWelcomeDoc)
        }
    }
}
