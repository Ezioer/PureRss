package com.zxq.purerss.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zxq.purerss.R
import com.zxq.purerss.databinding.FragmentLaunchBinding
import com.zxq.purerss.utils.PixelUtil


/**
 *  created by xiaoqing.zhou
 *  on 2020/7/20
 *  fun
 */
class LaunchFragment : Fragment() {
    private var binding: FragmentLaunchBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchBinding.inflate(inflater, container, false).apply {
            val list = arrayListOf<SpringAnimation>()
            for (i in 0 until llName.childCount) {
                val view = llName.getChildAt(i)
                view.translationY = PixelUtil.getScreenHeight(context!!).toFloat()
                val letterAnimY = SpringAnimation(view, SpringAnimation.TRANSLATION_Y, 0f)
                letterAnimY.spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
                letterAnimY.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                list.add(letterAnimY)
            }

            ivLogo.apply {
                translationY = 400f
                alpha = 0f
            }
            val logoAni = SpringAnimation(ivLogo, SpringAnimation.TRANSLATION_Y, 0f)
            logoAni.spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
            logoAni.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
            logoAni.setStartVelocity(-2000f)

            ivLogoRight.apply {
                translationY = 400f
                alpha = 0f
            }
            val logoAniRight = SpringAnimation(ivLogoRight, SpringAnimation.TRANSLATION_Y, 0f)
            logoAniRight.spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
            logoAniRight.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
            logoAniRight.setStartVelocity(-2000f)

            tvAppfeature.translationY = 500f
            tvAppfeature.alpha = 0f
            val featureAni = SpringAnimation(tvAppfeature, SpringAnimation.TRANSLATION_Y, 0f)
            featureAni.spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
            featureAni.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
            val featureAlphaAnim = ObjectAnimator.ofFloat(0f, 1f)
            featureAlphaAnim.duration = 300
            featureAlphaAnim.addUpdateListener { valueAnimator ->
                tvAppfeature.setAlpha(
                    valueAnimator.animatedValue as Float
                )
            }

            val logoAlphaAnim = ObjectAnimator.ofFloat(0f, 1f)
            logoAlphaAnim.duration = 600
            logoAlphaAnim.addUpdateListener { valueAnimator ->
                ivLogo.setAlpha(valueAnimator.animatedValue as Float)
                ivLogoRight.setAlpha(valueAnimator.animatedValue as Float)
            }

            ivLogo.postDelayed({
                logoAni.start()
                ivLogo.postDelayed({
                    logoAniRight.start()
                }, 150)
                tvAppfeature.postDelayed({
                    featureAlphaAnim.startDelay = 50
                    featureAlphaAnim.start()
                    featureAni.start()
                }, 200)
                for (i in 0 until list.size) {
                    llName.postDelayed(
                        {
                            list[i].start()
                            if (i == list.size - 1) {
                                list[i].addEndListener { animation, canceled, value, velocity ->
                                    Thread.sleep(200)
                                    findNavController().navigate(R.id.action_launch_to_choose)
                                }
                            }
                        },
                        12 * i.toLong()
                    )
                }
                logoAlphaAnim.start()
            }, 500)
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}