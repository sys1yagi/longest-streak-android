package com.sys1yagi.longeststreakandroid.preference

import com.chibatching.kotpref.KotprefModel

public object Account : KotprefModel() {

    var name: String by stringPrefVar()


    var email: String by stringPrefVar()


    var zoneId: String by stringPrefVar()

}
