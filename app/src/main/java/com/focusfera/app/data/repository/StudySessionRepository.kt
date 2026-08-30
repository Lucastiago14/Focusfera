package com.focusfera.app.data.repository

import com.focusfera.app.data.local.dao.StudySessionDao
import com.focusfera.app.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

class StudySessionRepository(
    private val studySessionDao: StudySessionDao
) {

    fun getAllSessions(): Flow<List<StudySessionEntity>> {
        return studySessionDao.getAllSessions()
    }

    fun getSessionsBySubject(
        subjectId: Long
    ): Flow<List<StudySessionEntity>> {
        return studySessionDao.getSessionsBySubject(subjectId)
    }

    fun getRecentSessions(
        limit: Int
    ): Flow<List<StudySessionEntity>> {
        return studySessionDao.getRecentSessions(limit)
    }

    fun getTotalStudiedBySubject(
        subjectId: Long
    ): Flow<Long> {
        return studySessionDao.getTotalStudiedBySubject(subjectId)
    }

    fun getTotalStudiedBetween(
        startTime: Long,
        endTime: Long
    ): Flow<Long> {
        return studySessionDao.getTotalStudiedBetween(
            startTime = startTime,
            endTime = endTime
        )
    }

    suspend fun insertSession(
        session: StudySessionEntity
    ): Long {
        return studySessionDao.insertSession(session)
    }

    suspend fun updateSession(
        session: StudySessionEntity
    ) {
        studySessionDao.updateSession(session)
    }
}