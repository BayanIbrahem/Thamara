package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.uri_decerator

import io.ktor.http.URLBuilder

/**
 * specific url decorator used to costomize uri builder to a spesific api
 */
interface UriDirector {
    /**
     * director for uri
     */
    fun URLBuilder.director(): URLBuilder
}