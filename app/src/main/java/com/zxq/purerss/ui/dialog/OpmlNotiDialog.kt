package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.zxq.purerss.R
import com.zxq.purerss.listener.ItemTypeClickListener
import com.zxq.purerss.listener.ItemTypeDiffCallback
import com.zxq.purerss.ui.type.FragmentType
import com.zxq.purerss.ui.type.TypeAdapter
import com.zxq.purerss.ui.type.TypeViewModel
import com.zxq.purerss.utils.*
import kotlinx.android.synthetic.main.dialog_opml_noti.*
import kotlinx.android.synthetic.main.dialog_search.*

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
        tv_ok.setOnClickListener { dismiss() }
        cb_nonoti.setOnCheckedChangeListener { buttonView, isChecked ->
            mContext.putSpValue("opmlnoti", isChecked)
        }
    }
}