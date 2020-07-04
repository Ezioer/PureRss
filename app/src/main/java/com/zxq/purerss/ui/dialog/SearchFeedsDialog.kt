package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssFeedInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.listener.RssDiffCallback
import com.zxq.purerss.ui.mainpage.MainPageAdapter
import com.zxq.purerss.ui.mainpage.MainPageFragment
import com.zxq.purerss.ui.mainpage.MainPageFragmentDirections
import com.zxq.purerss.ui.mainpage.MainPageViewModel
import com.zxq.purerss.utils.KeyBoardUtil
import com.zxq.purerss.utils.SpringAddItemAnimator
import com.zxq.purerss.utils.addOnAfterChange
import com.zxq.purerss.utils.getSpValue
import kotlinx.android.synthetic.main.dialog_search.*

class SearchFeedsDialog(
    private val mContext: Context,
    private var mainViewModel: MainPageViewModel,
    private var mainPageFragment: MainPageFragment
) : BaseDialog(mContext,Gravity.BOTTOM, R.style.anim_bottom2top,true,1.0,0.5) {
   private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_search,null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
       KeyBoardUtil.showKeyboard(et_search)
        et_search.addOnAfterChange {
            mainViewModel.serachFeeds(it.toString())
        }
        iv_close.setOnClickListener { dismiss() }
        val onClick = object : MainPageAdapter.FeedClick {
            override fun onClick(view: View, rss: RSSFeedEntity) {
                val action = MainPageFragmentDirections.actionMainpageToList(
                    RssFeedInfo(
                        rss.feedTitle,
                        rss.feedLink,
                        rss.feedDesc,
                        "",
                        rss.feedId
                    )
                )
                mView.findNavController().navigate(action)
            }
        }
        val adapter = MainPageAdapter(onClick, context?.getSpValue("slide", 0) == 0)
        recyclerview.adapter = adapter
        recyclerview.itemAnimator = SpringAddItemAnimator()
        adapter.setDiffCallback(RssDiffCallback())
        mainViewModel.searchFeedsList.observe(mainPageFragment, Observer {
            adapter.setDiffNewData(it)
        })
    }
}