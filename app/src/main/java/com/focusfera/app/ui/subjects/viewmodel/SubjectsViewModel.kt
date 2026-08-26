package com.focusfera.app.ui.subjects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focusfera.app.data.local.entity.SubjectEntity
import com.focusfera.app.data.repository.SubjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SubjectsViewModel(
    private val repository: SubjectRepository
) : ViewModel() {

    val subjects: StateFlow<List<SubjectEntity>> =
        repository
            .getActiveSubjects()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addSubject(
        name: String,
        color: Long
    ): Boolean {

        val trimmedName = name.trim()

        if (trimmedName.isBlank()) {
            return false
        }

        if (trimmedName.length > MAX_SUBJECT_NAME_LENGTH) {
            return false
        }

        val alreadyExists = subjects.value.any { subject ->
            subject.name.equals(
                trimmedName,
                ignoreCase = true
            )
        }

        if (alreadyExists) {
            return false
        }

        viewModelScope.launch {
            repository.insertSubject(
                SubjectEntity(
                    name = trimmedName,
                    color = color
                )
            )
        }

        return true
    }

    fun updateSubject(
        subject: SubjectEntity
    ): Boolean {

        val trimmedName = subject.name.trim()

        if (trimmedName.isBlank()) {
            return false
        }

        if (trimmedName.length > MAX_SUBJECT_NAME_LENGTH) {
            return false
        }

        val alreadyExists = subjects.value.any { existingSubject ->

            existingSubject.id != subject.id &&
                    existingSubject.name.equals(
                        trimmedName,
                        ignoreCase = true
                    )
        }

        if (alreadyExists) {
            return false
        }

        viewModelScope.launch {
            repository.updateSubject(
                subject.copy(
                    name = trimmedName
                )
            )
        }

        return true
    }

    fun deleteSubject(
        subject: SubjectEntity
    ) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }

    companion object {
        const val MAX_SUBJECT_NAME_LENGTH = 40
    }
}