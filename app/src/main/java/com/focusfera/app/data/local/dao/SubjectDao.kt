package com.focusfera.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusfera.app.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSubject(subject: SubjectEntity)

    @Update
    suspend fun updateSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)

    @Query(
        """
        SELECT * FROM subjects
        WHERE isArchived = 0
        ORDER BY createdAt DESC
        """
    )
    fun getActiveSubjects(): Flow<List<SubjectEntity>>

    @Query(
        """
        SELECT * FROM subjects
        WHERE id = :subjectId
        LIMIT 1
        """
    )
    suspend fun getSubjectById(subjectId: Long): SubjectEntity?
}