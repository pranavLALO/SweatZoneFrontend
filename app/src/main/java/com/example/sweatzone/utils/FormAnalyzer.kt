package com.example.sweatzone.utils

import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.abs
import kotlin.math.atan2

data class FormResult(
    val score: Int,
    val feedback: List<FeedbackItem>,
    val quickTip: String,
    val isGood: Boolean
)

data class FeedbackItem(
    val message: String,
    val isPositive: Boolean
)

object FormAnalyzer {

    fun analyzeSquat(minKneeAngle: Double, minBackAngle: Double, validFrames: Int): FormResult {
        val feedback = mutableListOf<FeedbackItem>()
        var score = 100
        var isGood = true

        if (validFrames < 5) {
            return FormResult(0, listOf(FeedbackItem("Body not fully detected", false)), "Ensure full body is in frame", false)
        }

        // Check depth
        if (minKneeAngle > 100) { // Should be around 90 or less, 120 is too high
            score -= 30
            feedback.add(FeedbackItem("Squat depth is too shallow", false))
            isGood = false
        } else {
            feedback.add(FeedbackItem("Good squat depth", true))
        }

        // Check back
        if (minBackAngle < 90) { // Leaning too forward
            score -= 20
            feedback.add(FeedbackItem("Back bending too much", false))
            isGood = false
        } else {
            feedback.add(FeedbackItem("Back posture is stable", true))
        }

        // Tempo / Consistency (Mocked for now based on frames)
        if (validFrames > 20) {
             feedback.add(FeedbackItem("Consistent tempo", true))
        }

        val tip = if (score > 80) "Great form! Add weights to progress." else "Focus on keeping your chest up and core tight to maintain a straight back."

        return FormResult(score, feedback, tip, score > 70)
    }

    fun analyzePushUp(minElbowAngle: Double, minBodyAngle: Double, validFrames: Int): FormResult {
        val feedback = mutableListOf<FeedbackItem>()
        var score = 100
        
        if (validFrames < 5) return FormResult(0, listOf(FeedbackItem("Body not fully detected", false)), "Ensure full body is in frame", false)

        if (minElbowAngle > 90) { // 90 degrees or less is good
            score -= 30
            feedback.add(FeedbackItem("Go lower (chest to floor)", false))
        } else {
            feedback.add(FeedbackItem("Good depth", true))
        }

        if (minBodyAngle < 160) {
            score -= 25
            feedback.add(FeedbackItem("Hips sagging", false))
        } else {
            feedback.add(FeedbackItem("Body line is straight", true))
        }

        val tip = if (score > 80) "Excellent! Try varying hand width." else "Engage your glutes and core to keep your body straight."
        
        return FormResult(score, feedback, tip, score > 70)
    }

    fun analyzePlank(minBodyAngle: Double, validFrames: Int): FormResult {
         val feedback = mutableListOf<FeedbackItem>()
        var score = 100
        
        if (validFrames < 5) return FormResult(0, listOf(FeedbackItem("Body not fully detected", false)), "Ensure full body is in frame", false)

        if (minBodyAngle < 165) {
            score -= 40
            feedback.add(FeedbackItem("Hips too low or high", false))
        } else {
             feedback.add(FeedbackItem("Solid straight line", true))
        }

        feedback.add(FeedbackItem("Head in neutral position", true)) // Mocked

         val tip = if (score > 80) "Perfect! Hold for longer duration." else "Don't let your hips sag. Squeeze your abs."

         return FormResult(score, feedback, tip, score > 70)
    }

    fun calculateAngle(a: PoseLandmark, b: PoseLandmark, c: PoseLandmark): Double {
        val angle = Math.toDegrees(
            atan2((c.position.y - b.position.y).toDouble(), (c.position.x - b.position.x).toDouble()) -
                    atan2((a.position.y - b.position.y).toDouble(), (a.position.x - b.position.x).toDouble())
        )
        val result = abs(angle)
        return if (result > 180) 360 - result else result
    }
}
