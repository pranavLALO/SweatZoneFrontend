package com.example.sweatzone.data.dto

import com.google.gson.annotations.SerializedName

data class SaveScoreRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("exercise_name") val exerciseName: String,
    @SerializedName("score") val score: Int,
    @SerializedName("metrics_json") val metricsJson: String? = null
)

data class TopScoresResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<ScoreItem>
)

data class ScoreItem(
    @SerializedName("id") val id: Int,
    @SerializedName("exercise_name") val exerciseName: String,
    @SerializedName("score") val score: Int,
    @SerializedName("created_at") val createdAt: String
)
