package com.febin.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Shared Ktor HTTP client configuration.
 * - CIO engine for Android; JSON serialization; Timber logging.
 * - Add interceptors (e.g., auth) as needed.
 */
object KtorClient {
    val client = HttpClient(CIO) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) = Timber.tag("Ktor").d(message)
            }
            level = LogLevel.BODY  // Full request/response logs (debug only)
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true  // Ignore extra JSON fields
                prettyPrint = true  // Human-readable in logs
                isLenient = true  // Flexible parsing
            })
        }
        // Add auth interceptor example
        // install(AuthInterceptor) { /* token logic */ }
    }
}