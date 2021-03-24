package com.zxq.purerss.ui.dialog

import android.content.Context
import android.content.pm.ShortcutInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.listener.SelectCutsItemDiffCallback
import kotlinx.android.synthetic.main.dialog_folder.tv_ok
import kotlinx.android.synthetic.main.dialog_selectshortcuts.*

class SelectShortCutsDialog(
    private val mContext: Context,
    private val list: MutableList<ShortcutInfo>,
    private val feedList: MutableList<RSSFeedEntity>
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, true, 0.8, 0.0) {
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_selectshortcuts, null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        tv_ok.setOnClickListener {
            listener?.invoke(feedList.filter { it.state ?: true }.toMutableList())
            dismiss()
        }

        tv_cancel.setOnClickListener { dismiss() }

        val adapter = SelectShortcutsAdapter()
        rv_feeds.adapter = adapter
        adapter.setDiffCallback(SelectCutsItemDiffCallback())
        adapter.setDiffNewData(feedList)

    }

    private var listener: ((MutableList<RSSFeedEntity>) -> Unit)? = null
    fun setListener(select: (MutableList<RSSFeedEntity>) -> Unit) {
        listener = select
    }

    /*interface OnSelectResultListener {
        fun select(list: MutableList<RSSFeedEntity>)
    }*/
}