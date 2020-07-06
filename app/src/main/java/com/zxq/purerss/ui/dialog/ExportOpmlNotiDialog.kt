package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.zxq.purerss.R
import com.zxq.purerss.utils.putSpValue
import kotlinx.android.synthetic.main.dialog_opml_noti.*

class ExportOpmlNotiDialog(
    private val mContext: Context
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, false, 0.8, 0.0) {
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_export_opml_noti, null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        tv_ok.setOnClickListener { dismiss() }
        cb_nonoti.setOnCheckedChangeListener { buttonView, isChecked ->
            mContext.putSpValue("exportopmlnoti", isChecked)
        }
    }
}