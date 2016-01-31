package com.sys1yagi.longeststreakandroid.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.api.GithubService
import com.sys1yagi.longeststreakandroid.databinding.ActivityMainBinding
import com.sys1yagi.longeststreakandroid.model.Event
import com.sys1yagi.longeststreakandroid.tool.PublicContributionJudgement
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import rx.schedulers.Schedulers

class MainActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val publicContributionJudgement = PublicContributionJudgement()

        setSupportActionBar(binding.toolbar)

        binding.contentMain.statusBoard.visibility = View.GONE;
        GithubService.client.userEvents("sys1yagi")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle<List<Event>>())
                .subscribe(
                        { events ->
                            binding.contentMain.statusBoard.visibility = View.VISIBLE
                            binding.contentMain.loading.visibility = View.GONE
                            val count = publicContributionJudgement.todayContributionCount("sys1yagi",
                                    System.currentTimeMillis(),
                                    events);
                            if (count > 0) {
                                binding.contentMain.todayStatus.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.green))
                                binding.contentMain.todayStatus.setText(R.string.ok)
                                binding.contentMain.contributionCount.text = getString(R.string.contribution_count, count)
                            } else {
                                binding.contentMain.todayStatus.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.red))
                                binding.contentMain.todayStatus.setText(R.string.not_yet)
                                binding.contentMain.contributionCount.visibility = View.GONE
                            }
                        },
                        { error ->
                            error.printStackTrace()
                        })
    }
}
