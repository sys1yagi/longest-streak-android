package com.sys1yagi.longeststreakandroid.tool

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

object KeyboardManager {

    fun getInputMethodManager(context: Context): InputMethodManager {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun show(context: Context) {
        if (context is Activity) {
            context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        getInputMethodManager(context).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        if (context is Activity) {
            context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
    }
}
