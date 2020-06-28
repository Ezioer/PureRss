package com.zxq.purerss

import android.app.Application
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import androidx.appcompat.app.AppCompatDelegate
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/9
 *  fun
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        var nightMode = if (SDK_INT >= Q) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
        val currentNightMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            putSpValue("nightmodel", 1)
        } else {
            if (getSpValue("nightmodel", 0) == 1) {
                nightMode = AppCompatDelegate.MODE_NIGHT_YES
                putSpValue("nightmodel", 1)
            } else {
                putSpValue("nightmodel", 0)
            }
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    companion object {
        var instance: App? = null
            private set
    }
}