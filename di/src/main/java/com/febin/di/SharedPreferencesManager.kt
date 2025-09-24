package com.febin.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Simple manager for SharedPreferences (e.g., for onboarding status).
 * - Injected via Koin; use in Composables or ViewModels.
 */
class SharedPreferencesManager(private val context: Context) {
    companion object {
        private const val PREF_NAME = "app_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isOnboardingCompleted(): Boolean = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit { putBoolean(KEY_ONBOARDING_COMPLETED, completed) }
    }

    // Add more keys/methods as needed (e.g., theme mode)
}