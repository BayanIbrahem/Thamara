package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.endpoint

import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.request_builder.RequestBuilder
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.uri_decerator.UriDirector
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

enum class Endpoint(
    val value: String,
) {
    Deck("deck"),
    Version("deck/version"),
    Rate("rate"),
    Collection("collection"),
    Tag("tag"),

}
fun getEndpoint(
    client: HttpClient,
    director: UriDirector,
    json: Json,
    endpoint: Endpoint,
): RequestBuilder = RequestBuilder(
    client = client,
    uriDirector = director,
    json = json,
    endpoint = endpoint.value
)