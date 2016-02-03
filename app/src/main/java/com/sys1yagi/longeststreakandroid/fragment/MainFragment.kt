package com.sys1yagi.longeststreakandroid.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers
import com.sys1yagi.fragmentcreator.annotation.FragmentCreator
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.api.GithubService
import com.sys1yagi.longeststreakandroid.databinding.FragmentMainBinding
import com.sys1yagi.longeststreakandroid.model.Event
import com.sys1yagi.longeststreakandroid.preference.Account
import com.sys1yagi.longeststreakandroid.tool.PublicContributionJudgement
import com.trello.rxlifecycle.components.support.RxFragment
import rx.schedulers.Schedulers

@FragmentCreator
class MainFragment : RxFragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainFragmentCreator.read(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkContributionOfTheToday(Account.name)
    }

    fun showProgress() {
        binding.statusBoard.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        binding.error.visibility = View.GONE
    }

    fun showStatus() {
        binding.statusBoard.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
        binding.error.visibility = View.GONE
    }

    fun showError(error: Throwable) {
        binding.statusBoard.visibility = View.GONE
        binding.progress.visibility = View.GONE
        binding.error.visibility = View.VISIBLE
        binding.errorText.text = error.message
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
        binding.todayStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
        binding.todayStatus.setText(R.string.ok)
        binding.contributionCount.text = getString(R.string.contribution_count, count)
    }

    fun notYetContributed() {
        binding.todayStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
        binding.todayStatus.setText(R.string.not_yet)
        binding.contributionCount.visibility = View.GONE
    }
}
