package ru.dnlkk.tagger.util

enum class SpecialSymbol(private val symbol: String) {
    WIDE_SPACE("&#12288;");

    override fun toString(): String = symbol
}