package com.github.funczz.ruby_calc.android

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class NewlineMarkersVisualTransformation(
    private val maker: String = NEWLINE_MAKER_STRING,
    private val spanStyle: SpanStyle = NEWLINE_MAKER_SPAN_STYLE,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val spanStyles = Regex("$maker\n")
            .findAll(text)
            .map { matchResult ->
                AnnotatedString.Range(
                    item = spanStyle,
                    start = matchResult.range.first,
                    end = matchResult.range.last,
                )
            }
            .toList()

        return TransformedText(
            text = AnnotatedString(
                text = text.text,
                spanStyles = spanStyles
            ),
            offsetMapping = OffsetMapping.Identity
        )
    }

    companion object {
        const val NEWLINE_MAKER_STRING: String = "↵"
        val NEWLINE_MAKER_SPAN_STYLE: SpanStyle =
            SpanStyle(color = Color.Cyan, fontWeight = FontWeight.Bold)
    }
}

fun String.newlineMarker(marker: String = ""): String {
    return this.replace("\n".toRegex(), "$marker\n")
}
