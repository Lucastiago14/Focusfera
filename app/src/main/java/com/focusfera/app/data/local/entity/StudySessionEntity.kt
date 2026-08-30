package com.focusfera.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["subjectId"])
    ]
)
data class StudySessionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val subjectId: Long,

    val mode: String,

    val plannedDuration: Long,

    val studiedDuration: Long,

    val startedAt: Long,

    val endedAt: Long? = null,

    val completed: Boolean = false
)