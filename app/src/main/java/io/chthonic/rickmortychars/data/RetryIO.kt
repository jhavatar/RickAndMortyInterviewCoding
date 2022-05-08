package io.chthonic.rickmortychars.domain

import android.util.Log
import kotlinx.coroutines.delay

suspend fun <T> retryIO(
    times: Int = Int.MAX_VALUE,
    firstRetryDelay: Long = 100, // 0.1 second
    maxDelay: Long = 1000,    // 1 second
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = firstRetryDelay
    repeat(times - 1) { iteration ->
        try {
            return block()
        } catch (e: Exception) {
            Log.e("retryIO", "retryIO ${iteration + 1} failed", e)
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}