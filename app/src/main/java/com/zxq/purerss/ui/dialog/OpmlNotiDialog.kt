package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.zxq.purerss.R
import com.zxq.purerss.utils.*

class OpmlNotiDialog(
    private val mContext: Context
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, false, 0.8, 0.0) {
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_opml_noti, null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        findViewById<TextView>(R.id.tv_ok).setOnClickListener { dismiss() }
        findViewById<CheckBox>(R.id.cb_nonoti).setOnCheckedChangeListener { buttonView, isChecked ->
            mContext.putSpValue("opmlnoti", isChecked)
        }
    }
}