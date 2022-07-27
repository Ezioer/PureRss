package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.listener.FolderClickListener
import com.zxq.purerss.listener.RssFolderDiffCallback
class FolderDialog(
    private val mContext: Context,
    private val list: MutableList<RSSFolderEntity>,
    private var onClick: FolderClickListener,
    private var findNavController: NavController
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, true, 0.8, 0.0) {
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_folder, null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tv_managefolder).setOnClickListener {
            findNavController.navigate(R.id.action_mainpage_to_managefolder)
            dismiss()
        }
        val adapter = FolderAdapter(onClick)
        findViewById<RecyclerView>(R.id.rv_folder).adapter = adapter
        adapter.setDiffCallback(RssFolderDiffCallback())
        adapter.setDiffNewData(list)
    }
}