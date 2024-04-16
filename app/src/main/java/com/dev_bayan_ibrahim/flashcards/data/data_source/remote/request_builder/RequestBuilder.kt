package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.request_builder

import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.input.InputField
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.uri_decerator.UriDirector
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.BodyParam
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.Request
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.RequestParam
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.RequestType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RequestBuilder(
    val client: HttpClient,
    val uriDirector: UriDirector,
    private val json: Json,
    val endpoint: String,
) {
    val baseUrl = URLBuilder().apply {
        with(uriDirector) { director() }
    }.build()

    fun prepareUri(
        params: List<RequestParam> = emptyList(),
        body: URLBuilder.() -> URLBuilder = { this },
    ): Url {
        return URLBuilder().apply {
            with(uriDirector) {
                director()
            }
            // todo check if correct
            appendPathSegments(endpoint)
            params.forEach { (key, value) ->
                this.parameters.append(key, value.toString())
            }
            body()
        }.build()
    }

    private fun buildJsonData(
        params: List<BodyParam>
    ): JsonObject {
        return buildJsonObject{
            params.forEach { param ->
                when(param) {
                    is BodyParam.Text -> {
                        put(key = param.key, value = param.value)
                    }
                }
            }
            /*
            formData {
            params.forEach { param ->
            when (param) {
            is BodyParam.File -> {
            append(
            key = param.key,
            value = param.array,
            headers = Headers.build {
            append(
            name = HttpHeaders.ContentType,
            value = param.type
            )
            append(
            name = HttpHeaders.ContentDisposition,
            value = "filename=\"${param.name}\""
            )
            }
            )
            }

            is BodyParam.Text -> {
            append(
            key = param.key,
            value = param.value.toString()
            )
            }
            }
            }
            }
            */
        }
    }

    private fun HttpRequestBuilder.requestBody(params: List<BodyParam>) {
        if (params.isNotEmpty()) {
            contentType(ContentType.Application.Json)
            setBody(buildJsonData(params))
        }
    }

    suspend fun <T : Any> HttpResponse.deserialize(
        deserializer: DeserializationStrategy<T>
    ): T = json.decodeFromString(
        deserializer = deserializer,
        string = body<String>()
    )

    private suspend fun <T : Any> HttpResponse.deserializeList(
        serializer: KSerializer<T>
    ): List<T> = json.decodeFromString(
        deserializer = ListSerializer(serializer),
        string = body()
    )

    fun <Output : Any> buildGetRequest(
        id: Long,
        deserializer: DeserializationStrategy<Output>,
        bodyParams: List<BodyParam> = emptyList(),
    ): Request<Output> {
        val url = prepareUri {
            appendEncodedPathSegments(id.toString())
        }
        return Request(
            key = id.toInt(),
            url = url.toString(),
            requestType = RequestType.GET
        ) {
            client.get(url) {
                requestBody(bodyParams)
            }.deserialize(deserializer)
        }
    }

    fun <Input : InputField, Output : Any> buildGetRequest(
        input: Input,
        deserializer: DeserializationStrategy<Output>,
        token: String? = null,
        requestFilter: (RequestParam) -> Boolean = { true },
        bodyFilter: (BodyParam) -> Boolean = { false },
    ): Request<Output> {
        val url = prepareUri(
            params = input.asRequestParams(requestFilter),
        )
        val bodyParams = input.asBodyParams(bodyFilter)
        return Request(
            key = input.hashCode(),
            url = url.toString(),
            requestType = RequestType.GET,
            bodyParams = bodyParams,
        ) {
            client.get(url) {
                requestBody(bodyParams)
                token?.let { token ->
                    header(
                        "Authorization",
                        "Bearer $token"
                    )
                }
            }.deserialize(deserializer)
        }
    }

    fun <Input : InputField, Output : Any> buildGetListRequest(
        input: Input,
        serializer: KSerializer<Output>,
        requestFilter: (RequestParam) -> Boolean = { true },
        bodyFilter: (BodyParam) -> Boolean = { false },
    ): Request<List<Output>> {
        val url = prepareUri(input.asRequestParams(requestFilter))
        val bodyParams = input.asBodyParams(bodyFilter)
        return Request(
            key = input.hashCode(),
            url = url.toString(),
            requestType = RequestType.GET_LIST,
            bodyParams = bodyParams
        ) {
            client.get(url) {
                requestBody(bodyParams)
            }.deserializeList(serializer)
        }
    }

    fun <Input : InputField, Output : Any> buildDeleteRequest(
        input: Input,
        deserializer: DeserializationStrategy<Output>,
        requestFilter: (RequestParam) -> Boolean = { true },
        bodyFilter: (BodyParam) -> Boolean = { false },
    ): Request<Output> {
        val url = prepareUri(input.asRequestParams(requestFilter))
        val bodyParams = input.asBodyParams(bodyFilter)
        return Request(
            key = input.hashCode(),
            url = url.toString(),
            requestType = RequestType.DELETE,
            bodyParams = bodyParams
        ) {
            client.delete(url) {
                requestBody(bodyParams)
            }.deserialize(deserializer)
        }
    }

    fun <Input : InputField, Output : Any> buildPostRequest(
        input: Input,
        deserializer: DeserializationStrategy<Output>,
        requestFilter: (RequestParam) -> Boolean = { false },
        bodyFilter: (BodyParam) -> Boolean = { true },
    ): Request<Output> {
        val url = prepareUri(input.asRequestParams(requestFilter))
        val bodyParams = input.asBodyParams(bodyFilter)
        return Request(
            key = input.hashCode(),
            url = url.toString(),
            requestType = RequestType.POST,
            bodyParams = bodyParams
        ) {
            client.post {
                url(url)
                requestBody(bodyParams)
            }.deserialize(deserializer)
        }
    }

    fun <Input : InputField, Output : Any> buildPutRequest(
        input: Input,
        deserializer: DeserializationStrategy<Output>,
        requestFilter: (RequestParam) -> Boolean = { false },
        bodyFilter: (BodyParam) -> Boolean = { true },
    ): Request<Output> {
        val url = prepareUri(input.asRequestParams(requestFilter))
        val bodyParams = input.asBodyParams(bodyFilter)
        return Request(
            key = input.hashCode(),
            url = url.toString(),
            requestType = RequestType.PUT,
            bodyParams = bodyParams
        ) {
            client.put {
                url(url)
                requestBody(bodyParams)
            }.deserialize(deserializer)
        }
    }

    fun <Input : InputField, Output : Any> buildPatchRequest(
        input: Input,
        deserializer: DeserializationStrategy<Output>,
        requestFilter: (RequestParam) -> Boolean = { false },
        bodyFilter: (BodyParam) -> Boolean = { true },
    ): Request<Output> {
        val url = prepareUri(input.asRequestParams(requestFilter))
        val bodyParams = input.asBodyParams(bodyFilter)
        return Request(
            key = input.hashCode(),
            url = url.toString(),
            requestType = RequestType.PATCH,
            bodyParams = bodyParams
        ) {
            client.patch {
                url(url)
                requestBody(bodyParams)
            }.deserialize(deserializer)
        }
    }
}
