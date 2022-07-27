package com.zxq.purerss

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.previewlibrary.ZoomMediaLoader
import com.tencent.mmkv.MMKV
import com.zxq.purerss.ui.friends.preview.GlideImageLoader
import com.zxq.purerss.utils.TypefaceUtil
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
        MMKV.initialize(this)
//        SystemUtil.enableGray(true)
        TypefaceUtil.replaceSystemDefaultFont(this, "fonts/kaiti.ttf")
        ZoomMediaLoader.getInstance().init(GlideImageLoader())
//        val ls = MmkvUtils.getInstance().getValue("111",String)
        lookActivityEvent()
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
    private fun lookActivityEvent() {
        //埋点方案，监控每个activity的启动和销毁
        registerActivityLifecycleCallbacks(object: ActivityLifecycleCallbacks{
            override fun onActivityPaused(p0: Activity) {
                //离开activty
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityDestroyed(p0: Activity) {
                //销毁activity
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityResumed(p0: Activity) {
                //进入activity
            }

        })
    }

    companion object {
        var instance: App? = null
            private set
    }
}