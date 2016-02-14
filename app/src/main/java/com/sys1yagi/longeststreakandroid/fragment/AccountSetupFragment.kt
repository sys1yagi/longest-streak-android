package com.sys1yagi.longeststreakandroid.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers
import com.sys1yagi.fragmentcreator.annotation.Args
import com.sys1yagi.fragmentcreator.annotation.FragmentCreator
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.api.GithubService
import com.sys1yagi.longeststreakandroid.databinding.FragmentAccountSetupBinding
import com.sys1yagi.longeststreakandroid.db.EventLog
import com.sys1yagi.longeststreakandroid.db.OrmaDatabase
import com.sys1yagi.longeststreakandroid.db.Settings
import com.sys1yagi.longeststreakandroid.model.Event
import com.sys1yagi.longeststreakandroid.tool.KeyboardManager
import com.trello.rxlifecycle.components.support.RxFragment
import org.threeten.bp.DateTimeException
import org.threeten.bp.ZoneId
import retrofit2.Response
import rx.schedulers.Schedulers
import javax.inject.Inject

@FragmentCreator
class AccountSetupFragment : RxFragment() {
    @Inject
    lateinit var githubService: GithubService

    lateinit var database: OrmaDatabase

    @Args(require = false)
    var isEditMode: Boolean = false

    lateinit var binding: FragmentAccountSetupBinding

    // workaround for fragment-creator
    fun setIsEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AccountSetupFragmentCreator.read(this)
        (context.applicationContext as LongestStreakApplication).component.inject(this)
        database = (context.applicationContext as LongestStreakApplication).database
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAccountSetupBinding.inflate(inflater);
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Settings.alreadyInitialized(database)) {
            Settings.getRecord(database).let {
                binding.editName.setText(it.name)
                binding.editEmail.setText(it.email)
                binding.editZoneId.setText(it.zoneId)
            }
        }

        binding.registerButton.setOnClickListener {
            v ->
            if (verifyName(binding) && verifyEmail(binding) && verifyZoneId(binding)) {
                val settings = saveAccount(
                        binding.editName.text.toString(),
                        binding.editEmail.text.toString(),
                        binding.editZoneId.text.toString()
                )
                syncEvents(settings,
                        {
                            openMainFragment(it)
                        },
                        {
                            it.printStackTrace()
                            showForm()
                        })
            }
        }
        KeyboardManager.show(context)
    }

    fun showLoading(){
        binding.settingsForm.visibility = View.GONE
        binding.loadingContainer.visibility = View.VISIBLE
        binding.loading.start()
    }
    fun showForm(){
        binding.settingsForm.visibility = View.VISIBLE
        binding.loadingContainer.visibility = View.GONE
        binding.loading.stop()
    }

    fun syncEvents(settings: Settings, callback: (Settings) -> Unit, error: (Throwable) -> Unit): Unit {
        showLoading()
        database.deleteFromEventLog().execute()
        githubService.userAllEvents(settings.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle<List<Event>>())
                .subscribe(
                        { events ->
                            events.forEach {
                                database.insertIntoEventLog(EventLog.toEventLog(settings.name, it))
                            }
                        },
                        {
                            error.invoke(it)
                        },
                        {
                            callback.invoke(settings)
                        }
                )
    }

    fun openMainFragment(settings: Settings) {
        if (isEditMode) {
            fragmentManager.popBackStack()
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, MainFragmentCreator.newBuilder().build())
                    .commit()
        }
    }

    fun saveAccount(name: String, email: String, zoneId: String): Settings {
        val (settings, saveAction) = Settings.getRecordAndAction(database)
        settings.name = name
        settings.email = email
        settings.zoneId = zoneId
        return saveAction.invoke(settings)
    }

    fun verifyName(binding: FragmentAccountSetupBinding): Boolean {
        if (TextUtils.isEmpty(binding.editName.text)) {
            binding.formName.error = "name is empty"
            return false
        } else {
            binding.formName.isErrorEnabled = false
            return true
        }
    }

    fun verifyEmail(binding: FragmentAccountSetupBinding): Boolean {
        if (TextUtils.isEmpty(binding.editEmail.text)) {
            binding.formEmail.error = "email is empty"
            return false
        } else {
            binding.formEmail.isErrorEnabled = false
            return true
        }
    }

    fun verifyZoneId(binding: FragmentAccountSetupBinding): Boolean {
        val zoneId = binding.editZoneId.text.toString()
        try {
            ZoneId.of(zoneId)
        } catch(e: DateTimeException) {
            binding.formZoneId.error = "zoneId is invalid : ${zoneId}"
            return false
        }
        binding.formZoneId.isErrorEnabled = false
        return true
    }
}
