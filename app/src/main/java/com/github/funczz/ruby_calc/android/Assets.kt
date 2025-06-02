package com.github.funczz.ruby_calc.android

import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream

sealed interface Assets {

    val fileName: String

    val assetManager: AssetManager

    fun isFile(): Boolean

    fun list(): List<Assets>

    fun fileTree(): List<File>

    fun use(function: (InputStream) -> Unit)

    fun useWithFileName(function: (Pair<String, InputStream>) -> Unit)

    class Directory(
        override val fileName: String,
        override val assetManager: AssetManager,
    ) : Assets {

        override fun isFile(): Boolean = false

        override fun list(): List<Assets> = assetManager.list(fileName)?.map {
            assetManager.getAssets(fileName = "%s/%s".format(fileName, it))
        } ?: emptyList()

        override fun fileTree(): List<File> {
            val tree = mutableListOf<File>()
            tailRecEach(list()) { tree.add(it) }
            return tree
        }

        override fun use(function: (InputStream) -> Unit) {
            tailRecEach(list()) { assetManager.open(it.fileName).use(function) }
        }

        override fun useWithFileName(function: (Pair<String, InputStream>) -> Unit) {
            tailRecEach(list()) {
                assetManager.open(it.fileName).use { i -> function((it.fileName to i)) }
            }
        }

        private tailrec fun tailRecEach(assetsList: List<Assets>, function: (File) -> Unit) {
            val first = assetsList.firstOrNull()
            val rest = assetsList.drop(1).toMutableList()
            when (first) {
                is File -> function(first)
                is Directory -> rest.addAll(first.list())
                else -> return
            }
            tailRecEach(assetsList = rest, function = function)
        }

    }

    class File(
        override val fileName: String,
        override val assetManager: AssetManager,
    ) : Assets {

        override fun isFile(): Boolean = true

        override fun list(): List<Assets> = emptyList()

        override fun fileTree(): List<File> = mutableListOf(this)

        override fun use(function: (InputStream) -> Unit) {
            assetManager.open(fileName).use(function)
        }

        override fun useWithFileName(function: (Pair<String, InputStream>) -> Unit) {
            assetManager.open(fileName).use { i -> function((fileName to i)) }
        }

    }

}

fun AssetManager.isFile(fileName: String): Boolean = try {
    this.open(fileName)
    true
} catch (_: IOException) {
    false
}

fun AssetManager.getAssets(fileName: String): Assets = when (this.isFile(fileName)) {
    true -> Assets.File(fileName = fileName, assetManager = this)
    else -> Assets.Directory(fileName = fileName, assetManager = this)
}
