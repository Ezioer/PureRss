package com.zxq.purerss.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.view.View
import android.app.Activity



/**
 * Created by xiaoqing.zhou
 * on  2018/12/6
 */
class KeyBoardUtil {
    companion object {
        fun showKeyboard(view: View) {
            val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                view.requestFocus()
                imm!!.showSoftInput(view, 0)
            }
        }

        fun hideKeyboard(view: View) {
            val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                imm!!.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun toggleSoftInput(context: Context) {
            val imm = context.applicationContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                imm!!.toggleSoftInput(0, 0)
            }
        }

        fun hide(context: Context) {
            //toggle(context);
            val view = (context as Activity).window.peekDecorView()
            if (view != null && view.windowToken != null) {
                val imm= context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}