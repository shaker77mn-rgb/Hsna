package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String = "عام",
    val isFavorite: Boolean = false,
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val wordCount: Int = 0,
    val charCount: Int = 0,
    val fontSize: Int = 16,
    val fontFamily: String = "افتراضي",
    val isPageMode: Boolean = true
)
