package com.zxq.purerss.ui.dialog

import android.content.Context
import android.content.pm.ShortcutInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.listener.SelectCutsItemDiffCallback

class SelectShortCutsDialog(
    private val mContext: Context,
    private val list: MutableList<ShortcutInfo>,
    private val feedList: MutableList<RSSFeedEntity>
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, true, 0.8, 0.0) {
    private lateinit var mView: View
    private lateinit var tvOk: TextView
    private lateinit var tvCancel: TextView
    private lateinit var rvFeeds: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_selectshortcuts, null)
        setContentView(mView)
        tvOk = findViewById(R.id.tv_ok)
        tvCancel = findViewById(R.id.tv_cancel)
        rvFeeds = findViewById(R.id.rv_feeds)
        initView()
    }

    private fun initView() {
        tvOk.setOnClickListener {
            listener?.invoke(feedList.filter { it.state ?: true }.toMutableList())
            dismiss()
        }

        tvCancel.setOnClickListener { dismiss() }

        val adapter = SelectShortcutsAdapter()
        rvFeeds.adapter = adapter
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