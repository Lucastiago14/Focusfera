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

    private var timerJob: Job? = null

    private var endTimeMillis: Long? = null

    private var pausedRemainingMillis: Long = 0L

    fun prepareTimer(
        durationMinutes: Int
    ) {
        if (
            _remainingMillis.value > 0L ||
            _isRunning.value
        ) {
            return
        }

        _remainingMillis.value =
            durationMinutes * 60_000L

        pausedRemainingMillis =
            _remainingMillis.value
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

        pausedRemainingMillis =
            _remainingMillis.value

        _isRunning.value = false

        endTimeMillis = null

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

    fun resetTimer(
        durationMinutes: Int
    ) {
        timerJob?.cancel()
        timerJob = null

        _isRunning.value = false
        _isFinished.value = false

        endTimeMillis = null

        _remainingMillis.value =
            durationMinutes * 60_000L

        pausedRemainingMillis =
            _remainingMillis.value
    }

    private fun startTicker() {

        timerJob?.cancel()

        timerJob = viewModelScope.launch {

            while (_isRunning.value) {

                updateRemainingTime()

                if (_remainingMillis.value <= 0L) {

                    _remainingMillis.value = 0L
                    pausedRemainingMillis = 0L

                    _isRunning.value = false
                    _isFinished.value = true

                    endTimeMillis = null

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

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}