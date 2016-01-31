package com.sys1yagi.longeststreakandroid.testtool

import java.io.File

object AssetsUtilForTest {

    val PATH = "src/test/assets"

    fun readString(fileName: String): String {
        return File(PATH, fileName).inputStream().bufferedReader("utf-8").readText()
    }
}
