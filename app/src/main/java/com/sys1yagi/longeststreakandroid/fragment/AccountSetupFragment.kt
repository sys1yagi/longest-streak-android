package com.sys1yagi.longeststreakandroid.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sys1yagi.fragmentcreator.annotation.Args
import com.sys1yagi.fragmentcreator.annotation.FragmentCreator
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.databinding.FragmentAccountSetupBinding
import com.sys1yagi.longeststreakandroid.db.Settings
import com.sys1yagi.longeststreakandroid.tool.KeyboardManager
import com.trello.rxlifecycle.components.support.RxFragment
import org.threeten.bp.DateTimeException
import org.threeten.bp.ZoneId

@FragmentCreator
class AccountSetupFragment : RxFragment() {

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
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAccountSetupBinding.inflate(inflater);
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Settings.getRecord(LongestStreakApplication.database)?.let {
            binding.editName.setText(it.name)
            binding.editEmail.setText(it.email)
            binding.editZoneId.setText(it.zoneId)
        }

        binding.registerButton.setOnClickListener {
            v ->
            if (verifyName(binding) && verifyEmail(binding) && verifyZoneId(binding)) {
                val settings = saveAccount(
                        binding.editName.text.toString(),
                        binding.editEmail.text.toString(),
                        binding.editZoneId.text.toString()
                )
                openMainFragment(settings)
            }
        }
        KeyboardManager.show(context)
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
        val (settings, saveAction) = Settings.getRecordAndAction(LongestStreakApplication.database)
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
