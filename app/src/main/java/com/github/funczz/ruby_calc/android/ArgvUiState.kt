package com.github.funczz.ruby_calc.android

data class ArgvUiState(
    val argv: MutableList<TextFieldState> = mutableListOf(),
) {
    fun toARGV(): List<String> = argv.map { it.value.text }
}
