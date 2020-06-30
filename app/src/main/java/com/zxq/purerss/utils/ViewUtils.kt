package com.zxq.purerss.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

/**
 * Created by xiaoqing.zhou
 * on  2019/1/10
 */
class ViewUtils {
    companion object {
        fun expanAni(v: View) {
            val animation = ObjectAnimator.ofFloat(v,"Rotation",0f, 180f)
            animation.duration = 250
            animation.start()
        }

        fun collAni(v: View) {
            val animation = ObjectAnimator.ofFloat(v,"Rotation",180f, 360f)
            animation.duration = 250
            animation.start()
        }

        fun circleAni(v: View){
            val animator = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f)
            animator.duration = 500
            animator.repeatCount = 3
            animator.start()
        }

        //状态栏透明
        fun settranslucent(activity: Activity) {
            if (Build.VERSION.SDK_INT >= 21) {
                val decorView = activity.window.decorView
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                decorView.systemUiVisibility = option
                activity.window.statusBarColor = Color.TRANSPARENT
            } else {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                /*    // 设置状态栏透明
                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    // 设置根布局的参数
                    val rootView = (activity.findViewById<View>(android.R.id
                            .content) as ViewGroup).getChildAt(0) as ViewGroup
                    rootView.fitsSystemWindows = false
                    rootView.clipToPadding = true*/
            }
        }
    }
}