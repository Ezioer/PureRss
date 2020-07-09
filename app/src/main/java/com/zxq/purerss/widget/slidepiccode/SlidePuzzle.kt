package com.zxq.purerss.widget.slidepiccode

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.zxq.purerss.R
import kotlinx.android.synthetic.main.layout_slidepuzzle.view.*
import java.util.logging.Logger
import kotlin.math.abs
import kotlin.math.log

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/9
 *  fun
 */
class SlidePuzzle(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    private lateinit var mSlideBar: SlideBar
    private lateinit var mPuzzle: Puzzle

    private var onVerify: ((Boolean) -> Unit)? = null

    fun setOnVerifyListener(listener: (Boolean) -> Unit) {
        onVerify = listener
    }

    init {
        View.inflate(context, R.layout.layout_slidepuzzle, this)
        mSlideBar = slidebar
        mPuzzle = puzzle
        mSlideBar.setOnDragListener { progress, time, b ->
            mPuzzle.setProgress(progress)
            if (b) {
                Log.i(
                    "slideprogress",
                    "progress----->" + progress + "x-------->" + mPuzzle.getRandomX()
                )
                verify(abs(progress * 0.9 - mPuzzle.getRandomX()) < 0.018, time!!)
            }
        }
    }

    private fun verify(isSuccess: Boolean, userTime: Float) {
        if (isSuccess) {
            mPuzzle.showSuccess()
            onVerify?.invoke(isSuccess)
        } else {
            mSlideBar.reset()
            onVerify?.invoke(isSuccess)
        }
    }
}