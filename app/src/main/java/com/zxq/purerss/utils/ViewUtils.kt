package com.zxq.purerss.utils

import android.animation.ObjectAnimator
import android.view.View
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
    }
}