package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.ui.bottomdrawer.AllFeedAdapter
import com.zxq.purerss.ui.bottomdrawer.FeedClick
import kotlinx.android.synthetic.main.fragment_bottom_nav_drawer.*

/**
 * Created by xiaoqing.zhou on 2018/4/17.
 */

class MenuListBottomDialog(private val mContext: Context, private val mList: List<RSSFeedEntity>) :
    BottomSheetDialog(mContext) {
    override fun onStart() {
        super.onStart()
        val window = window
        val windowparams = window?.attributes
        windowparams?.height = WindowManager.LayoutParams.MATCH_PARENT
        windowparams?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.setBackgroundDrawableResource(R.drawable.bottom_sheet_bg)
        window?.attributes = windowparams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_bottom_nav_drawer, null)
        setContentView(mView)

        var onclick: FeedClick = object : FeedClick {
            override fun onClick(view: View, rss: RSSFeedEntity) {
                LiveDataBus.get<RSSFeedEntity>("selectfeed").postValue(rss)
                dismiss()
            }
        }
        val adapter = AllFeedAdapter(mContext, onclick)
        nav_recycler_view.adapter = adapter
        adapter.submitList(mList)
    }
}
