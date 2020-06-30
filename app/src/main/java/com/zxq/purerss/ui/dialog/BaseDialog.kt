package com.zxq.purerss.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/30
 *  fun
 */
open class BaseDialog(private val mContext: Context, val gravity: Int, val animation: Int, val isCanCancel: Boolean, val widthPercantage: Double, val heightPercantage: Double) : Dialog(mContext) {
    override fun onStart() {
        super.onStart()
        val window = window?.apply {
            setWindowAnimations(animation)
            setGravity(gravity)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        val windowparams = window?.attributes?.apply {
            height =  if(heightPercantage ==0.0) WindowManager.LayoutParams.WRAP_CONTENT else  (mContext.resources.displayMetrics.heightPixels * heightPercantage).toInt()
            width = (mContext.resources.displayMetrics.widthPixels * widthPercantage).toInt()
        }
        window?.attributes = windowparams
        setCanceledOnTouchOutside(isCanCancel)
    }
}