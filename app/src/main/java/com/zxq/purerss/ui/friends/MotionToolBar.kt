package com.zxq.purerss.ui.friends

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_friendscircle.view.*

class MotionToolBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), AppBarLayout.OnOffsetChangedListener {

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        Log.e(
            "MotionToolBar",
            "onOffsetChanged: ----->$verticalOffset, scroll range--> ${appBarLayout?.totalScrollRange}"
        )
        val seekPosition = -verticalOffset / (appBarLayout?.totalScrollRange!!.toFloat() / 6 * 5)
        arcview.arcHeight = (verticalOffset + 540) / 5.toFloat()
        progress = seekPosition
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as? AppBarLayout)?.addOnOffsetChangedListener(this)
    }
}