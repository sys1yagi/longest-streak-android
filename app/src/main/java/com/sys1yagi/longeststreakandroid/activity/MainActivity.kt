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
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        checkContributionOfTheToday("sys1yagi")
    }

    fun showProgress() {
        binding.contentMain.statusBoard.visibility = View.GONE
        binding.contentMain.progress.visibility = View.VISIBLE
        binding.contentMain.error.visibility = View.GONE
    }

    fun showStatus() {
        binding.contentMain.statusBoard.visibility = View.VISIBLE
        binding.contentMain.progress.visibility = View.GONE
        binding.contentMain.error.visibility = View.GONE
    }

    fun showError(error: Throwable) {
        binding.contentMain.statusBoard.visibility = View.GONE
        binding.contentMain.progress.visibility = View.GONE
        binding.contentMain.error.visibility = View.VISIBLE
        binding.contentMain.errorText.text = error.message
    }

    fun checkContributionOfTheToday(name: String) {
        showProgress()

        GithubService.client.userEvents(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle<List<Event>>())
                .subscribe(
                        { events -> setupStatus(name, events) },
                        { error -> showError(error) }
                )
    }

    fun setupStatus(name: String, events: List<Event>) {
        showStatus()

        val publicContributionJudgement = PublicContributionJudgement()
        val count = publicContributionJudgement.todayContributionCount(name,
                System.currentTimeMillis(),
                events);
        if (count > 0) {
            alreadyContributed(count)
        } else {
            notYetContributed()
        }
    }

    fun alreadyContributed(count: Int) {
        binding.contentMain.todayStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
        binding.contentMain.todayStatus.setText(R.string.ok)
        binding.contentMain.contributionCount.text = getString(R.string.contribution_count, count)
    }

    fun notYetContributed() {
        binding.contentMain.todayStatus.setTextColor(ContextCompat.getColor(this, R.color.red))
        binding.contentMain.todayStatus.setText(R.string.not_yet)
        binding.contentMain.contributionCount.visibility = View.GONE
    }
}
