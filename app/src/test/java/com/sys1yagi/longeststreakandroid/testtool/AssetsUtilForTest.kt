package com.sys1yagi.longeststreakandroid.testtool

import java.io.File
import java.nio.charset.Charset

object AssetsUtilForTest {

    val PATH = "src/test/assets"

    fun readString(fileName: String): String {
        return File(PATH, fileName).inputStream().bufferedReader(Charset.forName("utf-8")).readText()
    }
}
