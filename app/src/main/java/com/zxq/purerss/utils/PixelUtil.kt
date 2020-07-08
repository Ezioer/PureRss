package com.zxq.purerss.utils

import android.content.Context

/**
 * Created by xiaoqing.zhou
 * on  2018/12/6
 */
class PixelUtil {
    companion object {
        fun getScreenWidth(context: Context): Int {
            return context.resources.displayMetrics.widthPixels
        }

        fun getScreenHeight(context: Context): Int {
            return context.resources.displayMetrics.heightPixels
        }


        fun getScreenDensity(context: Context): Float {
            return context.resources.displayMetrics.density
        }

        fun dp2px(context: Context, dipValue: Float): Int {
            val scale = getScreenDensity(context)
            return (dipValue * scale + 0.5).toInt()
        }

        fun px2dp(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        fun px2sp(context: Context, value: Float): Int {
            val scale = context.resources.displayMetrics.scaledDensity
            return (value / scale + 0.5f).toInt()
        }

        fun px2sp2f(context: Context, value: Float): Float {
            val scale = context.resources.displayMetrics.scaledDensity
            return (value / scale + 0.5f)
        }

    }
}