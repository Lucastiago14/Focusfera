package com.focusfera.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val color: Long,

    val createdAt: Long = System.currentTimeMillis(),

    val isArchived: Boolean = false
)