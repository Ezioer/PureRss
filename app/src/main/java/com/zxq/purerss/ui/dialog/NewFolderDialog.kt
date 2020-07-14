package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.zxq.purerss.R
import com.zxq.purerss.utils.KeyBoardUtil
import kotlinx.android.synthetic.main.dialog_newfolder.*

class NewFolderDialog(
    private val mContext: Context
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, true, 0.8, 0.0) {
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_newfolder, null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        KeyBoardUtil.showKeyboard(et_folder)
        tv_ok.setOnClickListener {
            if (et_folder.text.toString().isNotEmpty()) {
                listener?.success(et_folder.text.toString())
                dismiss()
            }
        }

        tv_cancel.setOnClickListener { dismiss() }
    }

    private var listener: AddFolderListener? = null
    fun setListener(l: AddFolderListener) {
        listener = l
    }

    interface AddFolderListener {
        fun success(title: String)
    }
}