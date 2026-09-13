package com.focusfera.app.ui.focus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FocusTimerViewModel : ViewModel() {

    private val _remainingMillis =
        MutableStateFlow(0L)

    val remainingMillis: StateFlow<Long> =
        _remainingMillis.asStateFlow()

    private val _isRunning =
        MutableStateFlow(false)

    val isRunning: StateFlow<Boolean> =
        _isRunning.asStateFlow()

    private val _isFinished =
        MutableStateFlow(false)

    val isFinished: StateFlow<Boolean> =
        _isFinished.asStateFlow()

    private val _studiedMillis =
        MutableStateFlow(0L)

    val studiedMillis: StateFlow<Long> =
        _studiedMillis.asStateFlow()

    private var timerJob: Job? = null

    private var endTimeMillis: Long? = null

    private var lastStartTimeMillis: Long? = null

    private var pausedRemainingMillis: Long = 0L

    private var preparedDurationMillis: Long = 0L

    fun prepareTimer(
        durationMinutes: Int
    ) {
        if (
            preparedDurationMillis > 0L ||
            _isRunning.value
        ) {
            return
        }

        preparedDurationMillis =
            durationMinutes * 60_000L

        _remainingMillis.value =
            preparedDurationMillis

        pausedRemainingMillis =
            preparedDurationMillis

        _studiedMillis.value = 0L

        _isFinished.value = false
    }

    fun startTimer() {

        if (_isRunning.value) {
            return
        }

        if (_remainingMillis.value <= 0L) {
            return
        }

        _isFinished.value = false
        _isRunning.value = true

        lastStartTimeMillis =
            System.currentTimeMillis()

        endTimeMillis =
            System.currentTimeMillis() +
                    _remainingMillis.value

        startTicker()
    }

    fun pauseTimer() {

        if (!_isRunning.value) {
            return
        }

        updateRemainingTime()
        accumulateStudiedTime()

        pausedRemainingMillis =
            _remainingMillis.value

        _isRunning.value = false

        endTimeMillis = null
        lastStartTimeMillis = null

        timerJob?.cancel()
        timerJob = null
    }

    fun resumeTimer() {

        if (_isRunning.value) {
            return
        }

        if (pausedRemainingMillis <= 0L) {
            return
        }

        _remainingMillis.value =
            pausedRemainingMillis

        startTimer()
    }

    fun finishEarly() {

        if (_isRunning.value) {
            updateRemainingTime()
            accumulateStudiedTime()
        }

        _isRunning.value = false
        _isFinished.value = true

        endTimeMillis = null
        lastStartTimeMillis = null

        pausedRemainingMillis =
            _remainingMillis.value

        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer(
        durationMinutes: Int
    ) {
        timerJob?.cancel()
        timerJob = null

        _isRunning.value = false
        _isFinished.value = false

        endTimeMillis = null
        lastStartTimeMillis = null

        preparedDurationMillis =
            durationMinutes * 60_000L

        _remainingMillis.value =
            preparedDurationMillis

        pausedRemainingMillis =
            preparedDurationMillis

        _studiedMillis.value = 0L
    }

    private fun startTicker() {

        timerJob?.cancel()

        timerJob = viewModelScope.launch {

            while (_isRunning.value) {

                updateRemainingTime()

                if (_remainingMillis.value <= 0L) {

                    accumulateStudiedTime()

                    _remainingMillis.value = 0L
                    pausedRemainingMillis = 0L

                    _isRunning.value = false
                    _isFinished.value = true

                    endTimeMillis = null
                    lastStartTimeMillis = null

                    break
                }

                delay(250L)
            }
        }
    }

    private fun updateRemainingTime() {

        val endTime =
            endTimeMillis ?: return

        val remaining =
            endTime - System.currentTimeMillis()

        _remainingMillis.value =
            remaining.coerceAtLeast(0L)
    }

    private fun accumulateStudiedTime() {

        val startedAt =
            lastStartTimeMillis ?: return

        val now =
            System.currentTimeMillis()

        val elapsed =
            now - startedAt

        _studiedMillis.value +=
            elapsed.coerceAtLeast(0L)

        lastStartTimeMillis = now
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}