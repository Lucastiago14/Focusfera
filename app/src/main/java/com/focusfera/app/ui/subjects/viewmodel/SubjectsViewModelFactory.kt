package com.focusfera.app.ui.subjects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.focusfera.app.data.repository.SubjectRepository

class SubjectsViewModelFactory(
    private val repository: SubjectRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(SubjectsViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return SubjectsViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconhecido: ${modelClass.name}"
        )
    }
}