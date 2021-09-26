package com.zxq.purerss.ui.wxboom

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.IntentFilter
import android.graphics.PathMeasure
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.zxq.purerss.R
import com.zxq.purerss.utils.lerp
import com.zxq.purerss.widget.ControlFocusInsetsAnimationCallback
import com.zxq.purerss.widget.RootViewDeferringInsetsCallback
import com.zxq.purerss.widget.TranslateDeferringInsetsAnimationCallback
import kotlinx.android.synthetic.main.activity_wxboom.*
import java.lang.Exception

/**
 *  created by xiaoqing.zhou
 *  on 2021/9/15
 *  fun
 */
class WxBoomActivity : AppCompatActivity() {
    private var isPlay = false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wxboom)
        window.setDecorFitsSystemWindows(false)
        /*root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
        var list = mutableListOf<ImMsgInfo>()
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        val adapter = WxBoomAdapter()
        rv_chat.adapter = adapter
        adapter.addData(list)
        rv_chat.smoothScrollToPosition(list.size)

        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        ViewCompat.setWindowInsetsAnimationCallback(root, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(root, deferringInsetsListener)

        ViewCompat.setWindowInsetsAnimationCallback(
            message_holder,
            TranslateDeferringInsetsAnimationCallback(
                view =message_holder,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
                // We explicitly allow dispatch to continue down to binding.messageHolder's
                // child views, so that step 2.5 below receives the call
                dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
            )
        )

        ViewCompat.setWindowInsetsAnimationCallback(
            rv_chat,
            TranslateDeferringInsetsAnimationCallback(
                view = rv_chat,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )

        ViewCompat.setWindowInsetsAnimationCallback(
           message_edittext,
            ControlFocusInsetsAnimationCallback(message_edittext)
        )

        /*ViewCompat.setOnApplyWindowInsetsListener(rv_chat) { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsets.bottom)
            insets
        }


        val view = rv_chat
        val cb = @RequiresApi(Build.VERSION_CODES.R)
        object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            var startBottom = 0
            var endBottom = 0
            var anima: WindowInsetsAnimation? = null
            override fun onPrepare(animation: WindowInsetsAnimation) {
                startBottom = view.bottom
            }

            override fun onStart(
                animation: WindowInsetsAnimation,
                bounds: WindowInsetsAnimation.Bounds
            ): WindowInsetsAnimation.Bounds {
                anima = animation
                endBottom = view.bottom
                view.translationY = (startBottom - endBottom).toFloat()
                return bounds
            }

            override fun onProgress(
                p0: WindowInsets,
                p1: MutableList<WindowInsetsAnimation>
            ): WindowInsets {
                val offset =
                    lerp((startBottom - endBottom).toFloat(), 0f, anima?.interpolatedFraction!!)
                view.translationY = offset
                return p0
            }
        }*/

        /*val cb1 = @RequiresApi(Build.VERSION_CODES.R)
        object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            var startBottom = 0
            var endBottom = 0
            var anima: WindowInsetsAnimation? = null
            override fun onPrepare(animation: WindowInsetsAnimation) {
                startBottom = tv_inputcontent.bottom
            }

            override fun onStart(
                animation: WindowInsetsAnimation,
                bounds: WindowInsetsAnimation.Bounds
            ): WindowInsetsAnimation.Bounds {
                anima = animation
                endBottom = tv_inputcontent.bottom
                tv_inputcontent.translationY = (startBottom - endBottom).toFloat()
                return bounds
            }

            override fun onProgress(
                p0: WindowInsets,
                p1: MutableList<WindowInsetsAnimation>
            ): WindowInsets {
                val offset =
                    lerp((startBottom - endBottom).toFloat(), 0f, anima?.interpolatedFraction!!)
                tv_inputcontent.translationY = offset
                return p0
            }
        }*/
//        view.setWindowInsetsAnimationCallback(cb)
//        tv_inputcontent.setWindowInsetsAnimationCallback(cb1)
//        tv_inputcontent.setOnClickListener {
//            ll_emoji.visibility = View.VISIBLE
//            rv_chat.smoothScrollToPosition(list.size)
    }
    /*   tv_boom.setOnClickListener {
           tv_inputcontent.setText("\uD83D\uDCA3")
       }*/
    /* btn_send.setOnClickListener {
//            list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
//            adapter.addData(ImMsgInfo(1, "\uD83D\uDCA3"))
         ll_emoji.visibility = View.GONE
//            rv_chat.smoothScrollToPosition(list.size)
//            adapter.notifyItemInserted(list.size)
         iv_boom.visibility = View.VISIBLE
         iv_boom.performClick()
     }
     iv_boom.setOnClickListener {
         val resource = this.resources
         var dm = resource.displayMetrics
         var controlX = dm.widthPixels / 2 - 400
         var controlY = dm.heightPixels / 2 - 400
         var path = android.graphics.Path()
         path.quadTo(
             -controlX.toFloat(),
             -controlY.toFloat(),
             -(iv_boom.x - 200),
             -(iv_boom.y - 1000)
         )
         var pos = FloatArray(2)
         var pathMeasure = PathMeasure(path, false)
         var anima = ValueAnimator.ofFloat(0f, pathMeasure.length)
         anima.interpolator = LinearInterpolator()
         anima.duration = 1000
         anima.addUpdateListener {
             val value = it.animatedValue as Float
             pathMeasure.getPosTan(value, pos, null)
             iv_boom.translationX = pos[0]
             iv_boom.translationY = pos[1]
             iv_boom.rotation = value
         }
         anima.addListener(onEnd = {
             iv_boom.visibility = View.GONE
             lottie_view.visibility = View.VISIBLE
             lottie_view.playAnimation()
             lottie_view.addAnimatorUpdateListener {
                 var value = it.animatedValue as Float
                 if (value >= (lottie_view.duration / 4) && !isPlay) {
                     isPlay = true
                 }
             }
             shitview.visibility = View.VISIBLE
             var scaleX = ObjectAnimator.ofFloat(shitview, "scaleX", 0f, 1.8f)
             scaleX.duration = 1800
             var scaleY = ObjectAnimator.ofFloat(shitview, "scaleY", 0f, 1.5f)
             scaleY.duration = 1800
             var tranY = ObjectAnimator.ofFloat(shitview, "translationY", 0f, -300f)
             tranY.duration = 1800
             var tranY2 = ObjectAnimator.ofFloat(shitview, "translationY", -300f, 300f)
             tranY2.duration = 1800
             var alpha = ObjectAnimator.ofFloat(shitview, "alpha", 1f, 0f)
             alpha.duration = 1800
             val animatorSet = AnimatorSet()
             animatorSet.play(scaleX).with(scaleY).with(tranY).before(tranY2).before(alpha)
             animatorSet.start()
             lottie_view.addAnimatorListener(object : Animator.AnimatorListener {
                 override fun onAnimationStart(animation: Animator?) {

                 }

                 override fun onAnimationEnd(animation: Animator?) {
                     lottie_view.visibility = View.GONE
                 }

                 override fun onAnimationCancel(animation: Animator?) {

                 }

                 override fun onAnimationRepeat(animation: Animator?) {

                 }

             })

         })
         anima.start()
     }*/
}