package com.zxq.purerss.utils

import android.animation.ObjectAnimator
import android.graphics.Color
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.core.transition.addListener
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/29
 *  fun
 */
class MotionUtil {
    companion object {
        fun startContainerTransform(start: View, end: View, root: ViewGroup, onEnd: EndListener) {
            val motionTransform = MaterialContainerTransform().apply {
                startView = start
                endView = end
                duration = 600
//                pathMotion = MaterialArcMotion()
                scrimColor = Color.TRANSPARENT
            }
            /*    motionTransform.addListener(onEnd = {
                    onEnd.onMotionEnd()
                })

                TransitionManager.beginDelayedTransition(root, motionTransform)*/
            start.visibility = View.GONE
            end.visibility = View.VISIBLE
        }

        fun startLeftRightAnimation(view: View) {
            val trans = ObjectAnimator.ofFloat(view, "translationX", 0f, 15f, 0f, -15f, 0f)
            trans.duration = 100
            trans.repeatCount = 2
            trans.start()
        }
    }

    interface EndListener {
        fun onMotionEnd()
    }
}