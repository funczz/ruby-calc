package com.github.funczz.ruby_calc.android.destination.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainDestinations(
    val icon: ImageVector
) {
    PROBLEM(Icons.Filled.Book),
    PROGRAM(Icons.Filled.Code),
    SETTINGS(Icons.Filled.Settings),
}
