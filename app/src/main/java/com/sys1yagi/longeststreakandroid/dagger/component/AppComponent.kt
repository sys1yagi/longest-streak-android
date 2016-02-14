package com.sys1yagi.longeststreakandroid.dagger.component

import com.sys1yagi.longeststreakandroid.api.Github
import com.sys1yagi.longeststreakandroid.dagger.module.IoModule
import com.sys1yagi.longeststreakandroid.dagger.module.NetworkModule
import com.sys1yagi.longeststreakandroid.fragment.AccountSetupFragment
import com.sys1yagi.longeststreakandroid.fragment.MainFragment
import dagger.Component

@Component(modules = arrayOf(NetworkModule::class, IoModule::class))
interface AppComponent {

    fun inject(target: MainFragment)
    fun inject(target: AccountSetupFragment)
    fun provideGithub(): Github
}
