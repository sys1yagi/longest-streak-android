package com.sys1yagi.longeststreakandroid.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
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
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkContributionOfTheToday(Account.name, Account.zoneId)
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

    fun checkContributionOfTheToday(name: String, zoneId: String) {
        showProgress()

        GithubService.client.userEvents(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle<List<Event>>())
                .subscribe(
                        { events -> setupStatus(name, zoneId, events) },
                        { error -> showError(error) }
                )
    }

    fun setupStatus(name: String, zoneId: String, events: List<Event>) {
        showStatus()

        val publicContributionJudgement = PublicContributionJudgement()
        val count = publicContributionJudgement.todayContributionCount(
                name, zoneId,
                System.currentTimeMillis(),
                events);
        binding.userName.text = getString(R.string.hi_today, Account.name)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_settings -> {
                fragmentManager
                        .beginTransaction()
                        .addToBackStack("Settings")
                        .replace(R.id.content_frame, AccountSetupFragmentCreator.newBuilder()
                                .setIsEditMode(true)
                                .build())
                        .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
