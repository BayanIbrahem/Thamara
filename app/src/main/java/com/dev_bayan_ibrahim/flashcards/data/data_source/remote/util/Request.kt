package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util

import android.util.Log


/**
 * request object represent single request (get, post,...) with specific params
 * @property execute, execute the request and returns a result
 */
class Request<Data>(
    val key: Int? = null,
    val url: String = "?",
    val requestType: RequestType = RequestType.UNKNOWN,
    val bodyParams: List<BodyParam> = emptyList(),
    private val executor: suspend () -> Data,
) {
    suspend fun execute(): Result<Data> = try {
        val data = executor()
        Log.e(
            "request",
            "${logHeader()}\nresult    success\ndata      $data"
        )
        Result.success(data)
    } catch (e: Exception) {
        Log.e(
            "request",
            "${logHeader()}\nresult    failure\nexception $e"
        )
        Result.failure(e)
    }
    private fun logHeader(): String = buildString {
        append("request   "); append(key ?: "?"); appendLine()
        append("type      "); append(requestType); appendLine()
        append("url       "); append(url); appendLine()
        append("body      "); append(bodyParams.joinToString("\n          "))
    }
}