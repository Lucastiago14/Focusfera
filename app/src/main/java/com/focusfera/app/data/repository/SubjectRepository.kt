package com.focusfera.app.data.repository

import com.focusfera.app.data.local.dao.SubjectDao
import com.focusfera.app.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

class SubjectRepository(
    private val subjectDao: SubjectDao
) {

    fun getActiveSubjects(): Flow<List<SubjectEntity>> {
        return subjectDao.getActiveSubjects()
    }

    suspend fun getSubjectById(subjectId: Long): SubjectEntity? {
        return subjectDao.getSubjectById(subjectId)
    }

    suspend fun insertSubject(subject: SubjectEntity) {
        subjectDao.insertSubject(subject)
    }

    suspend fun updateSubject(subject: SubjectEntity) {
        subjectDao.updateSubject(subject)
    }

    suspend fun deleteSubject(subject: SubjectEntity) {
        subjectDao.deleteSubject(subject)
    }
}