package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

data class OutputField<T>(
    val count: Int = 0,
    val currentPage: Int? = null,
    val lastPage: Int? = null,
    val results: List<T> = emptyList(),
)

class OutputFieldSerializer<T>(
    val resultsSerializer: KSerializer<T>
) : KSerializer<OutputField<T>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "QubitPaginatedOutputFieldSerializer"
    ) {
        element<Int>("total_count")
        element<String>("current_page")
        element<String>("last_page")
        element("decks", resultsSerializer.descriptor)
//        element("property", SomeSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): OutputField<T> {
        var count: Int? = null
        var next: Int? = null
        var previous: Int? = null
        val results = mutableListOf<T>()
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> count = decodeIntElement(descriptor, index)
                    1 -> next = decodeIntElement(descriptor, index)
                    2 -> previous = decodeIntElement(descriptor, index)
                    3 -> results.addAll(
                        decodeSerializableElement(descriptor, index, ListSerializer(resultsSerializer))
                    )
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return OutputField(
            count = count ?: results.count(),
            currentPage = next,
            lastPage = previous,
            results = results
        )
    }

    override fun serialize(
        encoder: Encoder,
        value: OutputField<T>
    ) {
    }
}
