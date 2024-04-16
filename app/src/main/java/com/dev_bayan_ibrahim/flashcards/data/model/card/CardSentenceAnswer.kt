package com.dev_bayan_ibrahim.flashcards.data.model.card

interface Language {
    val validCharacters: Set<Char>
    val ignorableCharacters: Set<Char>
    val interchangableCharacters: LanguageInterchangeables

    fun normalize(input: String): String {

        return input
    }
}

enum class InitialLanguages(
    override val validCharacters: Set<Char>,
    override val ignorableCharacters: Set<Char>,
    interchangeables: List<List<Char>>,
) : Language {
    En(
        validCharacters = setOf(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'á', 'ä', 'ã', 'à', 'â', 'ă', 'ā', 'ȁ', 'ȃ', 'ǎ', 'ȧ', 'å', 'ą', 'ḁ',
            'è', 'é', 'ê', 'ë', 'ĕ', 'ė', 'ē', 'ȅ', 'ȇ', 'ě', 'ę', 'ḙ',
            'ì', 'í', 'î', 'ï', 'ĭ', 'ī', 'ȉ', 'ȋ', 'ǐ', 'į', 'ḭ',
            'ò', 'ó', 'ô', 'ö', 'õ', 'ŏ', 'ō', 'ȍ', 'ȏ', 'ǒ', 'ø', 'ő', 'ṑ',
            'ù', 'ú', 'û', 'ü', 'ŭ', 'ū', 'ȕ', 'ȗ', 'ǔ', 'ų', 'ṻ',
        ),
        ignorableCharacters = setOf('-', '\''),
        interchangeables = listOf(
            listOf('a', 'á', 'ä', 'ã', 'à', 'â', 'ă', 'ā', 'ȁ', 'ȃ', 'ǎ', 'ȧ', 'å', 'ą', 'ḁ'),
            listOf('e', 'è', 'é', 'ê', 'ë', 'ĕ', 'ė', 'ē', 'ȅ', 'ȇ', 'ě', 'ę', 'ḙ'),
            listOf('i', 'ì', 'í', 'î', 'ï', 'ĭ', 'ī', 'ȉ', 'ȋ', 'ǐ', 'į', 'ḭ'),
            listOf('o', 'ò', 'ó', 'ô', 'ö', 'õ', 'ŏ', 'ō', 'ȍ', 'ȏ', 'ǒ', 'ø', 'ő', 'ṑ'),
            listOf('u', 'ù', 'ú', 'û', 'ü', 'ŭ', 'ū', 'ȕ', 'ȗ', 'ǔ', 'ų', 'ṻ'),
        ),
    ),
    Ar(
        validCharacters = setOf(
            'ا', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ', 'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض', 'ط',
            'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل', 'م', 'ن', 'ه', 'و', 'ي', 'ء', 'آ', 'أ', 'إ', 'ؤ',
            'ئ', 'ة', 'ـ', 'ً', 'ٍ', 'ٌ', 'ّ', 'ْ', 'َ', 'ُ', 'ِ',
        ),
        ignorableCharacters = setOf(
            'ـ', 'ً', 'ٍ', 'ٌ', 'ّ', 'ْ', 'َ', 'ُ', 'ِ',
        ),
        interchangeables = listOf(
            listOf('ا', 'آ', 'أ', 'إ'),
            listOf('ء', 'أ', 'إ', 'ؤ', 'ئ'),
        ),
    );

    override val interchangableCharacters = LanguageInterchangeables(interchangeables)

    companion object {
        fun languageOf(input: String): Language {
            val normalized = input.lowercase().filter { it.isLetter() }
            val hasEnglish = true
            val hasArabic = true
            val valid = mutableSetOf<Char>()
            val ignorables = mutableSetOf<Char>()
            val interchangeables = mutableListOf<List<Char>>()
            if (hasEnglish) {
                valid.addAll(En.validCharacters)
                ignorables.addAll(En.ignorableCharacters)
                interchangeables.addAll(En.interchangableCharacters.originalGroups)
            }
            if (hasArabic) {
                valid.addAll(Ar.validCharacters)
                ignorables.addAll(Ar.ignorableCharacters)
                interchangeables.addAll(Ar.interchangableCharacters.originalGroups)
            }

            return object : Language {
                override val validCharacters: Set<Char> = valid
                override val ignorableCharacters: Set<Char> = ignorables
                override val interchangableCharacters= LanguageInterchangeables(interchangeables)
            }
        }
    }
}

class LanguageInterchangeables(val originalGroups: List<List<Char>>) {
    private val entries: Map<Char, Int> = originalGroups.mapIndexed { index, chars ->
        chars.map { it to index }
    }.flatten().toMap()

    private val firsts: Map<Int, Char> = originalGroups.mapIndexedNotNull() { index, chars ->
        chars.firstOrNull()?.run { index to this }
    }.toMap()

    fun isEmpty(): Boolean = originalGroups.all { it.isEmpty() }

    operator fun get(key: Char): Int? = entries[key]
    fun containsValue(value: Int): Boolean = entries.containsValue(value)
    fun containsKey(key: Char): Boolean = entries.containsKey(key)

    /**
     * returns false if none of characters is between entities
     */
    fun interchangeables(char1: Char, char2: Char): Boolean {
        val group1 = entries[char1] ?: false
        val group2 = entries[char2] ?: false
        return group1 == group2
    }

    fun firstOfGroup(char: Char, keepUppercase: Boolean = false): Char {
        val first = entries[char]?.run { firsts[this] } ?: char

        return if (keepUppercase && char.isUpperCase()) {
            first.uppercaseChar()
        } else first
    }

    // return true if the two strings are interchangeables
    fun interchangeables(
        string1: String,
        string2: String,
        ignoreCase: Boolean = true,
    ): Boolean {
        if (ignoreCase) return interchangeables(string1.lowercase(), string2.lowercase(), false)
        if (string1.length != string2.length) return false
        repeat(string1.length) {
            val iCharsInterchangeables = interchangeables(string1[it], string2[it])
            if (!iCharsInterchangeables) return false
        }
        return true
    }

    /**
     * @return each char of the new string is the first char of the old string group
     */
    fun mapToFirstOfGroup(string: String, keepUppercase: Boolean = false): String {
        return buildString {
            string.forEach {
                append(firstOfGroup(it, keepUppercase))
            }
        }
    }
}
