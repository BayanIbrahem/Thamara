package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output

import com.dev_bayan_ibrahim.flashcards.data.model.deck.TagSerializer1
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

@Serializable(OutputFieldTagSerializer::class)
data class OutputFieldTag(
    val message: String,
    val tags: List<String>
)

object OutputFieldTagSerializer : KSerializer<OutputFieldTag> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "OutputFieldTagSerializer"
    ) {
        element<String>("message")
        element("tags", TagSerializer1.descriptor)
    }

    override fun deserialize(decoder: Decoder): OutputFieldTag {
        var message: String? = null
        val tags = mutableListOf<String>()
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> message = decodeStringElement(descriptor, index)
                    1 -> {
                        tags.addAll(
                            decodeSerializableElement(descriptor, index, ListSerializer(TagSerializer1))
                        )
                    }
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return OutputFieldTag(message = message ?: "", tags = tags)
    }

    override fun serialize(
        encoder: Encoder,
        value: OutputFieldTag
    ) {
    }
}
