package com.example.sweatzone.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweatzone.utils.WarmupSensorManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class WarmupType {
    WRIST, ARM
}

class WarmupViewModel(application: Application) : AndroidViewModel(application) {
    private val sensorManager = WarmupSensorManager(application)

    val currentDegrees = sensorManager.currentDegrees
    val completedReps = sensorManager.completedReps

    private val _isCalibrating = MutableStateFlow(true)
    val isCalibrating: StateFlow<Boolean> = _isCalibrating.asStateFlow()

    private val _countdown = MutableStateFlow(3)
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    private val _warmupType = MutableStateFlow(WarmupType.WRIST)
    val warmupType: StateFlow<WarmupType> = _warmupType.asStateFlow()

    fun setWarmupType(type: WarmupType) {
        if (!_isActive.value) {
            _warmupType.value = type
        }
    }

    fun startWarmup() {
        if (_isActive.value) return
        _isActive.value = true
        _isCalibrating.value = true
        _countdown.value = 3
        sensorManager.setWarmupType(_warmupType.value)
        sensorManager.reset()
        sensorManager.startListening()

        viewModelScope.launch {
            while (_countdown.value > 0) {
                delay(1000)
                _countdown.value -= 1
            }
            _isCalibrating.value = false
            sensorManager.finishCalibration()
        }
    }

    fun stopWarmup() {
        _isActive.value = false
        sensorManager.stopListening()
    }

    fun reset() {
        stopWarmup()
        sensorManager.reset()
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.stopListening()
    }
}
