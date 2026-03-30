package com.example.sweatzone.data.local

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "auth_pref"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_LEVEL = "user_level"
    private const val KEY_USER_GENDER = "user_gender"
    private const val KEY_USER_GOAL = "user_goal"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUserId(userId: Int) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int {
        // Default to -1 if not found
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun saveUserLevel(level: String) {
        prefs.edit().putString(KEY_USER_LEVEL, level.trim().lowercase()).apply()
    }

    fun getUserLevel(): String {
        return prefs.getString(KEY_USER_LEVEL, "beginner") ?: "beginner"
    }

    fun saveUserGender(gender: String) {
        prefs.edit().putString(KEY_USER_GENDER, gender).apply()
    }

    fun getUserGender(): String? {
        return prefs.getString(KEY_USER_GENDER, null)
    }

    fun saveUserGoal(goal: String) {
        prefs.edit().putString(KEY_USER_GOAL, goal).apply()
    }

    fun getUserGoal(): String? {
        return prefs.getString(KEY_USER_GOAL, null)
    }


    fun clear() {
        prefs.edit().clear().apply()
    }
}
