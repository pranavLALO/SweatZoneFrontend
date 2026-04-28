package com.example.sweatzone.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.sweatzone.viewmodel.WarmupType
import kotlin.math.abs

class WarmupSensorManager(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val _currentDegrees = MutableStateFlow(0f)
    val currentDegrees: StateFlow<Float> = _currentDegrees.asStateFlow()

    private val _completedReps = MutableStateFlow(0)
    val completedReps: StateFlow<Int> = _completedReps.asStateFlow()

    private var isCalibrating = true
    private var baseAngle: Float = 0f
    private var lastAngle: Float = 0f
    private var accumulatedAngle: Float = 0f
    private var currentWarmupType = WarmupType.WRIST

    fun setWarmupType(type: WarmupType) {
        currentWarmupType = type
    }

    fun startListening() {
        rotationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    // Call this after a 3-second UI countdown
    fun finishCalibration() {
        isCalibrating = false
        accumulatedAngle = 0f
        _currentDegrees.value = 0f
    }

    fun reset() {
        isCalibrating = true
        accumulatedAngle = 0f
        _completedReps.value = 0
        _currentDegrees.value = 0f
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            val orientationValues = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationValues)

            // Use Roll (index 2) for wrist rotation
            // Use Pitch (index 1) for arm rotation
            val currentRawAngle = if (currentWarmupType == WarmupType.WRIST) {
                Math.toDegrees(orientationValues[2].toDouble()).toFloat()
            } else {
                Math.toDegrees(orientationValues[1].toDouble()).toFloat()
            }

            if (isCalibrating) {
                baseAngle = currentRawAngle
                lastAngle = currentRawAngle
                return
            }

            // Calculate shortest path change
            var delta = currentRawAngle - lastAngle
            if (delta > 180f) delta -= 360f
            if (delta < -180f) delta += 360f

            // NOISE GATE: Ignore movements smaller than 2 degrees to prevent stationary jitter 
            // from slowly accumulating and triggering false reps. It will still catch slow rotations 
            // because lastAngle only updates when the threshold is crossed!
            if (abs(delta) > 2.0f) {
                accumulatedAngle += abs(delta)
                lastAngle = currentRawAngle
                checkThresholds()
            }
        }
    }

    private fun checkThresholds() {
        // If they completed 360 degrees, count a rep
        if (accumulatedAngle >= 360f) {
            _completedReps.value += 1
            accumulatedAngle -= 360f // carry over remainder
        }
        
        _currentDegrees.value = accumulatedAngle
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
