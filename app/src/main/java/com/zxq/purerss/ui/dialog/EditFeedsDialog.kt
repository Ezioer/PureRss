package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.listener.FolderClickListener
import com.zxq.purerss.listener.RssFolderDiffCallback
import com.zxq.purerss.ui.mainpage.MainPageViewModel
import kotlinx.android.synthetic.main.dialog_edit.*

class EditFeedsDialog(
    private val mContext: Context,
    private val list: MutableList<RSSFolderEntity>,
    private var item: RSSFeedEntity,
    private var findNavController: NavController,
    private var viewModel: MainPageViewModel
) : BaseDialog(mContext, Gravity.BOTTOM, R.style.anim_bottom2top, true, 1.0, 0.0) {
    private lateinit var mView: View
    private var parentId = 1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        tv_ok.setOnClickListener {
            //保存信息
            viewModel.updateFeeds(
                tv_titlevalue.text.toString(),
                tv_subtitlevalue.text.toString(),
                tv_linkvalue.text.toString(),
                parentId,
                item.feedId
            )
            dismiss()
        }
        tv_linkvalue.setText(item.feedLink)
        tv_titlevalue.setText(item.feedTitle)
        tv_subtitlevalue.setText(item.feedDesc)
        tv_folder_manage.setOnClickListener {
            dismiss()
            findNavController.navigate(R.id.action_mainpage_to_managefolder)
        }
        val adapter = EditFeedsAdapter(object : FolderClickListener {
            override fun onClick(view: View, rss: RSSFolderEntity) {
                parentId = rss.folderId
                tv_foldervalue.setText(rss.folderTitle)
            }
        })
        rv_folders.adapter = adapter
        adapter.setDiffCallback(RssFolderDiffCallback())
        adapter.setDiffNewData(list)
    }
}