package com.sys1yagi.longeststreakandroid.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.databinding.ActivityMainBinding
import com.sys1yagi.longeststreakandroid.fragment.AccountSetupFragmentCreator
import com.sys1yagi.longeststreakandroid.fragment.MainFragmentCreator
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

class MainActivity : RxAppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {

            LongestStreakApplication.database.selectFromAccount().firstOrNull()?.let {
                supportFragmentManager.beginTransaction()
                        .add(R.id.content_frame, MainFragmentCreator.newBuilder(it).build())
                        .commit()
                return
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.content_frame, AccountSetupFragmentCreator.newBuilder().build())
                    .commit()
        }
    }


}
