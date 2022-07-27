package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.zxq.purerss.R
import com.zxq.purerss.utils.KeyBoardUtil

class NewFolderDialog(
    private val mContext: Context
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, true, 0.8, 0.0) {
    private lateinit var mView: View
    private lateinit var etFolder: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_newfolder, null)
        setContentView(mView)
        etFolder = findViewById(R.id.et_folder)
        initView()
    }

    private fun initView() {
        KeyBoardUtil.showKeyboard(etFolder)
        findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            if (etFolder.text.toString().isNotEmpty()) {
                listener?.invoke(etFolder.text.toString())
                dismiss()
            }
        }

        findViewById<TextView>(R.id.tv_cancel).setOnClickListener { dismiss() }
    }

    private var listener: ((String) -> Unit)? = null
    fun setListener(success: (String) -> Unit) {
        listener = success
    }
}