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
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.listener.RssFolderDiffCallback
import com.zxq.purerss.ui.mainpage.MainPageViewModel

class EditFeedsDialog(
    private val mContext: Context,
    private val list: MutableList<RSSFolderEntity>,
    private var item: RSSFeedEntity,
    private var findNavController: NavController,
    private var viewModel: MainPageViewModel
) : BaseDialog(mContext, Gravity.BOTTOM, R.style.anim_bottom2top, true, 1.0, 0.0) {
    private lateinit var mView: View

    companion object {
        var parentId = 1L
    }

    private lateinit var tvOk: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubTitle: TextView
    private lateinit var tvLink: TextView
    private lateinit var tvFolder: TextView
    private lateinit var rvFolder: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null)
        setContentView(mView)
        tvOk = findViewById(R.id.tv_ok)
        tvTitle = findViewById(R.id.tv_titlevalue)
        tvSubTitle = findViewById(R.id.tv_subtitlevalue)
        tvLink = findViewById(R.id.tv_linkvalue)
        tvFolder = findViewById(R.id.tv_folder_manage)
        rvFolder = findViewById(R.id.rv_folders)
        initView()
    }

    private fun initView() {
        tvOk.setOnClickListener {
            //保存信息
            viewModel.updateFeeds(
                tvTitle.text.toString(),
                tvSubTitle.text.toString(),
                tvLink.text.toString(),
                parentId,
                item.feedId
            )
            dismiss()
        }
        tvLink.setText(item.feedLink)
        tvTitle.setText(item.feedTitle)
        tvSubTitle.setText(item.feedDesc)
        tvFolder.setOnClickListener {
            dismiss()
            findNavController.navigate(R.id.action_mainpage_to_managefolder)
        }
        val adapter = EditFeedsAdapter(item.parentId)
        rvFolder.adapter = adapter
        adapter.setDiffCallback(RssFolderDiffCallback())
        adapter.setDiffNewData(list)
    }
}