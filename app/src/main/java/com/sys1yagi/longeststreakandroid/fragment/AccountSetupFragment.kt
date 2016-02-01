package com.sys1yagi.longeststreakandroid.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sys1yagi.fragmentcreator.annotation.FragmentCreator
import com.sys1yagi.longeststreakandroid.LongestStreakApplication
import com.sys1yagi.longeststreakandroid.R
import com.sys1yagi.longeststreakandroid.databinding.FragmentAccountSetupBinding
import com.sys1yagi.longeststreakandroid.db.Account
import com.sys1yagi.longeststreakandroid.tool.KeyboardManager
import com.trello.rxlifecycle.components.support.RxFragment
import ix.Ix
import org.threeten.bp.DateTimeException
import org.threeten.bp.ZoneId

@FragmentCreator
class AccountSetupFragment : RxFragment() {

    lateinit var binding: FragmentAccountSetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAccountSetupBinding.inflate(inflater);
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            v ->

            Ix.from(
                    verifyName(binding),
                    verifyEmail(binding),
                    verifyZoneId(binding)
            )
                    .filter { result -> result }
                    .count()
                    .filter { count -> count == 3 }
                    .forEach {
                        val account = saveAccount(
                                binding.editName.text.toString(),
                                binding.editEmail.text.toString(),
                                binding.editZoneId.text.toString()
                        )
                        openMainFragment(account)
                    }
        }
        KeyboardManager.show(context)
    }

    fun openMainFragment(account: Account) {
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, MainFragmentCreator.newBuilder(account).build())
                .commit()
    }

    fun saveAccount(name: String, email: String, zoneId: String): Account {
        val account = Account()
        account.name = name
        account.email = email
        account.zoneId = zoneId

        LongestStreakApplication.database.insertIntoAccount(account)
        return account
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
