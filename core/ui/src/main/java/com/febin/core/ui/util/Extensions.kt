package com.febin.core.ui.util



import timber.log.Timber

// String extensions
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.length >= 8  // Basic validation; expand as needed
}

fun String.isValidPhone(): Boolean {
    return android.util.Patterns.PHONE.matcher(this).matches()
}


// Timber extension for custom logging
fun Timber.logDebug(tag: String, message: String) {
    Timber.tag(tag).d(message)
}

fun Timber.logError(tag: String, message: String, throwable: Throwable? = null) {
    if (throwable != null) {
        Timber.tag(tag).e(throwable, message)
    } else {
        Timber.tag(tag).e(message)
    }
}