package com.sys1yagi.longeststreakandroid.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.*
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers
import com.sys1yagi.fragmentcreator.annotation.FragmentCreator
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.api.GithubService
import com.sys1yagi.longeststreakandroid.databinding.FragmentMainBinding
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase
import com.sys1yagi.longeststreakandroid.db.Settings
import com.sys1yagi.longeststreakandroid.model.Event
import com.sys1yagi.longeststreakandroid.tool.PublicContributionJudgement
import com.trello.rxlifecycle.components.support.RxFragment
import retrofit2.Response
import rx.schedulers.Schedulers
import javax.inject.Inject

@FragmentCreator
class MainFragment : RxFragment() {

    @Inject
    lateinit var githubService: GithubService

    lateinit var database: OrmaDatabase

    lateinit var settings: Settings

    lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainFragmentCreator.read(this)
        setHasOptionsMenu(true)
        (context.applicationContext as LongestStreakApplication).component.inject(this)
        database = (context.applicationContext as LongestStreakApplication).database
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        settings = Settings.getRecord(database)
        checkContributionOfTheToday(settings)
    }

    fun showProgress(message: String = "") {
        activity.runOnUiThread {
            binding.statusBoard.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
            binding.error.visibility = View.GONE
            if (!TextUtils.isEmpty(message)) {
                binding.loadingText.text = message
            }
        }
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

    fun checkContributionOfTheToday(settings: Settings) {
        showProgress()

        githubService.userEvents(settings.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle<Response<List<Event>>>())
                .subscribe(
                        { response ->
                            setupStatus(settings, response.body())
                        },
                        { error -> showError(error) }
                )
    }

    fun setupStatus(settings: Settings, events: List<Event>) {
        showStatus()

        val publicContributionJudgement = PublicContributionJudgement()
        val count = publicContributionJudgement.todayContributionCount(
                settings.name, settings.zoneId,
                System.currentTimeMillis(),
                events);
        binding.userName.text = getString(R.string.hi_today, settings.name)
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
