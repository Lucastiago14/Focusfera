package com.focusfera.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusfera.app.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSession(
        session: StudySessionEntity
    ): Long

    @Update
    suspend fun updateSession(
        session: StudySessionEntity
    )

    @Query(
        """
        SELECT * FROM study_sessions
        ORDER BY startedAt DESC
        """
    )
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query(
        """
        SELECT * FROM study_sessions
        WHERE subjectId = :subjectId
        ORDER BY startedAt DESC
        """
    )
    fun getSessionsBySubject(
        subjectId: Long
    ): Flow<List<StudySessionEntity>>

    @Query(
        """
        SELECT * FROM study_sessions
        ORDER BY startedAt DESC
        LIMIT :limit
        """
    )
    fun getRecentSessions(
        limit: Int
    ): Flow<List<StudySessionEntity>>

    @Query(
        """
        SELECT COALESCE(SUM(studiedDuration), 0)
        FROM study_sessions
        WHERE subjectId = :subjectId
        """
    )
    fun getTotalStudiedBySubject(
        subjectId: Long
    ): Flow<Long>

    @Query(
        """
        SELECT COALESCE(SUM(studiedDuration), 0)
        FROM study_sessions
        WHERE startedAt >= :startTime
        AND startedAt < :endTime
        """
    )
    fun getTotalStudiedBetween(
        startTime: Long,
        endTime: Long
    ): Flow<Long>
}