package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output

import com.dev_bayan_ibrahim.flashcards.data.model.deck.CollectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

@Serializable(OutputFieldCollectionSerializer::class)
data class OutputFieldCollection(
    val message: String,
    val collections: List<String>
)

object OutputFieldCollectionSerializer : KSerializer<OutputFieldCollection> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "OutputFieldCollectionSerializer"
    ) {
        element<String>("message")
        element("collections", CollectionSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): OutputFieldCollection {
        var message: String? = null
        val collections = mutableListOf<String>()
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> message = decodeStringElement(descriptor, index)
                    1 -> {
                        collections.addAll(
                            decodeSerializableElement(descriptor, index, ListSerializer(CollectionSerializer))
                        )
                    }
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return OutputFieldCollection(message = message ?: "", collections = collections)
    }

    override fun serialize(
        encoder: Encoder,
        value: OutputFieldCollection
    ) {
    }
}
