package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.listener.FolderClickListener
import com.zxq.purerss.listener.ItemTypeClickListener
import com.zxq.purerss.listener.ItemTypeDiffCallback
import com.zxq.purerss.listener.RssFolderDiffCallback
import com.zxq.purerss.ui.type.FragmentType
import com.zxq.purerss.ui.type.TypeAdapter
import com.zxq.purerss.ui.type.TypeViewModel
import com.zxq.purerss.utils.*
import kotlinx.android.synthetic.main.dialog_folder.*
import kotlinx.android.synthetic.main.dialog_opml_noti.*
import kotlinx.android.synthetic.main.dialog_opml_noti.tv_ok
import kotlinx.android.synthetic.main.dialog_search.*

class AddFolderDialog(
    private val mContext: Context,
    private val list: MutableList<RSSFolderEntity>,
    private var onClick: FolderClickListener
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, true, 0.8, 0.0) {
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_folder, null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        tv_ok.setOnClickListener {
            if (!et_add.text.toString().isNullOrEmpty()) {
                addListener?.add(et_add.text.toString())
            }
        }
        val adapter = FolderAdapter(onClick)
        rv_folder.adapter = adapter
        adapter.setDiffCallback(RssFolderDiffCallback())
        adapter.setDiffNewData(list)
    }

    private var addListener: AddFolderListener? = null
    fun setAddFolderListener(addFolderListener: AddFolderListener) {
        addListener = addFolderListener
    }

    interface AddFolderListener {
        fun add(title: String)
    }
}