package com.zxq.purerss.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager


/**
 * Created by xiaoqing.zhou
 * on  2019/5/6
 */
class StatusBarUtil {
    companion object {
        //状态栏透明
        fun settranslucent(activity: Activity) {
            if (Build.VERSION.SDK_INT >= 21) {
                val decorView = activity.window.decorView
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                decorView.systemUiVisibility = option
                activity.window.statusBarColor = Color.TRANSPARENT
            } else {
                // 设置状态栏透明
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                // 设置根布局的参数
                val rootView = (activity.findViewById<View>(
                    android.R.id
                        .content
                ) as ViewGroup).getChildAt(0) as ViewGroup
                rootView.fitsSystemWindows = false
                rootView.clipToPadding = true
            }
        }

        fun StatusBarLightMode(activity: Activity): Int {
            var result = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                when {
                    MIUISetStatusBarLightMode(activity.window, true) -> result = 1
                    FlymeSetStatusBarLightMode(activity.window, true) -> result = 2
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        result = 3
                    }
                }
            }
            return result
        }

        fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
            var result = false
            if (window != null) {
                try {
                    val lp = window!!.attributes
                    val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                    val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                    darkFlag.isAccessible = true
                    meizuFlags.isAccessible = true
                    val bit = darkFlag.getInt(null)
                    var value = meizuFlags.getInt(lp)
                    value = if (dark) {
                        value or bit
                    } else {
                        value and bit.inv()
                    }
                    meizuFlags.setInt(lp, value)
                    window?.attributes = lp
                    result = true
                } catch (e: Exception) {
                }

            }
            return result
        }

        fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
            var result = false
            if (window != null) {
                val clazz = window?.javaClass
                try {
                    var darkModeFlag = 0
                    val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                    val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                    darkModeFlag = field.getInt(layoutParams)
                    val extraFlagField =
                        clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                    if (dark) {
                        extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                    } else {
                        extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                    }
                    result = true
                } catch (e: Exception) {
                }

            }
            return result
        }
    }
}